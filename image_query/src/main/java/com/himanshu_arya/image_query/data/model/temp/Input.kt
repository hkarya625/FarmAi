package com.himanshu_arya.image_query.data.model.temp

data class Input(
    val datetime: String,
    val health: String,
    val images: List<String>,
    val latitude: Double,
    val longitude: Double,
    val similar_images: Boolean
)