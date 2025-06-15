package com.himanshu_arya.image_query.data.model.temp

data class SimilarImage(
    val citation: String,
    val id: String,
    val license_name: String,
    val license_url: String,
    val similarity: Double,
    val url: String,
    val url_small: String
)