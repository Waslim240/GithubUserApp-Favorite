package waslim.githubuserapp.repository

import waslim.githubuserapp.model.api.datadetail.UserDetailsResponse
import waslim.githubuserapp.network.ApiService
import javax.inject.Inject

class UserDetailsRepository @Inject constructor(private val apiService: ApiService){
    suspend fun getDetailsUser(username: String) : UserDetailsResponse =
        apiService.getDetailsUser(username)
}