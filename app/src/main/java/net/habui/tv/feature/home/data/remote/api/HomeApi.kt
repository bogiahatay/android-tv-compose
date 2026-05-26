package net.habui.tv.feature.home.data.remote.api

import net.habui.tv.feature.home.data.remote.dto.HomeResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface HomeApi {
    @GET("home")
    suspend fun getHome(): Response<HomeResponseDto>
}
