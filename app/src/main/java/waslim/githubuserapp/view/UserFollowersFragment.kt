package waslim.githubuserapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import waslim.githubuserapp.adapter.UserFollowersAdapter
import waslim.githubuserapp.databinding.FragmentUserFollowersBinding
import waslim.githubuserapp.model.api.datafollow.UserFollowResponse
import waslim.githubuserapp.model.api.datasearch.UserItemResponse
import waslim.githubuserapp.model.local.Favorite
import waslim.githubuserapp.viewmodel.UserFollowersViewModel

@AndroidEntryPoint
class UserFollowersFragment : Fragment() {
    private var _binding: FragmentUserFollowersBinding? = null
    private val binding get() = _binding!!
    private val userFollowersViewModel by viewModels<UserFollowersViewModel>()
    private val userFollowersAdapter: UserFollowersAdapter by lazy(::UserFollowersAdapter)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataUser = activity?.intent?.getParcelableExtra<UserItemResponse>(DATA_USER)
        val dataUserFavorite = activity?.intent?.getParcelableExtra<Favorite>(DATA_USER_FAVORITE)

        when {
            dataUser?.login != null -> showRecyclerList(dataUser.login)
            dataUserFavorite?.login != null -> showRecyclerList(dataUserFavorite.login)
        }
    }

    private fun showRecyclerList(username: String) {
        showLoading(true)
        userFollowersViewModel.dataFollowers.observe(viewLifecycleOwner) {
            when {
                it != null -> {
                    userFollowersAdapter.setUserDataFollowers(it)
                    setRecyclerList()
                    showLoading(false)
                }
            }
        }
        userFollowersViewModel.getFollowers(username)
    }

    private fun setRecyclerList() {
        binding.apply {
            rvFollowers.layoutManager = LinearLayoutManager(requireContext())
            rvFollowers.adapter = userFollowersAdapter
            rvFollowers.setHasFixedSize(true)
        }

        userFollowersAdapter.setOnItemClickCallback(object : UserFollowersAdapter.OnItemClickCallback{
            override fun onItemClickedShare(dataUsers: UserFollowResponse) {
                shareUserData(dataUsers)
            }
        })
    }

    private fun shareUserData(dataUsers: UserFollowResponse) {
        val shareUserData = "Username: ${dataUsers.login}\n" +
                "Link Github: ${dataUsers.htmlUrl}"
        val share: Intent = Intent()
            .apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareUserData)
                type = "text/plain"
            }
        Intent.createChooser(share, "Share Data Using").apply {
            startActivity(this)
        }
    }

    private fun showLoading(isLoading: Boolean) = userFollowersViewModel.isLoading.observe(viewLifecycleOwner){
        binding.progressBarFollowers.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val DATA_USER = "data_user"
        const val DATA_USER_FAVORITE = "data_user_favorite"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}