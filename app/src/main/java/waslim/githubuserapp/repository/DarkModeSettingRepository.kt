package waslim.githubuserapp.repository

import kotlinx.coroutines.flow.Flow
import waslim.githubuserapp.model.datastore.DarkModeSettingPreferences
import javax.inject.Inject

class DarkModeSettingRepository @Inject constructor(private val darkModeSettingPreferences: DarkModeSettingPreferences) {
    suspend fun setThemeSetting(isDarkMode: Boolean) =
        darkModeSettingPreferences.setThemeSetting(isDarkMode)

    fun getThemeSetting() : Flow<Boolean> = darkModeSettingPreferences.getThemeSetting()
}