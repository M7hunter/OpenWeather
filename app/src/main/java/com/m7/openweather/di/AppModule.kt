package com.m7.openweather.di

import android.content.Context
import com.m7.openweather.BuildConfig
import com.m7.openweather.ConnectivityHandler
import com.m7.openweather.data.remote.APIs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideHomeAPIs(retrofit: Retrofit): APIs =
        retrofit.create(APIs::class.java)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG)
                addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
        }.build()

    @Provides
    fun provideConnectivityHandler(@ApplicationContext context: Context): ConnectivityHandler =
        ConnectivityHandler(context)
}