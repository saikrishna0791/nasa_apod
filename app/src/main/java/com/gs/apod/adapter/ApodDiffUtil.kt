package com.gs.apod.adapter

import androidx.recyclerview.widget.DiffUtil
import com.gs.apod.room.entity.FavouriteApods

class ApodDiffUtil(
    private val oldList : List<FavouriteApods>,
    private val newList : List<FavouriteApods>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].url == newList[newItemPosition].url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return oldList[oldItemPosition] == newList[newItemPosition]
        return when {
            oldList[oldItemPosition].url != newList[newItemPosition].url -> {
                false
            }

            oldList[oldItemPosition].title != newList[newItemPosition].title -> {
                false
            }

            else -> true
        }
    }
}