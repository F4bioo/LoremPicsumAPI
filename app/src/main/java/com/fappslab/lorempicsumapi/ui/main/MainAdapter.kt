package com.fappslab.lorempicsumapi.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.databinding.AdapterItemViewBinding
import com.fappslab.lorempicsumapi.utils.extensions.set


class MainAdapter(
    private val onClickListener: (photo: Photo) -> Unit
) : ListAdapter<Photo, MainAdapter.ViewHolder>(MainAdapter) {

    private val photos = arrayListOf<Photo>()
    private val differ = AsyncListDiffer(this, MainAdapter)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val biding = AdapterItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(biding, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photos[position]
        holder.viewBiding(photo)

        println("<> ${differ.currentList.size}")
    }

    override fun getItemCount() = photos.size

    inner class ViewHolder(
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

    override fun submitList(list: MutableList<Photo>?) {
        super.submitList(list?.let {
            photos.addAll(it)
            differ.submitList(photos)
            it
        })
    }

    private companion object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    private fun ImageView.set(@DrawableRes icon: Int, color: Int) {
        setImageResource(icon)
        setColorFilter(color)
    }
}
