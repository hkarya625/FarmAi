package com.himanshu_arya.image_query.data.model.temp

data class Response(
    val access_token: String,
    val completed: Double,
    val created: Double,
    val custom_id: Any,
    val input: Input,
    val model_version: String,
    val result: Result
)