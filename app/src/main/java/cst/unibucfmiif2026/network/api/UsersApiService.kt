package cst.unibucfmiif2026.network.api

import cst.unibucfmiif2026.network.dto.UsersResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UsersApiService {
    @GET("api/users")
    suspend fun getUsers(
        @Query("page") page: Int
    ) : UsersResponse

    @POST("api/upload-avatar")
    suspend fun addUser(
        @Part("firstname") firstname: String,
        @Part("lastname") lastname: String,
        @Part("email") email: String,
        @Part("avatar") avatar : MultipartBody.Part
    ) : Response<Unit>
}