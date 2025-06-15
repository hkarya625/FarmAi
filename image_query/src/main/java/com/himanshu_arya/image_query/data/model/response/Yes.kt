package com.himanshu_arya.image_query.data.model.response

data class Yes(
    val entity_id: String,
    val name: String,
    val suggestion_index: Int,
    val translation: String
)