package com.itstor.pictale.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itstor.pictale.R
import com.itstor.pictale.data.local.entity.StoryEntity
import com.itstor.pictale.databinding.PostItemsBinding
import com.itstor.pictale.utils.TimeUtils
import eightbitlab.com.blurview.RenderScriptBlur

class StoryListAdapter :
    PagingDataAdapter<StoryEntity, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryEntity)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = PostItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(
        private val binding: PostItemsBinding,
        private val onItemClickCallback: OnItemClickCallback
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: StoryEntity) {
            val radius = 20f

            binding.apply {
                Glide.with(itemView.context)
                    .load(data.photoUrl).placeholder(R.color.silver)
                    .into(ivItemPhoto)

                blurViewPostAuthor.setupWith(
                    binding.root,
                    RenderScriptBlur(itemView.context)
                )
                    .setFrameClearDrawable(ivItemPhoto.background)
                    .setBlurRadius(radius)

                btnPostDetail.setOnClickListener {
                    this@MyViewHolder.onItemClickCallback.onItemClicked(data)
                }

                tvDate.text = TimeUtils.getTimeAgo(data.createdAt)

                tvItemName.text = data.name
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}