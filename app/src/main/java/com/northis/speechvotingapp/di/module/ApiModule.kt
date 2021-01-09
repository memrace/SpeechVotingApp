package com.northis.speechvotingapp.di.module

import android.app.Application
import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.northis.speechvotingapp.authentication.IUserTokenStorage
import com.northis.speechvotingapp.authentication.UnsafeConnection
import com.northis.speechvotingapp.network.IUserService
import dagger.Module
import dagger.Provides
import io.ktor.client.*
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import javax.net.ssl.X509TrustManager


@Module
class ApiModule(private val baseUrl: String) {
    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache? {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson? {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        return gsonBuilder.create()
    }
    @Provides
    @Singleton
    fun provideInterceptor(storage: IUserTokenStorage, context: Context): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val newRequest = request
                .newBuilder()
                .addHeader("Authorization", "Bearer ${storage.getAccessToken(context)}")
                .build()
            return@Interceptor chain.proceed(newRequest)
        }
    }
    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache?, interceptor: Interceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        with(client){
            sslSocketFactory(UnsafeConnection.getSslSocketFactory(), UnsafeConnection.getTrustAllCerts()[0] as X509TrustManager)
            hostnameVerifier { _, _ -> true }
            cache(cache)
            addInterceptor(interceptor)
        }
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson?, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): IUserService{
        return retrofit.create(IUserService::class.java)
    }

}