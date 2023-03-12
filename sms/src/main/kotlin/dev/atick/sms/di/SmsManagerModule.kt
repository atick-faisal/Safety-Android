@file:Suppress("DEPRECATION")

package dev.atick.sms.di

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SmsManagerModule {

    @Provides
    @Singleton
    fun provideSmsManager(
        @ApplicationContext context: Context
    ): SmsManager {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            SmsManager.getDefault()
        } else {
            context.getSystemService(SmsManager::class.java)
        }
    }
}