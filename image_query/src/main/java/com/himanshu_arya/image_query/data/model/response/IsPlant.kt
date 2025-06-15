package com.himanshu_arya.image_query.data.model.response

data class IsPlant(
    val binary: Boolean,
    val probability: Double,
    val threshold: Double
)