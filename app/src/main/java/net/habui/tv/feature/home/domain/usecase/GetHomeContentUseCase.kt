package net.habui.tv.feature.home.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.habui.tv.core.result.Resource
import net.habui.tv.feature.home.domain.model.HomeContent
import net.habui.tv.feature.home.domain.repository.HomeRepository

class GetHomeContentUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<Resource<HomeContent>> = flow {
        emit(Resource.Loading)
        emit(repository.getHome())
    }
}
