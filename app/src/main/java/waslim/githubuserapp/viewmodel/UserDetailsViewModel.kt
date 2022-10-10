package waslim.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import waslim.githubuserapp.model.api.datadetail.UserDetailsResponse
import waslim.githubuserapp.repository.UserDetailsRepository
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(private val userDetailsRepository: UserDetailsRepository) : ViewModel(){
    private val _detailUser = MutableLiveData<UserDetailsResponse?>()
    val detailUser: LiveData<UserDetailsResponse?> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDataDetailsUser(username: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = false
                val dataDetailsUser = userDetailsRepository.getDetailsUser(username)
                _detailUser.value = dataDetailsUser
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message.toString()}")
            }
        }
    }

    companion object {
        private const val TAG = "ViewModelUserDetails"
    }
}