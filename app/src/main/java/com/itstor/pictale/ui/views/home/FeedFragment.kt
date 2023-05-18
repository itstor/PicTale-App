package com.itstor.pictale.ui.views.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.itstor.pictale.R
import com.itstor.pictale.common.Status
import com.itstor.pictale.data.remote.response.DetailStoryResponse
import com.itstor.pictale.databinding.FragmentFeedBinding
import com.itstor.pictale.ui.adapter.StoryListAdapter
import com.itstor.pictale.ui.decoration.MarginItemDecoration
import com.itstor.pictale.ui.views.story_detail.StoryDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private val viewModel: FeedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StoryListAdapter()
        val userListLayoutManager = LinearLayoutManager(context)
        binding.rvFeed.layoutManager = userListLayoutManager
        binding.rvFeed.addItemDecoration(MarginItemDecoration(24))
        binding.srlFeed.setOnRefreshListener {
            viewModel.storiesParams.value = Triple(null, null, null)
        }

        viewModel.stories.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.pbFeed.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.pbFeed.visibility = View.GONE
                    binding.srlFeed.isRefreshing = false

                    if (it.data.isNullOrEmpty()) {
                        showToast(getString(R.string.msg_no_feed))
                        return@observe
                    }

                    adapter.setData(ArrayList(it.data))
                    adapter.setOnItemClickCallback(object : StoryListAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: DetailStoryResponse) {
                            val intent = Intent(context, StoryDetailActivity::class.java)
                            intent.putExtra(StoryDetailActivity.EXTRA_STORY, data)

                            startActivity(intent)
                        }
                    })

                    binding.rvFeed.adapter = adapter
                }

                Status.FAILURE -> {
                    binding.pbFeed.visibility = View.GONE
                    showToast(it.message ?: getString(R.string.msg_something_went_wrong))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.storiesParams.value = Triple(null, null, null)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}