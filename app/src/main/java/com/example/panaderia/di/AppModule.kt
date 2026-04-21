package com.example.panaderia.di

import android.content.Context
import com.example.panaderia.data.hardware.*
import com.example.panaderia.data.repository.CartRepositoryImpl
import com.example.panaderia.data.repository.ProductRepositoryImpl
import com.example.panaderia.data.repository.UserRepositoryImpl
import com.example.panaderia.domain.repository.IUserRepository
import com.example.panaderia.domain.hardware.*
import com.example.panaderia.domain.repository.ICartRepository
import com.example.panaderia.domain.repository.ICustomerProductRepository
import com.example.panaderia.domain.repository.ProductRepository
import com.example.panaderia.data.remote.PanaderiaApi
import com.example.panaderia.data.remote.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val token = tokenManager.getToken()
            val request = chain.request().newBuilder()
            
            if (token != null) {
                request.addHeader("Authorization", "Bearer $token")
            }
            
            chain.proceed(request.build())
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun providePanaderiaApi(client: OkHttpClient): PanaderiaApi {
        return Retrofit.Builder()
            .baseUrl(PanaderiaApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PanaderiaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCamera(@ApplicationContext context: Context): ICameraManager = AndroidCameraManager(context)

    @Provides
    @Singleton
    fun provideGps(): IGpsManager = AndroidGpsManager()

    @Provides
    @Singleton
    fun provideAuth(): IAuthManager = AndroidAuthManager()

    @Provides
    @Singleton
    fun provideVibrator(@ApplicationContext context: Context): IVibratorManager = AndroidVibratorManager(context)

    @Provides
    @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): IUserRepository = impl

    @Provides
    @Singleton
    fun provideProductRepository(impl: ProductRepositoryImpl): ProductRepository = impl

    @Provides
    @Singleton
    fun provideCustomerRepository(impl: ProductRepositoryImpl): ICustomerProductRepository = impl

    @Provides
    @Singleton
    fun provideCartRepository(impl: CartRepositoryImpl): ICartRepository = impl
}
