package waslim.githubuserapp.view.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.AndroidEntryPoint
import waslim.githubuserapp.R
import waslim.githubuserapp.viewmodel.DarkModeSettingViewModel

@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val darkModeSettingViewModel by viewModels<DarkModeSettingViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        checkDarkMode()

    }

    private fun checkDarkMode() {
        darkModeSettingViewModel.getThemeSetting().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                splashHandler()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                splashHandler()
            }
        }
    }


    private fun splashHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, DELAY)
    }


    companion object {
        private const val DELAY = 3000L
    }
}