package com.himanshu_arya.image_query.di

import com.himanshu_arya.image_query.data.repository.HealthRepositoryImpl
import com.himanshu_arya.image_query.domain.repository.HealthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHealthRepository(
        impl: HealthRepositoryImpl
    ): HealthRepository
}