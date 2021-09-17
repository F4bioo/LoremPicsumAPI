package com.fappslab.lorempicsumapi.ui.main

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.databinding.AdapterItemViewBinding
import com.fappslab.lorempicsumapi.utils.extensions.set

class UserAdapter : PagedListAdapter<Photo, UserAdapter.UserViewHolder>(USER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class UserViewHolder(
        private val biding: AdapterItemViewBinding,
        private val onClickListener: (photo: Photo) -> Unit
    ) : RecyclerView.ViewHolder(biding.root) {

        fun viewBiding(photo: Photo) {
            biding.apply {
                imagePhoto.set(photo.downloadUrl)
                textAuthor.text = photo.author
                textId.text = String.format("#%s", photo.id)

                cardRoot.setOnClickListener {
                    onClickListener(photo)
                }
            }
        }
    }
}

val USER_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
        // User ID serves as unique ID
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
        // Compare full contents (note: Java users should call .equals())
        oldItem == newItem
}
