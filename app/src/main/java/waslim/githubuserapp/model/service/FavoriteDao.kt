package waslim.githubuserapp.model.service

import androidx.room.*
import waslim.githubuserapp.model.local.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM Favorite WHERE login = :login")
    suspend fun getFavoriteByUsername(login: String) : Favorite

    @Query("SELECT * FROM Favorite ORDER BY login ASC")
    suspend fun getALlFavorite() : List<Favorite>

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

}