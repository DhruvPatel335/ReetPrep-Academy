package com.reet.prep.academy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reet.prep.academy.R
import com.reet.prep.academy.model.VideoModel

class VideoModelAdapter(val context: Context, val videoList: List<VideoModel>) :
    RecyclerView.Adapter<VideoModelAdapter.ViewHolder>() {
    private lateinit var clickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.current_affair_video_item, parent, false)
        return VideoModelAdapter.ViewHolder(itemView,clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(videoList[position].thumbnail).into(holder.videoImage)
        holder.videoTitle.text = videoList[position].name
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val videoImage = itemView.findViewById<ImageView>(R.id.ivVideoThumbnail)
        val videoTitle = itemView.findViewById<TextView>(R.id.tvVideoName)
        init {
            itemView.setOnClickListener {
                clickListener.onClick(bindingAdapterPosition)
            }
        }
    }
}