package net.habui.tv.feature.home.data.remote.datasource

import javax.inject.Inject
import net.habui.tv.feature.home.data.remote.api.HomeApi
import net.habui.tv.feature.home.data.remote.dto.HomeResponseDto
import retrofit2.Response

interface HomeRemoteDataSource {
    suspend fun getHome(): Response<HomeResponseDto>
}

class RetrofitHomeRemoteDataSource @Inject constructor(
    private val api: HomeApi
) : HomeRemoteDataSource {
    override suspend fun getHome(): Response<HomeResponseDto> = api.getHome()
}
