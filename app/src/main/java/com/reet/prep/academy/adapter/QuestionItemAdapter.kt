package com.reet.prep.academy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reet.prep.academy.R
import com.reet.prep.academy.model.QuestionsModel


class QuestionItemAdapter(val context: Context, val questionsList: List<QuestionsModel>) :
    RecyclerView.Adapter<QuestionItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.quiz_questions_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return questionsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.question.text = questionsList[position].question
        holder.optionA.text = questionsList[position].optionA
        holder.optionB.text = questionsList[position].optionB
        holder.optionC.text = questionsList[position].optionC
        holder.optionD.text = questionsList[position].optionD
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question: TextView = itemView.findViewById(R.id.tvQuestion)
        val optionA: Button = itemView.findViewById(R.id.btnOptionA)
        val optionB: Button = itemView.findViewById(R.id.btnOptionB)
        val optionC: Button = itemView.findViewById(R.id.btnOptionC)
        val optionD: Button = itemView.findViewById(R.id.btnOptionD)
    }
}