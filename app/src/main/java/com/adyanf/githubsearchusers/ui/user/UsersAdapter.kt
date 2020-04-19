package com.adyanf.githubsearchusers.ui.user

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adyanf.githubsearchusers.R
import com.adyanf.githubsearchusers.ui.extensions.inflate
import com.adyanf.githubsearchusers.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_user.view.*

class UsersAdapter(private var users: MutableList<User>) : RecyclerView.Adapter<UsersAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_user))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun updateUsers(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    fun insertUsers(users: List<User>, startPosition: Int, itemCount: Int) {
        this.users.addAll(users)
        notifyItemRangeInserted(startPosition, itemCount)
    }

    fun clearUsers() {
        this.users.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private lateinit var user: User

        fun bind(user: User) {
            this.user = user
            itemView.login.text = user.login
            Glide.with(itemView).load(user.avatarUrl).into(itemView.avatar)

            animateView()
        }

        private fun animateView() {
            itemView.alpha = 0f
            val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f).apply {
                duration = 500L
            }
            AnimatorSet().apply {
                play(animator)
                start()
            }
        }
    }
}