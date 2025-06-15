package com.himanshu_arya.image_query.domain.usecase

import com.himanshu_arya.image_query.domain.repository.HealthRepository
import javax.inject.Inject

class PlantHealthUseCase @Inject constructor(
    private val healthRepository: HealthRepository
) {
    suspend operator fun invoke(image:String) = healthRepository.getHealth(image)
}