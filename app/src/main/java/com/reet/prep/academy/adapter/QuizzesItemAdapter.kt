package com.reet.prep.academy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reet.prep.academy.R
import org.w3c.dom.Text

class QuizzesItemAdapter(val context: Context, val quizList: List<String>) :
    RecyclerView.Adapter<QuizzesItemAdapter.ViewHolder>() {
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
                .inflate(R.layout.quizzes_item, parent, false)
        return QuizzesItemAdapter.ViewHolder(itemView, clickListener)
    }

    override fun getItemCount(): Int {
        return quizList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.quizName.text = quizList[position]
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val quizName: TextView = itemView.findViewById(R.id.tvQuizName)

        init {
            itemView.setOnClickListener {
                clickListener.onClick(bindingAdapterPosition)
            }
        }
    }
}