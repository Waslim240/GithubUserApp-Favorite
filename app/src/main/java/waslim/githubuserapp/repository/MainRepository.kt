package waslim.githubuserapp.repository

import waslim.githubuserapp.model.api.datasearch.UserItemResponse
import waslim.githubuserapp.model.api.datasearch.UserSearchResponse
import waslim.githubuserapp.model.service.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService){
    suspend fun getUser() : ArrayList<UserItemResponse> = apiService.getUser()

    suspend fun searchUserByUsername(username: String) : UserSearchResponse =
        apiService.searchUsersByUsername(username)

}