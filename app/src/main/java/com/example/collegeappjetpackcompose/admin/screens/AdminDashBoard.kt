package com.example.collegeappjetpackcompose.admin.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx. compose. ui. graphics. Color
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.collegeappjetpackcompose.models.DashBoardItemModel
import com.example.collegeappjetpackcompose.navigation.Routes
import com.example.collegeappjetpackcompose.ui.theme.TITLE_SIZE
import androidx. navigation. NavGraph. Companion. findStartDestination
import androidx. compose. ui. platform. LocalContext
import android. widget. Toast
import com. google. firebase. auth. FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashBoard(navController: NavController) {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()

        val list = listOf(
            DashBoardItemModel("Manage Banner",
                Routes.ManageBanner.route),
            DashBoardItemModel("Manage Notice",
                Routes.ManageNotice.route),
            DashBoardItemModel("Manage Teachers",
                Routes.ManageFaculty.route),
            DashBoardItemModel("Manage Events",
                Routes.ManageGallery.route),
            DashBoardItemModel("Manage Homeworks",
                Routes.ManageAssign.route),
            DashBoardItemModel("Manage School Info",
                Routes.ManageCollegeInfo.route),
            DashBoardItemModel("Logout", "logout")

        )


        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(text = "TEACHER DASHBOARD")
                },
                    /*colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Purple80
                    )*/
                    )
            },
            content = { padding ->
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(items = list) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    if (item.title == "Logout") {
                                        // Perform logout
                                        firebaseAuth.signOut()

                                        // Show logout success toast
                                        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()

                                        // Navigate to the login screen
                                        navController.navigate(Routes.SignIn.route) {
                                            // Clear the back stack to prevent going back to the dashboard after logout
                                            popUpTo(0)
                                        }

                                    } else {
                                        // Navigate to respective route for other dashboard items
                                        navController.navigate(item.route)
                                    }
                                }
                        ) {
                            Text(
                                text = item.title,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = TITLE_SIZE,
                                color = if (item.title == "Logout") Color.Red else Color.Black // Highlight logout in red
                            )
                        }
                    }
                }
            }
        )
}

@Preview(showSystemUi = true)
@Composable
fun AdminDashBoardPreview() {
    AdminDashBoard(navController = rememberNavController())
}