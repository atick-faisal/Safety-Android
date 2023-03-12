package dev.atick.sms.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.sms.utils.SmsHelper
import dev.atick.sms.utils.SmsHelperImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SmsHelperModule {

    @Binds
    @Singleton
    abstract fun bindSmsHelper(
        smsHelperImpl: SmsHelperImpl
    ): SmsHelper
}