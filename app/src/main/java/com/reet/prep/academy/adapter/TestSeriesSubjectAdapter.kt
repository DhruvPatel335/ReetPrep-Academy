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
import com.reet.prep.academy.model.TestSubject

class TestSeriesSubjectAdapter(val context: Context, val subjectList: List<TestSubject>) :
    RecyclerView.Adapter<TestSeriesSubjectAdapter.ViewHolder>() {
    private lateinit var clickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.explore_courses_item, parent, false)
        return ViewHolder(itemView, clickListener)
    }

    override fun getItemCount(): Int {
        return subjectList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(subjectList[position].thumbnailUrl).into(holder.thumbnailImage)
        holder.testSubjectName.text = subjectList[position].testSubject
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val thumbnailImage: ImageView = itemView.findViewById(R.id.ivCategoryImage)
        val testSubjectName: TextView = itemView.findViewById(R.id.tvCategoryItemName)

        init {
            itemView.setOnClickListener {
                clickListener.onClick(bindingAdapterPosition)
            }
        }
    }
}