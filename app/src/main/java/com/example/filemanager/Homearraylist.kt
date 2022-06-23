package com.example.filemanager


import android.widget.ImageView
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class Homearraylist(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var img : ImageView = itemView.findViewById(R.id.showimghome)
    var tx1 : TextView  = itemView.findViewById(R.id.showtexthome)
    var container : CardView = itemView.findViewById(R.id.cardView)

}