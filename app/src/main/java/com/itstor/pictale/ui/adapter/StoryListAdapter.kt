package com.itstor.pictale.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itstor.pictale.R
import com.itstor.pictale.data.remote.response.DetailStoryResponse
import com.itstor.pictale.databinding.PostItemsBinding
import com.itstor.pictale.utils.StoryDiffCallback
import com.itstor.pictale.utils.TimeUtils
import eightbitlab.com.blurview.RenderScriptBlur

class StoryListAdapter : RecyclerView.Adapter<StoryListAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: PostItemsBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listStory = ArrayList<DetailStoryResponse>()

    fun setData(listStory: ArrayList<DetailStoryResponse>) {
        val diffCallback = StoryDiffCallback(this.listStory, listStory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listStory.clear()
        this.listStory.addAll(listStory)
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DetailStoryResponse)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = PostItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = listStory[position]

        val radius = 20f

        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(story.photoUrl).placeholder(R.color.silver)
                .into(ivItemPhoto)

            blurViewPostAuthor.setupWith(
                holder.binding.root,
                RenderScriptBlur(holder.itemView.context)
            )
                .setFrameClearDrawable(ivItemPhoto.background)
                .setBlurRadius(radius)

            btnPostDetail.setOnClickListener {
                onItemClickCallback.onItemClicked(listStory[holder.adapterPosition])
            }

            tvDate.text = TimeUtils.getTimeAgo(story.createdAt)

            tvItemName.text = story.name
        }
    }

    override fun getItemCount(): Int = listStory.size
}