package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SheetAdapter(val sheetlist: List<String>) :
    RecyclerView.Adapter<SheetAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sheet_name: TextView = view.findViewById(R.id.main_sheet_name)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sheet_information, parent, false)
        val viewHolder = ViewHolder(view)
        // 整个块的点击事件
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val sheet = sheetlist[position]

            MediaInformation.now_sheet = sheet

            // 改变目前开启activity为歌单
            MediaInformation.mode_act = "sheet"
            val intent = Intent(parent.context, Activity_sheet::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            parent.context.startActivity(intent)
        }

        return viewHolder
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val song = sheetlist[position]
        holder.sheet_name.text = song

    }
    override fun getItemCount() = sheetlist.size
}
