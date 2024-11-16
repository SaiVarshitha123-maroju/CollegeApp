package com.example.collegeappjetpackcompose.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.collegeappjetpackcompose.itemview.FacultyItemView
import com.example.collegeappjetpackcompose.navigation.Routes
import com.example.collegeappjetpackcompose.viewmodel.FacultyViewModel

@Composable
fun Faculty(navController: NavController) {
    val facultyViewModel : FacultyViewModel = viewModel()
    val categoryList by facultyViewModel.categoryList.observeAsState(null)
    facultyViewModel.getCategory()

    LazyColumn {
        items(categoryList?: emptyList()){
            FacultyItemView (it, delete = { docId ->
                facultyViewModel.deleteCategory(docId)
            }, onClick = {
                    categoryName->
                val routes= Routes.FacultyDetailScreen.route.replace("{catName}",categoryName)
                navController.navigate(routes)
            })
        }
    }
}