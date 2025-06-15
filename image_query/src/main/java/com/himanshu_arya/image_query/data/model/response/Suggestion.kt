package com.himanshu_arya.image_query.data.model.response

data class Suggestion(
    val details: Details,
    val id: String,
    val name: String,
    val probability: Double,
    val similar_images: List<SimilarImage>
)