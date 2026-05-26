package net.habui.tv.core.result

sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val error: AppError) : Resource<Nothing>
}

sealed interface AppError {
    data object Empty : AppError
    data object Timeout : AppError
    data object Network : AppError
    data object Unknown : AppError
    data class Api(val code: Int) : AppError
}
