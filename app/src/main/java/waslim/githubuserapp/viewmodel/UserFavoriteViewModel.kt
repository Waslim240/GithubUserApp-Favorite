package waslim.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import waslim.githubuserapp.model.local.Favorite
import waslim.githubuserapp.repository.UserFavoriteRepository
import javax.inject.Inject

@HiltViewModel
class UserFavoriteViewModel @Inject constructor(private val userFavoriteRepository: UserFavoriteRepository) : ViewModel() {

    private val _favoriteData = MutableLiveData<List<Favorite>?>()
    val favoriteData: LiveData<List<Favorite>?> = _favoriteData

    private val _favoriteDataByUsername = MutableLiveData<Favorite?>()
    val favoriteDataByUsername: LiveData<Favorite?> = _favoriteDataByUsername

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getAllFavorite() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val dataFavorite = userFavoriteRepository.getAllFavorite()
                _favoriteData.value = dataFavorite
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message.toString()}")
            }

        }
    }


    fun getFavoriteByUsername(username: String) {
        viewModelScope.launch {
            val dataFavoriteByUsername = userFavoriteRepository.getFavoriteByUsername(username)
            _favoriteDataByUsername.value = dataFavoriteByUsername
        }
    }


    fun insertFavorite(favorite: Favorite) {
        viewModelScope.launch {
            userFavoriteRepository.insertFavorite(favorite)
        }
    }


    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            userFavoriteRepository.deleteFavorite(favorite)
        }
    }


    companion object {
        private const val TAG = "ViewModelFavorite"
    }

}