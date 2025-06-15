package com.himanshu_arya.voice_input.di

import com.himanshu_arya.voice_input.domain.repository.SpeechToTextRepository
import com.himanshu_arya.voice_input.data.repository.SpeechToTextRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Install in Application scope
abstract class RepositoryModule {

    @Binds
    @Singleton // Match the scope of the implementation
    abstract fun bindSpeechToTextRepository(
        speechToTextRepositoryImpl: SpeechToTextRepositoryImpl
    ): SpeechToTextRepository
}