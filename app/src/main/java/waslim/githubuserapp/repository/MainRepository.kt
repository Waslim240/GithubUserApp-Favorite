package waslim.githubuserapp.repository

import waslim.githubuserapp.model.api.datasearch.UserItemResponse
import waslim.githubuserapp.model.api.datasearch.UserSearchResponse
import waslim.githubuserapp.network.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService){
    suspend fun getUser() : ArrayList<UserItemResponse>{
        return apiService.getUser()
    }

    suspend fun searchUserByUsername(username: String) : UserSearchResponse {
        return apiService.searchUsersByUsername(username)
    }

}