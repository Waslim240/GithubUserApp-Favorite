package waslim.githubuserapp.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import waslim.githubuserapp.BuildConfig
import waslim.githubuserapp.model.datastore.DarkModeSettingPreferences
import waslim.githubuserapp.model.local.FavoriteDao
import waslim.githubuserapp.model.local.FavoriteDatabase
import waslim.githubuserapp.network.ApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val URL = BuildConfig.BASE_URL

    private val loggingInterceptor = when {
        BuildConfig.DEBUG -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        else -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .build()


    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


    @Singleton
    @Provides
    fun provideGithubUserApi(retrofit: Retrofit) : ApiService =
        retrofit.create(ApiService::class.java)


    @Singleton
    @Provides
    fun providesFavoriteDatabase(context: Application) : FavoriteDatabase =
        FavoriteDatabase.getDatabase(context)


    @Singleton
    @Provides
    fun providesFavoriteDao(favoriteDatabase: FavoriteDatabase) : FavoriteDao =
        favoriteDatabase.favoriteDao()


    @Singleton
    @Provides
    fun providesDarkMode(@ApplicationContext context: Context) : DarkModeSettingPreferences =
        DarkModeSettingPreferences(context)

}