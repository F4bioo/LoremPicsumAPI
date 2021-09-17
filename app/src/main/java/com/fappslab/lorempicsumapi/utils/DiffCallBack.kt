package com.fappslab.lorempicsumapi.utils

import androidx.recyclerview.widget.DiffUtil
import com.fappslab.lorempicsumapi.data.model.Photo

class DiffCallBack(
    private var oldCList: List<Photo>,
    private var newList: List<Photo>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldCList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCList[oldItemPosition] == newList[newItemPosition]
    }
}
