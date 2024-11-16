package com.example.collegeappjetpackcompose.models

import com.example.collegeappjetpackcompose.navigation.Routes

data class NavItem(

    val title:String,
    val icon: Int
)

data class BottomNavItem(
    val title:String,
    val icon: Int,
    val routes: String
)
