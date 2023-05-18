package com.itstor.pictale.utils

import androidx.recyclerview.widget.DiffUtil
import com.itstor.pictale.data.remote.response.DetailStoryResponse

class StoryDiffCallback(
    private val mOldStoryList: List<DetailStoryResponse>,
    private val mNewStoryList: List<DetailStoryResponse>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldStoryList.size
    }

    override fun getNewListSize(): Int {
        return mNewStoryList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldStoryList[oldItemPosition].id == mNewStoryList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldStoryList[oldItemPosition]
        val newFavorite = mNewStoryList[newItemPosition]
        return oldFavorite.id == newFavorite.id && oldFavorite.description == newFavorite.description
    }
}