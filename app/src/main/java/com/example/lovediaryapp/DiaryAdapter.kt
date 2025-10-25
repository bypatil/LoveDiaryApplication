package com.example.lovediaryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryAdapter(
    private val diaryList: MutableList<DiaryEntry>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    interface OnItemClickListener {
        fun onEdit(entry: DiaryEntry, position: Int)
        fun onDelete(entry: DiaryEntry, position: Int)
    }

    inner class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.tvTitle)
        val contentText: TextView = itemView.findViewById(R.id.tvContent)
        val dateText: TextView = itemView.findViewById(R.id.tvDate)
        val editButton: ImageButton = itemView.findViewById(R.id.btnEdit)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
        return DiaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val entry = diaryList[position]
        holder.titleText.text = entry.title
        holder.contentText.text = entry.content
        holder.dateText.text = entry.date

        holder.editButton.setOnClickListener { listener.onEdit(entry, position) }
        holder.deleteButton.setOnClickListener { listener.onDelete(entry, position) }
    }

    override fun getItemCount(): Int = diaryList.size
}
