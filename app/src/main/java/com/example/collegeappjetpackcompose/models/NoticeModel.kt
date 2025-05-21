package com.example.collegeappjetpackcompose.models

import java.time.LocalDate
import java.time.LocalTime

data class NoticeModel(
    val imageUrl: String? = null,
    val docId: String? = null,
    val title: String? = null,
    val link: String? = null,
    val date: String? = null,
    val time: String? = null,
    val venue: String? = null
)
