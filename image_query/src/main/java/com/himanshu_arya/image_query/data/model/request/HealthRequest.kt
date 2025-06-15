package com.himanshu_arya.image_query.data.model.request


data class HealthRequest(
    val images: List<String>,
    val latitude: Double ,
    val longitude: Double,
    val similar_images: Boolean
//    val modifiers: List<String> = listOf("crops_fast", "similar_images"),
//    val plant_details: List<String> = listOf("common_names", "url", "wiki_description", "taxonomy"),
//    val disease_details: List<String> = listOf("description", "treatment"),
//    val language: String = "en"
)