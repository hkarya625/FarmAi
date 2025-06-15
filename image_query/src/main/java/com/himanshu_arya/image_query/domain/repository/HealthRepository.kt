package com.himanshu_arya.image_query.domain.repository

import com.himanshu_arya.image_query.domain.model.PlantHealthDomain
import com.himanshu_arya.image_query.domain.network.ResultWrapper

interface HealthRepository {
    suspend fun getHealth(image:String):ResultWrapper<PlantHealthDomain>
}