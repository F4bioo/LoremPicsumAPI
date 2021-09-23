package com.fappslab.lorempicsumapi.data.usecase

interface BaseUseCase {
    interface Params<T, Params> {
        suspend operator fun invoke(params: Params): T
    }

    interface Empty<T> {
        suspend operator fun invoke(): T
    }
}
