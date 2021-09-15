package com.fappslab.lorempicsumapi.utils

interface BaseUseCase<T, Params> {
    suspend operator fun invoke(params: Params): T
}
