package com.example.events.ui.users

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.events.R
import com.example.events.data.entities.User
import com.example.events.databinding.ItemUserBinding

class UserAdapter(
    private val context: Context
): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    val items = mutableListOf<User>()

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                tvName.text = user.name
                tvEmail.text = context.getString(R.string.email_value, user.email)
                if(user.clas.isNullOrEmpty()){
                    tvClass.isVisible = false
                }else {
                    tvClass.isVisible = true
                    tvClass.text = context.getString(R.string.class_value, user.clas)
                }
                ivPhoto.setImageResource(
                    if(user.teacher){
                        R.drawable.ic_teacher
                    }else{
                        R.drawable.ic_pupil
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(users: List<User>){
        items.clear()
        items.addAll(users)
        notifyDataSetChanged()
    }

}