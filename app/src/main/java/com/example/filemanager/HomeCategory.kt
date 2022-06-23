package com.example.filemanager

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*


class HomeCategory : AppCompatActivity() {

    lateinit var category : String
    lateinit var categoryname : TextView
    lateinit var filelink : String
    lateinit var categoryrecycler : RecyclerView
    lateinit var categoryviewAdapter: CategoryviewAdapter
    lateinit var datas : MutableList<File>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_category)

        category = intent.getStringExtra("category").toString()
        categoryname = findViewById<View>(R.id.homecategory) as TextView
        categoryrecycler = findViewById<View>(R.id.homecategoryrecycler) as RecyclerView
        categoryrecycler.layoutManager= LinearLayoutManager(this)
        datas = ArrayList()
        categoryviewAdapter=CategoryviewAdapter(this@HomeCategory, datas as ArrayList<File>,category)
        categoryrecycler.adapter = categoryviewAdapter
        filelink =System.getenv("EXTERNAL_STORAGE")
        categoryname.text = category
        displayfile()
    }

    private fun displayfile() {
        categoryrecycler.layoutManager= LinearLayoutManager(this)
        datas = ArrayList()
        datas.addAll( gettingfile(filelink))
        categoryviewAdapter=CategoryviewAdapter(this@HomeCategory, datas as ArrayList<File>,category)
        categoryrecycler.adapter = categoryviewAdapter
    }

    private fun gettingfile(path: String) : ArrayList<File> {
        val file = File(path)
        val fileArrayList = ArrayList<File>()
        if(file.isDirectory && file.canRead()){
            val file1 = file.listFiles()
            for (singlefile in file1){
                if (singlefile.isDirectory && singlefile.canRead()){
                    gettingfile(singlefile.absolutePath)
                }else if(singlefile.isFile && singlefile.canRead()){
                    when(category){
                        "Images" -> if(singlefile.name.lowercase(Locale.getDefault()).endsWith(".png") ||
                            singlefile.name.lowercase(Locale.getDefault()).endsWith(".jpg") ||
                            singlefile.name.lowercase(Locale.getDefault()).endsWith(".jpej") ||
                            singlefile.name.lowercase(Locale.getDefault()).endsWith(".gif")){
                            fileArrayList.add(singlefile)

                        }
                        "Video" -> if(singlefile.name.lowercase(Locale.getDefault()).endsWith(".mp4") ||
                            singlefile.name.lowercase(Locale.getDefault()).endsWith(".mkv")){
                            fileArrayList.add(singlefile)

                        }
                        "Audio" -> if(singlefile.name.lowercase(Locale.getDefault()).endsWith(".mp3") ||
                            singlefile.name.lowercase(Locale.getDefault()).endsWith(".wav")){
                            fileArrayList.add(singlefile)

                        }
                        "Document" -> if(singlefile.name.lowercase(Locale.getDefault()).endsWith(".txt") ||
                            singlefile.name.lowercase(Locale.getDefault()).endsWith(".pdf")){
                            fileArrayList.add(singlefile)

                        }
                    }
                }

            }
        }else if(file.isFile && file.canRead()){
            when(category){
                "Images" -> if(file.name.lowercase(Locale.getDefault()).endsWith(".png") ||
                    file.name.lowercase(Locale.getDefault()).endsWith(".jpg") ||
                    file.name.lowercase(Locale.getDefault()).endsWith(".jpej") ||
                    file.name.lowercase(Locale.getDefault()).endsWith(".gif")){
                    fileArrayList.add(file)

                }
                "Video" -> if(file.name.lowercase(Locale.getDefault()).endsWith(".mp4") ||
                    file.name.lowercase(Locale.getDefault()).endsWith(".mkv")){
                    fileArrayList.add(file)

                }
                "Audio" -> if(file.name.lowercase(Locale.getDefault()).endsWith(".mp3") ||
                    file.name.lowercase(Locale.getDefault()).endsWith(".wav")){
                    fileArrayList.add(file)

                }
                "Document" -> if(file.name.lowercase(Locale.getDefault()).endsWith(".txt") ||
                    file.name.lowercase(Locale.getDefault()).endsWith(".pdf")){
                    fileArrayList.add(file)

                }
            }
        }
        return fileArrayList
    }
}