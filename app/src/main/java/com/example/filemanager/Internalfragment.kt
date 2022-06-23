package com.example.filemanager

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.format.Formatter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Internalfragment : Fragment(),Onfileselectedlistner {

    lateinit var recyclerView: RecyclerView
    lateinit var showfilepath: TextView
    lateinit var back: ImageView
    lateinit var fileList: MutableList<File>
    lateinit var storage: File
    lateinit var data: String
    lateinit var fileAdapter: FileAdapter
    var items = arrayOf("Details", "Rename", "Delete", "Share")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_internalfragment, container, false)
        showfilepath = view.findViewById(R.id.internalfragmentfilepath)
        back = view.findViewById(R.id.internalfragmentbackimg)
        recyclerView = view.findViewById(R.id.internalfragmentrecyclerview)
        val internalstorage = System.getenv("EXTERNAL_STORAGE")
        storage = File(internalstorage)
        showfilepath.text = storage.absolutePath
        try {
            data = requireArguments().getString("path").toString()
            val file = File(data)
            storage = file
            showfilepath.text = storage.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        displayfiles()
        return view
    }

    private fun displayfiles() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        fileList = ArrayList()
        fileList.addAll(searchfile(storage))
        fileAdapter = FileAdapter(requireContext(), fileList, this)
        recyclerView.adapter = fileAdapter
    }

    private fun searchfile(file: File): ArrayList<File> {
        val fileArrayList = ArrayList<File>()
        val files = file.listFiles()
        for (singlefile in files) {
            if (singlefile.isDirectory && !singlefile.isHidden) {
                fileArrayList.add(singlefile)
            }
        }
        for(singlefile in files)
        {
            if (singlefile.name.lowercase(Locale.getDefault()).endsWith(".jpej")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".jpj")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".png")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".gif")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".mp3")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".wav")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".mp4")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".txt")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".pdf")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".jpg")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".amr")||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".docx")) {
                fileArrayList.add(singlefile)
            }
        }
        return fileArrayList
    }

    override fun onfileclick(file: File) {
        if(file.isDirectory){
            val bundle = Bundle()
            bundle.putString("path",file.absolutePath)
            val internalfragment = Internalfragment()
            internalfragment.arguments = bundle
            requireFragmentManager().beginTransaction().replace(R.id.frameLayout,internalfragment)
                .addToBackStack(null).commit()
        }else{
            try {
                Fileopener.fileopener(requireContext(),file)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    override fun onfilelongclick(file: File, position: Int) {
        val optiondialogueBox = Dialog(requireContext())
        optiondialogueBox.setContentView(R.layout.option_dialogue)
        optiondialogueBox.setTitle("select a option")
        val optiondialogues = optiondialogueBox.findViewById<View>(R.id.list) as ListView
        val customadapter = CustomAdapter()
        optiondialogues.adapter=customadapter
        optiondialogueBox.show()
        optiondialogues.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
            when(parent.getItemAtPosition(position).toString()){
                "Details" -> {
                    val alertdialog = AlertDialog.Builder(context)
                    alertdialog.setTitle("Details")
                    val details = TextView(context)
                    alertdialog.setView(details)
                    val lastmodifieddate = Date(file.lastModified())
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                    val dateformatted = simpleDateFormat.format(lastmodifieddate)
                    val size = Formatter.formatShortFileSize(context,file.length())
                    details.text="""
                        File Name: ${file.name}
                        Path : ${file.absolutePath}
                        File size : $size
                        Last modified date : $dateformatted
                    """.trimIndent()
                    alertdialog.setPositiveButton("ok"){dialog,_ -> dialog.dismiss()}
                    alertdialog.create()
                    alertdialog.show()

                }
                "Rename" -> {
                    val renamealertdialog =  AlertDialog.Builder(context)
                    renamealertdialog.setTitle("Rename File")
                    val name = EditText(context)
                    renamealertdialog.setView(name)
                    renamealertdialog.setPositiveButton("ok"){ dialog, _ ->
                        val newfilename= name.editableText.toString()
                        val extension = file.absolutePath.substring(file.absolutePath.lastIndexOf(".")+1)
                        val current = File(file.absolutePath)
                        val destination = File(file.absolutePath.replace(file.name,newfilename)+extension)
                        if(current.renameTo(destination)){
                            fileList[position] = destination
                            fileAdapter.notifyDataSetChanged()
                            Toast.makeText(context,"file name changed", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context,"File name could not be changed",Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                    }
                    renamealertdialog.setNegativeButton("cancel") { dialog,_ ->  dialog.dismiss()}
                    renamealertdialog.create()
                    renamealertdialog.show()
                }
                "Delete" -> deletefile(file,position)
                "Share" ->  sharefile(file)
            }
        }
    }

     fun sharefile(file: File) {
        val filename = file.name
         val share = Intent()
         share.action = Intent.ACTION_SEND
         share.type = "*/*"
         val URI = FileProvider.getUriForFile(requireContext(),requireContext().applicationContext.packageName+ ".provider",file)
         share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
         share.putExtra(Intent.EXTRA_STREAM , URI)
         startActivity(Intent.createChooser(share,"Send $filename?"))
     }

    fun deletefile(file: File, position: Int) {
            val deletefile = AlertDialog.Builder(context)
            deletefile.setTitle("Delete"+file.name + " ?")
            deletefile.setPositiveButton("ok"){ dialog, _ ->
                file.delete()
                fileList.removeAt(position)
                fileAdapter.notifyDataSetChanged()
                dialog.dismiss()
                Toast.makeText(context,"File Deleted Successfully",Toast.LENGTH_SHORT).show()
            }
         deletefile.setNegativeButton("cancel"){ dialog,_ ->
             dialog.dismiss()
         }
         deletefile.create()
         deletefile.show()
    }


    internal inner class CustomAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return items.size
        }

        override fun getItem(p0: Int): Any {
            return items[p0]
        }

        override fun getItemId(p0: Int): Long {
          return 0
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
          val view = layoutInflater.inflate(R.layout.option_layout,null)
          val textoption = view.findViewById<TextView>(R.id.txtoption)
          val img = view.findViewById<ImageView>(R.id.imgoption)
          textoption.text =items[p0]

          if(items[p0]== "Details") {
              img.setImageResource(R.drawable.details)
          }
            if(items[p0]== "Rename") {
                img.setImageResource(R.drawable.rename)
            }
            if(items[p0]== "Delete") {
                img.setImageResource(R.drawable.delete)
            }
            if(items[p0]== "Share") {
                img.setImageResource(R.drawable.share)
            }
            return view
        }

    }
}

