package com.fappslab.lorempicsumapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.usecase.GetFavorite
import com.fappslab.lorempicsumapi.databinding.AdapterItemBinding
import com.fappslab.lorempicsumapi.utils.extensions.bg
import com.fappslab.lorempicsumapi.utils.extensions.set
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RemoteAdapter(
    private val lifecycle: Lifecycle,
    private val getFavorite: GetFavorite,
    private val onClickListener: (view: View, photo: Photo, position: Int) -> Unit
) : PagingDataAdapter<Photo, RemoteAdapter.ViewHolder>(RemoteAdapter) {

    private var job: Job? = null
    //private val differ = AsyncListDiffer(this, RemoteAdapter)

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
        getItem(position)?.let { holder.viewBiding(it) }
    }

    inner class ViewHolder(
        private val biding: AdapterItemBinding,
        private val onClickListener: (view: View, photo: Photo, position: Int) -> Unit
    ) : RecyclerView.ViewHolder(biding.root) {

        fun viewBiding(photo: Photo) {
            biding.apply {
                imagePhoto.set(photo.downloadUrl)
                imagePhoto.bg(progressPhotos)
                textAuthor.text = photo.author
                textId.text = String.format("#%s", photo.id)
                checkFavorite.isFavorite(photo.id)

                itemView.setOnClickListener {
                    it.postDelayed({
                        onClickListener.invoke(it, photo, layoutPosition)
                    }, 300)
                }

                checkFavorite.setOnClickListener {
                    photo.favorite = checkFavorite.isChecked
                    onClickListener.invoke(it, photo, layoutPosition)
                }
            }
        }
    }

    private fun CheckBox.isFavorite(id: String) {
        job = lifecycle.coroutineScope.launch {
            val dataState = getFavorite.invoke(GetFavorite.Params(id))
            isChecked = dataState is DataState.OnSuccess
                    && dataState.data == true
        }
    }

    fun jobCancel() {
        job?.cancel()
    }
}
