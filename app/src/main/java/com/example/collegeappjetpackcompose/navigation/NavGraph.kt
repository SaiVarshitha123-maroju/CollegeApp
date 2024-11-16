package com.example.collegeappjetpackcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.collegeappjetpackcompose.admin.screens.AdminDashBoard
import com.example.collegeappjetpackcompose.admin.screens.FacultyDetailScreen
import com.example.collegeappjetpackcompose.admin.screens.ManageBanner
import com.example.collegeappjetpackcompose.admin.screens.ManageCollegeInfo
import com.example.collegeappjetpackcompose.admin.screens.ManageFaculty
import com.example.collegeappjetpackcompose.admin.screens.ManageGallery
import com.example.collegeappjetpackcompose.admin.screens.ManageNotice
import com.example.collegeappjetpackcompose.screens.AboutUs
import com.example.collegeappjetpackcompose.screens.BottomNav
import com.example.collegeappjetpackcompose.screens.Faculty
import com.example.collegeappjetpackcompose.screens.Gallery
import com.example.collegeappjetpackcompose.screens.Home
import com.example.collegeappjetpackcompose.utils.Constant.isAdmin

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController ,
        startDestination =  if (isAdmin) Routes.AdminDashBoard.route else Routes.BottomNav.route
    ){

        composable(Routes.BottomNav.route){
            BottomNav(navController)
        }

        composable(Routes.Home.route){
            Home()
        }

        composable(Routes.AboutUs.route){
            AboutUs()
        }

        composable(Routes.Gallery.route){
            Gallery()
        }

        composable(Routes.Faculty.route){
            Faculty(navController)
        }

        composable(Routes.AdminDashBoard.route){
            AdminDashBoard(navController)
        }

        composable(Routes.ManageBanner.route){
            ManageBanner(navController)
        }
        composable(Routes.ManageNotice.route){
            ManageNotice(navController)
        }

        composable(Routes.ManageFaculty.route){
            ManageFaculty(navController)
        }
        composable(Routes.FacultyDetailScreen.route){
            val data=it.arguments!!.getString("catName")
            FacultyDetailScreen(navController,data!!)
        }

        composable(Routes.ManageCollegeInfo.route){
            ManageCollegeInfo(navController)
        }

        composable(Routes.ManageGallery.route){
            ManageGallery(navController)
        }



    }
}