package waslim.githubuserapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import waslim.githubuserapp.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        this.title = getString(R.string.about)
    }
}