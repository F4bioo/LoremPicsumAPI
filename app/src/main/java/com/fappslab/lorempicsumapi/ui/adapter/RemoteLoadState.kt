package com.fappslab.lorempicsumapi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fappslab.lorempicsumapi.databinding.AdapterLoadBinding

class RemoteLoadState(
    private val onClickListener: () -> Unit
) : LoadStateAdapter<RemoteLoadState.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val biding = AdapterLoadBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(biding, onClickListener)
    }

    override fun onBindViewHolder(holder: RemoteLoadState.ViewHolder, loadState: LoadState) {
        holder.viewBiding(loadState)
    }

    inner class ViewHolder(
        private val binding: AdapterLoadBinding,
        private val onClickListener: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun viewBiding(loadState: LoadState) {
            binding.apply {

                itemView.layoutParams.let { it as StaggeredGridLayoutManager.LayoutParams }
                    .apply { isFullSpan = true }

                progress.isVisible = loadState is LoadState.Loading
                buttonTryAgain.isVisible = loadState is LoadState.Error

                textError.isVisible =
                    !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()

                textError.text = (loadState as? LoadState.Error)?.error?.message

                buttonTryAgain.setOnClickListener {
                    onClickListener()
                }
            }
        }
    }
}
