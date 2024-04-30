package com.example.moengage_task.model

import com.example.moengage_task.model.Article
import com.google.gson.annotations.SerializedName

data class ResponseData(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("status")
    val status: String
)