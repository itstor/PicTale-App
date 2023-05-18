package com.itstor.pictale

import com.itstor.pictale.data.local.entity.StoryEntity

object DataDummy {
    fun generateDummyStoryEntity(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryEntity(
                i.toString(),
                "author + $i",
                "desc $i",
                "https://picsum.photos/200/300?random=$i",
                0.0,
                0.0,
                "2023-09-01T00:00:00.000Z"
            )
            items.add(quote)
        }
        return items
    }
}