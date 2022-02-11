package com.nahlasamir244.flickrsearchapp.di

import android.content.Context
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.nahlasamir244.flickrsearchapp.data.api.PhotoApiService
import com.nahlasamir244.flickrsearchapp.data.datasource.photo.local.PhotoLocalDataSource
import com.nahlasamir244.flickrsearchapp.data.datasource.photo.local.PhotoLocalDataSourceImpl
import com.nahlasamir244.flickrsearchapp.data.datasource.photo.remote.PhotoRemoteDataSource
import com.nahlasamir244.flickrsearchapp.data.datasource.photo.remote.PhotoRemoteDataSourceImpl
import com.nahlasamir244.flickrsearchapp.data.db.database.FlickrSearchAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// here we add dependencies that we are using inside the whole application (app scope)
@Module
@InstallIn(ApplicationComponent::class)
class AppModule {
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @BaseUrl
    @Singleton
    fun provideBaseUrl() = "https://www.flickr.com/services/rest/"

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient, @BaseUrl baseUrl: String,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun providePhotoApiService(retrofit: Retrofit): PhotoApiService = retrofit.create(
        PhotoApiService::class.java
    )

    @Provides
    @DatabaseName
    @Singleton
    fun provideDatabaseName() = "Flickr_Search.db"

    @Provides
    @Singleton
    fun provideFlickrSearchAppDatabase(
        @ApplicationContext applicationContext: Context,
        @DatabaseName databaseName: String,
    )
            : FlickrSearchAppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            FlickrSearchAppDatabase::class.java,
            databaseName
        ).build()
    }

    @Provides
    fun providePhotoDao(flickrSearchAppDatabase: FlickrSearchAppDatabase) =
        flickrSearchAppDatabase.photoDao()

    @Provides
    fun providePhotoRemoteKeysDao(flickrSearchAppDatabase: FlickrSearchAppDatabase) =
        flickrSearchAppDatabase.photoRemoteKeysDao()


    @Provides
    @Singleton
    fun providePhotoLocalDataSource(photoLocalDataSourceImpl: PhotoLocalDataSourceImpl)
            : PhotoLocalDataSource = photoLocalDataSourceImpl

    @Provides
    @Singleton
    fun providePhotoRemoteDataSource(photoRemoteDataSourceImpl: PhotoRemoteDataSourceImpl)
            : PhotoRemoteDataSource = photoRemoteDataSourceImpl

}