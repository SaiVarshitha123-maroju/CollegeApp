package com.example.collegeappjetpackcompose.models

data class AssignModel(
    val fileUrl: String? = "", // URL for the uploaded file
    val fileTitle: String? = "", // Title of the file
    val docId: String? = "" // Firebase document ID
)
