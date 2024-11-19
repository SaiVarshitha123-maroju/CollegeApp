package com.example.collegeappjetpackcompose.admin.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.collegeappjetpackcompose.models.DashBoardItemModel
import com.example.collegeappjetpackcompose.navigation.Routes
import com.example.collegeappjetpackcompose.ui.theme.Purple40
import com.example.collegeappjetpackcompose.ui.theme.Purple80
import com.example.collegeappjetpackcompose.ui.theme.TITLE_SIZE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashBoard(navController: NavController) {



        val list = listOf(
            DashBoardItemModel("Manage Banner",
                Routes.ManageBanner.route),
            DashBoardItemModel("Manage Notice",
                Routes.ManageNotice.route),
            DashBoardItemModel("Manage Faculty",
                Routes.ManageFaculty.route),
            DashBoardItemModel("Manage Gallery",
                Routes.ManageGallery.route),
            DashBoardItemModel("Manage Assignments",
                Routes.ManageAssign.route),
            DashBoardItemModel("Manage College Info",
                Routes.ManageCollegeInfo.route),
        )


        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(text = "FACULTY DashBoard")
                },
                    /*colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Purple80
                    )*/
                    )
            },
        content = {padding ->
            LazyColumn (modifier = Modifier.padding(padding)) {

                items(items = list, itemContent = {

                    Card(modifier = Modifier
                     .fillMaxWidth()
                     .padding(8.dp)

                     .clickable {

                     navController.navigate(it.route)


                     }) {
                     Text(
                         text = it.title,
                         modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                         fontWeight = FontWeight.Bold,
                         fontSize = TITLE_SIZE
                     )
                 }
                })
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun Routes.AdminDashBoardPreview(){
    AdminDashBoard(navController = rememberNavController())
}