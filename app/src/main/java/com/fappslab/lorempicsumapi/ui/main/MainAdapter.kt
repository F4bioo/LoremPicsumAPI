package com.fappslab.lorempicsumapi.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.databinding.AdapterItemViewBinding
import com.fappslab.lorempicsumapi.utils.extensions.set


class MainAdapter(
    private val onClickListener: (view: View, photo: Photo) -> Unit
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
        //println("<> AAA id: ${photo.id} - s2: ${photo.favorite}")
        holder.viewBiding(photo)

    }

    override fun getItemCount() = photos.size

    inner class ViewHolder(
        private val biding: AdapterItemViewBinding,
        private val onClickListener: (view: View, photo: Photo) -> Unit
    ) : RecyclerView.ViewHolder(biding.root) {

        fun viewBiding(photo: Photo) {
            biding.apply {
                imagePhoto.set(photo.downloadUrl)
                textAuthor.text = photo.author
                checkFavorite.isChecked = photo.favorite

                cardRoot.setOnClickListener {
                    onClickListener(cardRoot, photo)
                }

                checkFavorite.setOnCheckedChangeListener { buttonView, isChecked ->
                    photo.favorite = isChecked
                    onClickListener(buttonView, photo)
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
}
