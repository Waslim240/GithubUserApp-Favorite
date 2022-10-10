package waslim.githubuserapp.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "avatars_url") val avatars_url: String?
): Parcelable