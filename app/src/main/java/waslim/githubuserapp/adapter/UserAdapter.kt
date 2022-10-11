package waslim.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import waslim.githubuserapp.R
import waslim.githubuserapp.databinding.ItemListUsersBinding
import waslim.githubuserapp.model.api.datasearch.UserItemResponse

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var onItemClickDetails: OnItemClickCallback
    private lateinit var onItemClickShareUserData: OnItemClickCallback
    private val listUsers = ArrayList<UserItemResponse>()

    fun setOnItemClickCallback(onItemClick: OnItemClickCallback) {
        onItemClick.also {
            onItemClickDetails = it
            onItemClickShareUserData = it
        }
    }

    fun setUserDataList(userItemResponses: ArrayList<UserItemResponse>) {
        listUsers.run {
            clear()
            addAll(userItemResponses)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUsers[position]
        holder.apply {
            itemView.apply {
                binding.apply {
                    Glide.with(context)
                        .load(user.avatarUrl)
                        .error(R.drawable.ic_baseline_error_24)
                        .into(ivAvatarsList)
                    tvUsernameList.text = user.login
                    tvLinkList.text = user.htmlUrl

                    btnShareUserData.setOnClickListener {
                        onItemClickShareUserData.onItemClickedShare(listUsers[adapterPosition])
                    }
                }

                setOnClickListener {
                    onItemClickDetails.onItemClickedMoveDetail(listUsers[adapterPosition])
                }
            }
        }
    }

    override fun getItemCount() = listUsers.size

    class ViewHolder (var binding: ItemListUsersBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClickedMoveDetail(dataUsers: UserItemResponse)
        fun onItemClickedShare(dataUsers: UserItemResponse)
    }

}