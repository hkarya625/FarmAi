package com.himanshu_arya.chatbot.di

import com.himanshu_arya.chatbot.data.remote.NetworkServiceImpl
import com.himanshu_arya.chatbot.data.repository.GeminiRepositoryImpl
import com.himanshu_arya.chatbot.domain.network.NetworkService
import com.himanshu_arya.chatbot.domain.repository.GeminiRepository
import com.himanshu_arya.chatbot.domain.usecase.GetGeminiResponseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object GeminiModule {


    @Provides
    fun provideGeminiDataSource(): NetworkService = NetworkServiceImpl()

    @Provides
    fun provideGeminiRepository(dataSource: NetworkService): GeminiRepository = GeminiRepositoryImpl(dataSource)

    @Provides
    fun provideGetGeminiResponseUseCase(repository: GeminiRepository): GetGeminiResponseUseCase = GetGeminiResponseUseCase(repository)
}