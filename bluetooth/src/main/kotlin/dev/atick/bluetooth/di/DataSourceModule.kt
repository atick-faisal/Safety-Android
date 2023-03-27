package dev.atick.bluetooth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.bluetooth.data.BtDataSource
import dev.atick.bluetooth.data.BtDataSourceBleImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindBtDataSource(
        btDataSourceImpl: BtDataSourceBleImpl
    ): BtDataSource
}