package com.gs.apod.adapter

import com.gs.apod.room.entity.FavouriteApods

interface FavouriteListener {
    fun addTofavourite(data : FavouriteApods)
}