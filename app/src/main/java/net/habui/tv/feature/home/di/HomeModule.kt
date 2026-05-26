package net.habui.tv.feature.home.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.habui.tv.feature.home.data.remote.api.HomeApi
import net.habui.tv.feature.home.data.remote.datasource.HomeRemoteDataSource
import net.habui.tv.feature.home.data.remote.datasource.RetrofitHomeRemoteDataSource
import net.habui.tv.feature.home.data.repository.HomeRepositoryImpl
import net.habui.tv.feature.home.domain.repository.HomeRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindHomeRemoteDataSource(
        dataSource: RetrofitHomeRemoteDataSource
    ): HomeRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        repository: HomeRepositoryImpl
    ): HomeRepository

    companion object {
        @Provides
        @Singleton
        fun provideHomeApi(retrofit: Retrofit): HomeApi {
            return retrofit.create(HomeApi::class.java)
        }
    }
}