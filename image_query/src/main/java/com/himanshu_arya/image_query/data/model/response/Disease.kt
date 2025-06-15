package com.himanshu_arya.image_query.data.model.response

data class Disease(
    val question: Question,
    val suggestions: List<Suggestion>
)