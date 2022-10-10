package waslim.githubuserapp.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import waslim.githubuserapp.R
import waslim.githubuserapp.adapter.UserAdapter
import waslim.githubuserapp.databinding.ActivityMainBinding
import waslim.githubuserapp.model.api.datasearch.UserItemResponse
import waslim.githubuserapp.viewmodel.MainViewModel

@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExit = false
    private var username: String? = null
    private val mainViewModel by viewModels<MainViewModel>()
    private val adapterList: UserAdapter by lazy {
        UserAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doubleBackExit()
        checkConnection()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.cari_user)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                mainViewModel.searchUserByUsername(query)
                username = query
                mainViewModel.searchUser.observe(this@MainActivity) {
                    when {
                        it.isNullOrEmpty() -> {
                            showLoading(false)
                            binding.apply {
                                rvUser.visibility = View.GONE
                                noData.visibility = View.VISIBLE
                            }
                            closedKeyboard()
                        }
                        else -> {
                            setRecyclerList()
                            adapterList.setUserDataList(it)
                            showLoading(false)
                            closedKeyboard()
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                startActivity(Intent(this, UserFavoriteActivity::class.java))
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


    private fun showLoading(isLoading: Boolean) = mainViewModel.isLoading.observe(this) {
        binding.progressBarMain.visibility = when {
            isLoading -> View.VISIBLE
            else -> View.GONE
        }
    }


    private fun checkConnection() = when {
        isOnline(applicationContext) -> setUserData()
        else -> Toast.makeText(applicationContext, getString(R.string.no_internet), Toast.LENGTH_LONG).show()
    }


    private fun setUserData() = when {
        username != null -> {
            showLoading(true)
            mainViewModel.searchUser.observe(this) {
                when {
                    it != null -> {
                        showLoading(false)
                        setRecyclerList()
                        adapterList.setUserDataList(it)
                    }
                    else -> {
                        showLoading(false)
                        Toast.makeText(applicationContext, getString(R.string.no_data), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            mainViewModel.searchUserByUsername(username.toString())
        }
        else -> {
            showLoading(true)
            mainViewModel.userData.observe(this) {
                when {
                    it != null -> {
                        showLoading(false)
                        setRecyclerList()
                        adapterList.setUserDataList(it)
                    }
                    else -> {
                        showLoading(false)
                        Toast.makeText(applicationContext, getString(R.string.no_data), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            mainViewModel.getUsers()
        }
    }


    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return when {
            capabilities != null -> {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        true
                    }
                    else -> capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                }
            }
            else -> false
        }
    }


    private fun setRecyclerList() {
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(applicationContext)
            rvUser.adapter = adapterList
            noData.visibility = View.GONE
            rvUser.visibility = View.VISIBLE
            rvUser.setHasFixedSize(true)
        }

        adapterList.run {
            setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
                override fun onItemClickedMoveDetail(dataUsers: UserItemResponse) {
                    moveWhiteGithubUserData(dataUsers)
                    closedKeyboard()
                }

                override fun onItemClickedShare(dataUsers: UserItemResponse) {
                    shareUserData(dataUsers)
                    closedKeyboard()
                }
            })
        }
    }


    private fun moveWhiteGithubUserData(dataUsers: UserItemResponse) {
        Intent(applicationContext, UserDetailsActivity::class.java).apply {
            putExtra(UserDetailsActivity.DATA_USER, dataUsers)
            startActivity(this)
        }
    }


    private fun shareUserData(dataUsers: UserItemResponse) {
        val shareUserData = "Username: ${dataUsers.login}\n" +
                "Link Github: ${dataUsers.htmlUrl}"
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


    private fun closedKeyboard() {
        val view: View? = currentFocus
        val inputMethodManager: InputMethodManager
        when {
            view != null -> {
                inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }


    private fun doubleBackExit() = onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            when {
                doubleBackToExit -> {
                    finish()
                }
                else -> {
                    doubleBackToExit = true
                    Toast.makeText(applicationContext, getString(R.string.double_click), Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        kotlin.run {
                            doubleBackToExit = false
                        }
                    }, DURATION)
                }
            }
        }
    })


    companion object {
        private const val DURATION = 2000L
    }

}