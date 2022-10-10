package waslim.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import waslim.githubuserapp.model.api.datafollow.UserFollowResponse
import waslim.githubuserapp.repository.UserFollowingRepository
import javax.inject.Inject

@HiltViewModel
class UserFollowingViewModel @Inject constructor(private val userFollowingRepository: UserFollowingRepository) : ViewModel() {
    private val _dataFollowing = MutableLiveData<ArrayList<UserFollowResponse>?>()
    val dataFollowing: LiveData<ArrayList<UserFollowResponse>?> = _dataFollowing

    private val _isLoading = MutableLiveData<Boolean> ()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowing(username: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = false
                val dataFollowing = userFollowingRepository.getFollowing(username, TYPE)
                _dataFollowing.value = dataFollowing
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message.toString()}")
            }
        }
    }

    companion object {
        private const val TAG = "ViewModelUserFollowing"
        private const val TYPE = "following"
    }
}