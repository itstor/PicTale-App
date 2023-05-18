package com.itstor.pictale.ui.views.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.itstor.pictale.data.local.entity.StoryEntity
import com.itstor.pictale.databinding.FragmentFeedBinding
import com.itstor.pictale.ui.adapter.LoadingStateAdapter
import com.itstor.pictale.ui.adapter.StoryListAdapter
import com.itstor.pictale.ui.decoration.MarginItemDecoration
import com.itstor.pictale.ui.views.story_detail.StoryDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private val viewModel: FeedViewModel by viewModels()

    private lateinit var adapter: StoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userListLayoutManager = LinearLayoutManager(context)
        binding.rvFeed.layoutManager = userListLayoutManager
        binding.rvFeed.addItemDecoration(MarginItemDecoration(24))

        adapter = StoryListAdapter()
        adapter.setOnItemClickCallback(object : StoryListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StoryEntity) {
                val intent = Intent(context, StoryDetailActivity::class.java)
                intent.putExtra(StoryDetailActivity.EXTRA_STORY, data)

                startActivity(intent)
            }
        })

        binding.rvFeed.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        binding.swipeContainer.setOnRefreshListener {
            adapter.refresh()
        }

        adapter.addOnPagesUpdatedListener {
            if (binding.swipeContainer.isRefreshing) {
                binding.swipeContainer.isRefreshing = false
            }
        }

        lifecycleScope.launch {
            viewModel.getStories().collect {
                adapter.submitData(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}