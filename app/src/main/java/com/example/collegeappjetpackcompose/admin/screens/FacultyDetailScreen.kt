package com.example.collegeappjetpackcompose.admin.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.collegeappjetpackcompose.viewmodel.FacultyViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.collegeappjetpackcompose.itemview.TeacherItemView
import com.example.collegeappjetpackcompose.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultyDetailScreen(navController: NavController,catName:String) {
    val context = LocalContext.current

    val facultyViewModel : FacultyViewModel = viewModel()
    val facultyList by facultyViewModel.facultyList.observeAsState(null)

    facultyViewModel.getFaculty(catName)
    Scaffold(

        topBar = {
            TopAppBar(title = {
                Text(text = catName,
                    color = Color.White
                )
            },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Purple40),

                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack, contentDescription = null,
                            tint = Color.White
                        )


                    }
                },
            )
        },

        ) { padding ->
        LazyVerticalGrid(columns= GridCells.Adaptive(minSize = 140.dp),
            modifier = Modifier.padding(padding)) {
            items(facultyList?:emptyList()){
                TeacherItemView(facultyModel = it, delete = {
                    facultyModel->
                    facultyViewModel.deleteFaculty(facultyModel)
                })
            }
        }
    }

}
