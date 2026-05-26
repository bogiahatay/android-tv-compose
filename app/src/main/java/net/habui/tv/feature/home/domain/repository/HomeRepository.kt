package net.habui.tv.feature.home.domain.repository

import net.habui.tv.core.result.Resource
import net.habui.tv.feature.home.domain.model.HomeContent

interface HomeRepository {
    suspend fun getHome(): Resource<HomeContent>
}
