package waslim.githubuserapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import waslim.githubuserapp.R
import waslim.githubuserapp.adapter.FavoriteAdapter
import waslim.githubuserapp.databinding.ActivityFavoriteBinding
import waslim.githubuserapp.model.local.Favorite
import waslim.githubuserapp.viewmodel.UserFavoriteViewModel

@AndroidEntryPoint
class UserFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val userFavoriteViewModel by viewModels<UserFavoriteViewModel>()
    private val adapterFavorite: FavoriteAdapter by lazy {
        FavoriteAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.title = getString(R.string.favorite)

        showListFavorite()


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                Toast.makeText(applicationContext, getString(R.string.in_favorite), Toast.LENGTH_SHORT).show()
                true
            }

            R.id.setting -> {
                startActivity(Intent(this, DarkModeSettingActivity::class.java))
                true
            }

            R.id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }

            else -> true
        }
    }


    private fun showListFavorite () {
        showLoading(true)
        userFavoriteViewModel.favoriteData.observe(this) {
            if (it.isNullOrEmpty()) {
                showLoading(false)
                binding.apply {
                    noDataFavorite.visibility = View.VISIBLE
                    rvUserFavorite.visibility = View.GONE
                }
            } else {
                showLoading(false)
                setRecyclerList()
                adapterFavorite.setUserDataList(it)
                adapterFavorite.notifyDataSetChanged()
            }
        }
        userFavoriteViewModel.getAllFavorite()
    }


    private fun showLoading(isLoading: Boolean) = userFavoriteViewModel.isLoading.observe(this) {
        binding.progressBarFavorite.visibility = when {
            isLoading -> View.VISIBLE
            else -> View.GONE
        }
    }


    private fun setRecyclerList () {
        binding.apply {
            rvUserFavorite.visibility = View.VISIBLE
            noDataFavorite.visibility = View.INVISIBLE
            rvUserFavorite.layoutManager = LinearLayoutManager(applicationContext)
            rvUserFavorite.adapter = adapterFavorite
            rvUserFavorite.setHasFixedSize(true)
        }

        adapterFavorite.run {
            setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
                override fun onItemClickedMoveDetail(dataUsers: Favorite) {
                    moveWhiteGithubUserData(dataUsers)
                }

                override fun onItemClickedShare(dataUsers: Favorite) {
                    shareUserData(dataUsers)
                }
            })
        }
    }


    private fun moveWhiteGithubUserData(dataUsers: Favorite) {
        Intent(applicationContext, UserDetailsActivity::class.java).apply {
            putExtra(UserDetailsActivity.DATA_USER_FAVORITE, dataUsers)
            startActivity(this)
        }
    }


    private fun shareUserData(dataUsers: Favorite) {
        val shareUserData = "Username: ${dataUsers.login}"
        val share: Intent = Intent()
            .apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareUserData)
                type = "text/plain"
            }
        Intent.createChooser(share, getString(R.string.share_to)).apply {
            startActivity(this)
        }
    }

    override fun onResume() {
        super.onResume()
        showListFavorite()
    }

}

