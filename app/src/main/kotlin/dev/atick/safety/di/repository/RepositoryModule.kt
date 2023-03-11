package dev.atick.safety.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.safety.repository.auth.AuthRepository
import dev.atick.safety.repository.auth.AuthRepositoryImpl
import dev.atick.safety.repository.content.ContentRepository
import dev.atick.safety.repository.content.ContentRepositoryImpl
import dev.atick.safety.repository.intro.IntroRepository
import dev.atick.safety.repository.intro.IntroRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindIntroRepository(
        introRepositoryImpl: IntroRepositoryImpl
    ): IntroRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindContentRepository(
        contentRepositoryImpl: ContentRepositoryImpl
    ): ContentRepository
}