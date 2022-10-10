package waslim.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import waslim.githubuserapp.model.api.datafollow.UserFollowResponse
import waslim.githubuserapp.repository.UserFollowerRepository
import javax.inject.Inject

@HiltViewModel
class UserFollowersViewModel @Inject constructor(private val userFollowerRepository: UserFollowerRepository) : ViewModel() {
    private val _dataFollowers = MutableLiveData<ArrayList<UserFollowResponse>?>()
    val dataFollowers: LiveData<ArrayList<UserFollowResponse>?> = _dataFollowers

    private val _isLoading = MutableLiveData<Boolean> ()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowers(username: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = false
                val dataFollowers = userFollowerRepository.getFollowers(username, TYPE)
                _dataFollowers.value = dataFollowers
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message.toString()}")
            }
        }
    }

    companion object {
        private const val TAG = "ViewModelUserFollowers"
        private const val TYPE = "followers"
    }
}