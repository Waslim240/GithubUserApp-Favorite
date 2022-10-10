package waslim.githubuserapp.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import waslim.githubuserapp.R
import waslim.githubuserapp.adapter.SectionsPagerFollow
import waslim.githubuserapp.databinding.ActivityUserDetailsBinding
import waslim.githubuserapp.model.api.datasearch.UserItemResponse
import waslim.githubuserapp.model.local.Favorite
import waslim.githubuserapp.viewmodel.UserDetailsViewModel
import waslim.githubuserapp.viewmodel.UserFavoriteViewModel

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserDetailsBinding
    private val userDetailsViewModel by viewModels<UserDetailsViewModel>()
    private val userFavoriteViewModel by viewModels<UserFavoriteViewModel>()
    private var checkUsername = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.title = getString(R.string.user_details)

        val dataUser = intent?.getParcelableExtra<UserItemResponse>(DATA_USER)
        val dataUserFavorite = intent?.getParcelableExtra<Favorite>(DATA_USER_FAVORITE)

        when {
            dataUser?.login != null -> {
                setDataDetailsUser(dataUser.login)
                checkUsername(dataUser.login)
                insertDeleteFavorite(dataUser.id, dataUser.login, dataUser.htmlUrl, dataUser.avatarUrl)
            }
            dataUserFavorite?.login != null -> {
                setDataDetailsUser(dataUserFavorite.login)
                checkUsername(dataUserFavorite.login)
                insertDeleteFavorite(dataUserFavorite.id, dataUserFavorite.login, dataUserFavorite.url, dataUserFavorite.avatars_url)
            }
        }

        showLoading(true)
        showSectionPager()

    }


    private fun setDataDetailsUser(username: String) {
        userDetailsViewModel.detailUser.observe(this){ UserDetailsResponse ->
            binding.apply {
                when {
                    UserDetailsResponse != null -> {
                        if (UserDetailsResponse.name.isNullOrEmpty()) tvNameDetails.text = getString(R.string.no_dataList) else tvNameDetails.text = UserDetailsResponse.name
                        if (UserDetailsResponse.location.isNullOrEmpty()) tvLocationDetail.text = getString(R.string.no_dataList) else tvLocationDetail.text = UserDetailsResponse.location
                        if (UserDetailsResponse.login.isNullOrEmpty()) tvUsernameDetail.text = getString(R.string.no_dataList) else tvUsernameDetail.text = UserDetailsResponse.login
                        if (UserDetailsResponse.company.isNullOrEmpty()) tvCompanyDetail.text = getString(R.string.no_dataList) else tvCompanyDetail.text = UserDetailsResponse.company
                        if (UserDetailsResponse.htmlUrl.isNullOrEmpty()) tvLinkDetail.text = getString(R.string.no_dataList) else tvLinkDetail.text = UserDetailsResponse.htmlUrl
                        if (UserDetailsResponse.email.isNullOrEmpty()) tvEmailDetail.text = getString(R.string.no_dataList) else tvEmailDetail.text = UserDetailsResponse.email
                        Glide.with(applicationContext)
                            .load(UserDetailsResponse.avatarUrl)
                            .error(R.drawable.ic_baseline_error_24)
                            .into(ivAvatarsUserDetail)
                        setVisibleIcon()
                    }
                }
            }
            showLoading(false)
        }
        userDetailsViewModel.getDataDetailsUser(username)
    }



    private fun checkUsername(username: String) {
        userFavoriteViewModel.favoriteDataByUsername.observe(this) {
            if (it?.login == username) {
                checkUsername = true
            }
        }
        userFavoriteViewModel.getFavoriteByUsername(username)
    }


    private fun insertDeleteFavorite(id: Int?, username: String?, htmlUrl: String?, avatarUrl: String?) {
        binding.fabFavorite.setOnClickListener {
            when {
                !checkUsername -> {
                    userFavoriteViewModel.insertFavorite(
                        Favorite(
                            id,
                            username,
                            htmlUrl,
                            avatarUrl
                        )
                    )
                    Toast.makeText(this, "$username " +getString(R.string.add_favorite) , Toast.LENGTH_LONG).show()
                    checkUsername = true

                    val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setContentTitle(getString(R.string.user_favorite))
                        .setContentText("$username " +getString(R.string.add_favorite))
                        .setSubText(getString(R.string.favorite))
                        .setAutoCancel(true)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                        channel.description = CHANNEL_NAME
                        mBuilder.setChannelId(CHANNEL_ID)
                        mNotificationManager.createNotificationChannel(channel)
                    }

                    val notification = mBuilder.build()
                    mNotificationManager.notify(NOTIFICATION_ID, notification)
                }
                else -> {
                    userFavoriteViewModel.deleteFavorite(
                        Favorite(
                            id,
                            username,
                            htmlUrl,
                            avatarUrl
                        )
                    )
                    Toast.makeText(this, "$username " +getString(R.string.delete_favorite), Toast.LENGTH_LONG).show()
                    checkUsername = false

                    val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setContentTitle(getString(R.string.user_favorite))
                        .setContentText("$username " +getString(R.string.delete_favorite))
                        .setSubText(getString(R.string.favorite))
                        .setAutoCancel(true)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                        channel.description = CHANNEL_NAME
                        mBuilder.setChannelId(CHANNEL_ID)
                        mNotificationManager.createNotificationChannel(channel)
                    }

                    val notification = mBuilder.build()
                    mNotificationManager.notify(NOTIFICATION_ID, notification)
                }
            }
        }
    }


    private fun setVisibleIcon() {
        binding.apply {
            imageView1.visibility = View.VISIBLE
            imageView2.visibility = View.VISIBLE
            imageView3.visibility = View.VISIBLE
            imageView4.visibility = View.VISIBLE
            imageView5.visibility = View.VISIBLE
        }
    }


    private fun showLoading(isLoading: Boolean) = userDetailsViewModel.isLoading.observe(this){
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun showSectionPager() {
        binding.apply {
            val sectionPager = SectionsPagerFollow(this@UserDetailsActivity)
            val viewPager: ViewPager2 = viewPager
            viewPager.adapter = sectionPager
            val tabs: TabLayout = tabs
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            supportActionBar?.elevation = 0f
        }
    }


    companion object {
        const val DATA_USER = "data_user"
        const val DATA_USER_FAVORITE = "data_user_favorite"

        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "favorite channel"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}