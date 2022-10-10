package waslim.githubuserapp.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import waslim.githubuserapp.BuildConfig
import waslim.githubuserapp.model.api.datasearch.UserItemResponse
import waslim.githubuserapp.model.api.datadetail.UserDetailsResponse
import waslim.githubuserapp.model.api.datafollow.UserFollowResponse
import waslim.githubuserapp.model.api.datasearch.UserSearchResponse

interface ApiService {

    @GET("users")
    @Headers("Authorization: token $MY_TOKEN")
    suspend fun getUser(): ArrayList<UserItemResponse>

    @GET("search/users")
    @Headers("Authorization: token $MY_TOKEN")
    suspend fun searchUsersByUsername(
        @Query("q") query: String
    ): UserSearchResponse

    @GET("users/{username}")
    @Headers("Authorization: token $MY_TOKEN")
    suspend fun getDetailsUser(
        @Path("username") username: String
    ): UserDetailsResponse

    @GET("users/{username}/{type}")
    @Headers("Authorization: token $MY_TOKEN")
    suspend fun getFollow(
        @Path("username") username: String,
        @Path("type") type: String
    ) : ArrayList<UserFollowResponse>

    companion object {
        private const val MY_TOKEN = BuildConfig.API_TOKEN
    }
}