package waslim.githubuserapp.repository

import waslim.githubuserapp.model.local.Favorite
import waslim.githubuserapp.model.local.FavoriteDao
import javax.inject.Inject

class UserFavoriteRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    suspend fun insertFavorite(favorite: Favorite) {
        return favoriteDao.insertFavorite(favorite)
    }

    suspend fun getAllFavorite() : List<Favorite> {
        return favoriteDao.getALlFavorite()
    }

    suspend fun getFavoriteByUsername(username: String) : Favorite {
        return favoriteDao.getFavoriteByUsername(username)
    }

    suspend fun deleteFavorite(favorite: Favorite) {
        return favoriteDao.deleteFavorite(favorite)
    }

}