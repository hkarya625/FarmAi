package com.himanshu_arya.image_query.data.model.response

data class IsHealthy(
    val binary: Boolean,
    val probability: Double,
    val threshold: Double
)