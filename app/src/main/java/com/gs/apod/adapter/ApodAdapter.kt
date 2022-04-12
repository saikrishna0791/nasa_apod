package com.gs.apod.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.gs.apod.databinding.ApodItemBinding
import com.gs.apod.model.ApodItem
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.Priority

import com.bumptech.glide.request.RequestOptions
import com.gs.apod.R
import com.gs.apod.room.entity.FavouriteApods


class ApodAdapter(val context: Context, var apods: List<FavouriteApods>) : RecyclerView.Adapter<ApodAdapter.PostsHolder>() {
    var options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.progress_anim)
        .error(android.R.drawable.stat_notify_error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()

    inner class PostsHolder(itemView: ApodItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        var cardText: TextView = itemView.cardTxt
        var secondText: TextView = itemView.secondTxt
        var apodImage: ImageView = itemView.apodIv
//        var favAnim : LottieAnimationView = itemView.favLv

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsHolder {
        //var view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        var binding : ApodItemBinding = ApodItemBinding.inflate(LayoutInflater.from(parent.context))
        return PostsHolder( binding )
    }

    override fun onBindViewHolder(holder: PostsHolder, position: Int) {
        holder.cardText.text = apods[position].title.trim()
        holder.secondText.text = apods[position].explanation?.trim()
        Glide.with(context)
            .load(apods[position].url)
            .apply(options)
            .into(holder.apodImage);
        /*holder.favAnim.setOnClickListener {
            apods[position].isLiked = !apods[position].isLiked
            holder.favAnim.apply {
                if (apods[position].isLiked) {
                    playAnimation()
                } else {
                    cancelAnimation()
                    progress = 0.0f
                }
            }
        }*/
    }

    override fun getItemCount(): Int {
        return apods.size
    }

    //Call this to add data always to the adapter
    fun setData(newList: List<FavouriteApods>){
        val diffUtil = ApodDiffUtil(apods, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        apods = newList
        diffResult.dispatchUpdatesTo(this)
    }
}