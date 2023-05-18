package com.itstor.pictale.ui.views.story_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.itstor.pictale.R
import com.itstor.pictale.data.local.entity.StoryEntity
import com.itstor.pictale.databinding.ActivityStoryDetailBinding
import com.itstor.pictale.utils.TimeUtils

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var story: StoryEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extraStory = intent.getParcelableExtra<StoryEntity>(EXTRA_STORY)
        if (extraStory == null) {
            finish()
            return
        }

        story = extraStory

        setUpToolbar()

        binding.apply {
            tvDetailDate.text = TimeUtils.getTimeAgo(story.createdAt)
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description

            Glide.with(this@StoryDetailActivity)
                .load(story.photoUrl)
                .placeholder(R.color.silver)
                .into(ivDetailPhoto)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.mainToolbar)
        title = getString(R.string.title_story_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val materialShapeDrawable = binding.mainToolbar.background as MaterialShapeDrawable
        materialShapeDrawable.shapeAppearanceModel = materialShapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setBottomLeftCorner(CornerFamily.ROUNDED, 64f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 64f)
            .build()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_STORY = "story"
    }
}