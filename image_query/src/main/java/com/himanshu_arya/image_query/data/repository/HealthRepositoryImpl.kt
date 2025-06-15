package com.himanshu_arya.image_query.data.repository

import com.himanshu_arya.image_query.domain.model.PlantHealthDomain
import com.himanshu_arya.image_query.domain.network.NetworkService
import com.himanshu_arya.image_query.domain.network.ResultWrapper
import com.himanshu_arya.image_query.domain.repository.HealthRepository
import javax.inject.Inject

class HealthRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
):HealthRepository {
    override suspend fun getHealth(image: String): ResultWrapper<PlantHealthDomain> {
        return networkService.plantHealth(image)
    }
}