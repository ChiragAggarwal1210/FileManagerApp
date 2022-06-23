package com.example.filemanager.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast

import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

import com.example.filemanager.HomeCategory
import com.example.filemanager.Homearraylist
import com.example.filemanager.Model1
import com.example.filemanager.R
import java.io.IOException


class HomearraylistAdapter(var model1ArrayList: ArrayList<Model1>,var context : Context):
RecyclerView.Adapter<Homearraylist>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Homearraylist {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homesingle, parent, false)
        val viewHolder = Homearraylist(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: Homearraylist, position: Int) {
        val temp = model1ArrayList[position]
        holder.tx1.text= temp.text1
        holder.img.setImageResource(temp.image)
        holder.container.setOnClickListener {
            try {
                val i = Intent(context,HomeCategory :: class.java)
                i.putExtra("category",temp.text1)
                context.startActivity(i)
                Toast.makeText(context,"Fetching${temp.text1}",Toast.LENGTH_SHORT).show()

            }
            catch (e : IOException){
                e.printStackTrace()
            }
        }

    }

    override fun getItemCount(): Int {
        return model1ArrayList.size
    }
}
