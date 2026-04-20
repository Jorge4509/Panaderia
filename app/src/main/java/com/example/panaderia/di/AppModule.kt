package com.example.panaderia.di

import android.content.Context
import com.example.panaderia.data.repository.MockUserRepository
import com.example.panaderia.data.hardware.*
import com.example.panaderia.data.repository.CartRepositoryImpl
import com.example.panaderia.data.repository.ProductRepositoryImpl
import com.example.panaderia.domain.repository.IUserRepository
import com.example.panaderia.domain.hardware.*
import com.example.panaderia.domain.repository.ICartRepository
import com.example.panaderia.domain.repository.ICustomerProductRepository
import com.example.panaderia.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideUserRepository(mockUserRepository: MockUserRepository): IUserRepository = mockUserRepository

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
