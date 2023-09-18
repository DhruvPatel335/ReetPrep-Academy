package com.reet.prep.academy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reet.prep.academy.R
import com.reet.prep.academy.model.CAProduct

class CurrentAffairPdfAdapter(
    private val context: Context,
    private val pdfItems: List<CAProduct>
) : RecyclerView.Adapter<CurrentAffairPdfAdapter.ViewHolder>() {
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
                .inflate(R.layout.current_affair_pdf_item, parent, false)
        return ViewHolder(itemView,clickListener)
    }

    override fun getItemCount(): Int {
        return pdfItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pdfTitle.text = pdfItems[position].title
    }

    class ViewHolder(itemView: View,clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val pdfTitle = itemView.findViewById<TextView>(R.id.tvPdfTitle)
        init {
            itemView.setOnClickListener {
                clickListener.onClick(adapterPosition)
            }
        }
    }
}