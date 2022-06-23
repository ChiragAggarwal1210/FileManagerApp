package com.example.filemanager

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import java.io.File
import java.util.*

class FileAdapter(
    private val context: Context,
    private val files : List<File>,
    private val onfileselectedlistner: Onfileselectedlistner
) : RecyclerView.Adapter<Fileviewholders>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Fileviewholders {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filecontainorlayout,parent,false)
        return Fileviewholders(view)
    }

    override fun onBindViewHolder(holder: Fileviewholders, position: Int) {
        val temp = files[position]
        holder.filename.text = temp.name
        var items =0
        if(temp.isDirectory){
            val files1 = temp.listFiles()
            for(singlefile in files1){
                if(!singlefile.isHidden){
                    items++
                }
            }
            holder.filesize.text= "$items Files"
            holder.imgfile.setImageResource(R.drawable.folder)
        }else{
            holder.filesize.text = Formatter.formatShortFileSize(context,temp.length())
            getimg(temp,holder)
        }
        holder.containor.setOnClickListener { onfileselectedlistner.onfileclick(temp)
    }
        holder.containor.setOnLongClickListener {
            onfileselectedlistner.onfilelongclick(temp,position)
            true
        }
    }

    private fun getimg(temp: File, holder: Fileviewholders) {
        if(temp.name.lowercase(Locale.getDefault()).endsWith(".jpeg")||
            temp.name.lowercase(Locale.getDefault()).endsWith(".jpg")||
            temp.name.lowercase(Locale.getDefault()).endsWith(".png")||
            temp.name.lowercase(Locale.getDefault()).endsWith(".gif")){
            holder.imgfile.setImageResource(R.drawable.image)
        }
        else if (temp.name.lowercase(Locale.getDefault()).endsWith(".mp4")||
            temp.name.lowercase(Locale.getDefault()).endsWith(".wav")){
            holder.imgfile.setImageResource(R.drawable.video)
        }
        else if (temp.name.lowercase(Locale.getDefault()).endsWith(".mp3")||
            temp.name.lowercase(Locale.getDefault()).endsWith(".amr")){
            holder.imgfile.setImageResource(R.drawable.audio)
        }
        else if (temp.name.lowercase(Locale.getDefault()).endsWith(".txt")||
            temp.name.lowercase(Locale.getDefault()).endsWith(".pdf")){
            holder.imgfile.setImageResource(R.drawable.file)
        }

    }

    override fun getItemCount(): Int {
       return files.size
    }
}