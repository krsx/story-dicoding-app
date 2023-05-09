package com.example.proyekakhirstoryapp.data

import com.example.proyekakhirstoryapp.data.db.model.StoryModel

object DataDummy {
    fun generateDummyStoriesEntity(): List<StoryModel> {
        val storiesList = ArrayList<StoryModel>()
        for (i in 0..10) {
            val story = StoryModel(
                "id",
                "name",
                "description",
                "image",
                "createdAt"
            )
            storiesList.add(story)
        }
        return storiesList
    }
}