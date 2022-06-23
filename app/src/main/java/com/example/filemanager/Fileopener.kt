package com.example.filemanager

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

object Fileopener {
    fun fileopener(context: Context,file : File){
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName+".provider",
            file
        )
        var intent = Intent(Intent.ACTION_VIEW)
        if (uri.toString().contains(".pdf")||uri.toString().contains(".txt")){
            intent.setDataAndType(uri,"text/plain")
        }else if (uri.toString().contains(".mp3")||uri.toString().contains(".wav")){
            intent.setDataAndType(uri,"audio/x-wav")
        }else if (uri.toString().contains(".png")||uri.toString().contains(".jpg")||
            uri.toString().contains(".jpeg")||
            uri.toString().contains(".gif")){
            intent.setDataAndType(uri,"image/jpej")
        }else if(uri.toString().contains(".mp4")){
            intent.setDataAndType(uri,"video/*")
        }else{
            intent.setDataAndType(uri,"*/*")
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }
}