package com.himanshu_arya.image_query.utils

import kotlin.math.pow
import kotlin.math.round

fun roundToDecimals(value: Double): Double {
    val factor = 10.0.pow(3)
    return round(value * factor) / factor
}