package com.example.filemanager

import java.io.File
interface Onfileselectedlistner {
    fun onfileclick(file : File)
    fun onfilelongclick(file: File,position: Int)
}