package waslim.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import waslim.githubuserapp.model.api.datasearch.UserItemResponse
import waslim.githubuserapp.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _searchUser = MutableLiveData<ArrayList<UserItemResponse>?>()
    val searchUser: LiveData<ArrayList<UserItemResponse>?> = _searchUser

    private val _userData = MutableLiveData<ArrayList<UserItemResponse>?>()
    val userData: LiveData<ArrayList<UserItemResponse>?> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUsers(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = false
                val dataUser = mainRepository.getUser()
                _userData.value = dataUser
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message.toString()}")
            }

        }
    }

    fun searchUserByUsername(username: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = false
                val data = mainRepository.searchUserByUsername(username)
                _searchUser.value = data.userItemResponses
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message.toString()}")
            }
        }
    }

    companion object {
        private const val TAG = "ViewModelUser"
    }

}