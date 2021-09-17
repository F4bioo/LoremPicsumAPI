package com.fappslab.lorempicsumapi.di

import com.fappslab.lorempicsumapi.BuildConfig
import com.fappslab.lorempicsumapi.data.api.Api
import com.fappslab.lorempicsumapi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideLevel(
    ): HttpLoggingInterceptor.Level {
        return HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideHttpLogging(
        level: HttpLoggingInterceptor.Level
    ): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = level
        return logging
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            client.addInterceptor(logging)
        }
        return client.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
    }

    @Singleton
    @Provides
    fun provideApi(
        retrofit: Retrofit.Builder
    ): Api {
        return retrofit
            .build()
            .create(Api::class.java)
    }
}
