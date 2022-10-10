package waslim.githubuserapp.repository

import waslim.githubuserapp.model.api.datafollow.UserFollowResponse
import waslim.githubuserapp.network.ApiService
import javax.inject.Inject

class UserFollowerRepository @Inject constructor(private val apiService: ApiService){
    suspend fun getFollowers(username: String, type: String) : ArrayList<UserFollowResponse> =
        apiService.getFollow(username, type)
}