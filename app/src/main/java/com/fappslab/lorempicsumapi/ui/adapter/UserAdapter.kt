package com.fappslab.lorempicsumapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.databinding.AdapterItemBinding
import com.fappslab.lorempicsumapi.utils.extensions.set

class UserAdapter(
    private val onClickListener: (view: View, photo: Photo) -> Unit
) : PagingDataAdapter<Photo, UserAdapter.UserViewHolder>(UserAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val biding = AdapterItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(biding, onClickListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { holder.viewBiding(it) }
    }

    inner class UserViewHolder(
        private val biding: AdapterItemBinding,
        private val onClickListener: (view: View, photo: Photo) -> Unit
    ) : RecyclerView.ViewHolder(biding.root) {

        fun viewBiding(photo: Photo) {
            biding.apply {
                imagePhoto.set(photo.downloadUrl)
                textAuthor.text = photo.author
                textId.text = String.format("#%s", photo.id)

                cardRoot.setOnClickListener {
                    onClickListener(it, photo)
                }
            }
        }
    }

    private companion object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem == newItem
    }
}
