package com.himanshu_arya.image_query.di

import com.himanshu_arya.image_query.data.remote.ApiService
import com.himanshu_arya.image_query.data.remote.NetworkServiceImpl
import com.himanshu_arya.image_query.domain.network.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkServiceModule {

    @Provides
    @Singleton
    fun provideNetworkModule(api:ApiService):NetworkService{
        return NetworkServiceImpl(api)
    }
}