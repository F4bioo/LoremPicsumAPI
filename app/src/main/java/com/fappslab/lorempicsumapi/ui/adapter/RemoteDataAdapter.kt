package com.fappslab.lorempicsumapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.databinding.AdapterItemBinding
import com.fappslab.lorempicsumapi.utils.extensions.set

class RemoteDataAdapter(
    private val onClickListener: (view: View, photo: Photo, position: Int) -> Unit
) : PagingDataAdapter<Photo, RemoteDataAdapter.ViewHolder>(RemoteDataAdapter) {

    private val differ = AsyncListDiffer(this, RemoteDataAdapter)

    private companion object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val biding = AdapterItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(biding, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = getItem(position)
        photo?.let { holder.viewBiding(it) }
    }

    inner class ViewHolder(
        private val biding: AdapterItemBinding,
        private val onClickListener: (view: View, photo: Photo, position: Int) -> Unit
    ) : RecyclerView.ViewHolder(biding.root) {

        fun viewBiding(photo: Photo) {
            biding.apply {
                imagePhoto.set(photo.downloadUrl)
                textAuthor.text = photo.author
                textId.text = String.format("#%s", photo.id)
                checkFavorite.isChecked = photo.favorite

                itemView.setOnClickListener {
                    it.postDelayed({
                        onClickListener(it, photo, layoutPosition)
                    }, 300)
                }

                checkFavorite.setOnClickListener {
                    photo.favorite = checkFavorite.isChecked
                    onClickListener(it, photo, layoutPosition)
                }
            }
        }
    }
}
