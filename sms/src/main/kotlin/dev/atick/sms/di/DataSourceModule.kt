package dev.atick.sms.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.sms.data.SmsDataSource
import dev.atick.sms.data.SmsDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindSmsDataSource(
        smsDataSourceImpl: SmsDataSourceImpl
    ): SmsDataSource

}