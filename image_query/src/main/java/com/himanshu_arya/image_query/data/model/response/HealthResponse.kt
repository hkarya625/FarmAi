package com.himanshu_arya.image_query.data.model.response

import com.himanshu_arya.image_query.domain.model.DiseaseSuggestion
import com.himanshu_arya.image_query.domain.model.PlantHealthDomain
import com.himanshu_arya.image_query.domain.model.PlantHealthStatus
import com.himanshu_arya.image_query.domain.model.QuestionOption

data class HealthResponse(
    val access_token: String,
    val completed: Double,
    val created: Double,
    val custom_id: Any,
    val input: Input,
    val model_version: String,
    val result: Result,
    val sla_compliant_client: Boolean,
    val sla_compliant_system: Boolean,
    val status: String
){
    fun toDomainResponse() = PlantHealthDomain(
        healthStatus = PlantHealthStatus(
            isPlantProbability = result.is_plant.probability,
            isHealthyProbability = result.is_healthy.probability
        ),
        mainIssue1 = DiseaseSuggestion(
            name = result.disease.suggestions.getOrNull(0)?.name ?: "Unknown",
            probability = result.disease.suggestions.getOrNull(0)?.probability ?: 0.0
        ),
        mainIssue2 = DiseaseSuggestion(
            name = result.disease.suggestions.getOrNull(1)?.name ?: "Unknown",
            probability = result.disease.suggestions.getOrNull(1)?.probability ?: 0.0
        ),
        followUpQuestion = result.disease.question?.text ?: "No follow-up question",
        optionYes = QuestionOption(
            answerText = result.disease.question?.options?.yes?.translation ?: "Yes",
            relatedDiseaseName = result.disease.question?.options?.yes?.name ?: "Unknown"
        ),
        optionNo = QuestionOption(
            answerText = result.disease.question?.options?.no?.translation ?: "No",
            relatedDiseaseName = result.disease.question?.options?.no?.name ?: "Unknown"
        )
    )
}