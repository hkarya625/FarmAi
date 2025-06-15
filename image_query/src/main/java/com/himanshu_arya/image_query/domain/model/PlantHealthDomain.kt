package com.himanshu_arya.image_query.domain.model

data class PlantHealthDomain(
    val healthStatus: PlantHealthStatus,
    val mainIssue1: DiseaseSuggestion,
    val mainIssue2: DiseaseSuggestion,
    val followUpQuestion: String,
    val optionYes: QuestionOption,
    val optionNo: QuestionOption
)

data class PlantHealthStatus(
    val isPlantProbability: Double,
    val isHealthyProbability: Double
)

data class DiseaseSuggestion(
    val name: String,
    val probability: Double
)

data class QuestionOption(
    val answerText: String,
    val relatedDiseaseName: String
)
