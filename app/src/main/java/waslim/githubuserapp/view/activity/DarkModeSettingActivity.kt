package waslim.githubuserapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.AndroidEntryPoint
import waslim.githubuserapp.R
import waslim.githubuserapp.databinding.ActivityDarkModeSettingBinding
import waslim.githubuserapp.viewmodel.DarkModeSettingViewModel

@AndroidEntryPoint
class DarkModeSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDarkModeSettingBinding
    private val darkModeSettingViewModel by viewModels<DarkModeSettingViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDarkModeSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.title = getString(R.string.setting)

        setThemeSetting()
        getThemeSetting()
    }


    private fun setThemeSetting() {
        binding.apply {
            switchModeDark.setOnCheckedChangeListener { _, isChecked ->
                switchModeDark.isChecked = isChecked
                darkModeSettingViewModel.setThemeSetting(isChecked)
            }
        }
    }


    private fun getThemeSetting() {
        binding.apply {
            darkModeSettingViewModel.getThemeSetting().observe(this@DarkModeSettingActivity) {
                if (it) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchModeDark.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchModeDark.isChecked = false
                }
            }


        }
    }

}