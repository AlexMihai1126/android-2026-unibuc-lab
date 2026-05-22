package cst.unibucfmiif2026.network

import com.google.firebase.auth.FirebaseAuth
import cst.unibucfmiif2026.BuildConfig
import cst.unibucfmiif2026.data.AuthDataStore
import cst.unibucfmiif2026.network.api.AuthApiService
import cst.unibucfmiif2026.network.api.UsersApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://reqres.in/"
    private const val LOCAL_BASE_URL = "http://10.0.2.2:3000"
    private val apiKeyInterceptor = Interceptor { chain ->
        val request = chain
            .request()
            .newBuilder()
            .addHeader("x-api-key", BuildConfig.API_KEY)
            .build()
        chain.proceed(request)
    }

    private val authTokenInterceptor = Interceptor { chain ->
        val token = runBlocking {
            AuthDataStore.tokenFlow.first()
        } ?: ""
        val request = chain
            .request()
            .newBuilder()
            .addHeader("Authorization", token)
            .build()
        chain.proceed(request)
    }

    private val firebaseAuthTokenInterceptor = Interceptor { chain ->
        val token = runBlocking {
            FirebaseAuth.getInstance()
                .currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
        }

        val requestBuilder = chain
            .request()
            .newBuilder()

        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val okHttpClientAuthorized = OkHttpClient
        .Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(firebaseAuthTokenInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val usersApi : UsersApiService by lazy {
        Retrofit.Builder()
            .baseUrl(LOCAL_BASE_URL)
            .client(okHttpClientAuthorized)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsersApiService::class.java)
    }

    val usersLocalApi : UsersApiService by lazy {
        Retrofit.Builder()
            .baseUrl(LOCAL_BASE_URL)
            .client(okHttpClientAuthorized)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsersApiService::class.java)
    }

    val authApi : AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}