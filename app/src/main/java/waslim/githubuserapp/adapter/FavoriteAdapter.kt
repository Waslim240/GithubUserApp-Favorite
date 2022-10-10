package waslim.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import waslim.githubuserapp.R
import waslim.githubuserapp.databinding.ItemListUsersBinding
import waslim.githubuserapp.model.local.Favorite

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private lateinit var onItemClickDetails: OnItemClickCallback
    private lateinit var onItemClickShareUserData: OnItemClickCallback

    private var listFavorite: List<Favorite>? = null

    fun setOnItemClickCallback(onItemClick: OnItemClickCallback) {
        this.onItemClickDetails = onItemClick
        this.onItemClickShareUserData = onItemClick
    }

    fun setUserDataList(favorite: List<Favorite>) {
        this.listFavorite = favorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listFavorite?.get(position)
        holder.apply {
            binding.apply {
                Glide.with(itemView.context)
                    .load(user?.avatars_url)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(ivAvatarsList)
                tvUsernameList.text = user?.login
                tvLinkList.text = user?.url

                btnShareUserData.setOnClickListener {
                    onItemClickShareUserData.onItemClickedShare(listFavorite!![adapterPosition])
                }
            }

            itemView.setOnClickListener {
                onItemClickDetails.onItemClickedMoveDetail(listFavorite!![adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return when (listFavorite) {
            null -> 0
            else -> listFavorite!!.size
        }
    }

    class ViewHolder (var binding: ItemListUsersBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClickedMoveDetail(dataUsers: Favorite)
        fun onItemClickedShare(dataUsers: Favorite)
    }

}