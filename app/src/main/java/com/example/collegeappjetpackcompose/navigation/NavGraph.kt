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
import com.example.collegeappjetpackcompose.admin.screens.ManageAssign
import com.example.collegeappjetpackcompose.admin.screens.ManageNotice
import com.example.collegeappjetpackcompose.screens.AboutUs
import com.example.collegeappjetpackcompose.screens.Assign
import com.example.collegeappjetpackcompose.screens.BottomNav
import com.example.collegeappjetpackcompose.screens.Faculty
import com.example.collegeappjetpackcompose.screens.Gallery
import com.example.collegeappjetpackcompose.screens.Home
import com. google. firebase. auth. FirebaseAuth
import com.example.collegeappjetpackcompose.screens.SignUpScreen
import SignInScreen
import androidx. compose. ui. platform. LocalContext
import com. google. firebase. firestore. FirebaseFirestore
import androidx. compose. runtime. mutableStateOf
import androidx.compose.runtime.*
import android. widget. Toast

@Composable
fun NavGraph(navController: NavHostController) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    var isUserChecked by remember { mutableStateOf(false) }
    var startDestination by remember { mutableStateOf(Routes.SignIn.route) }

    LaunchedEffect(Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val role = document.getString("role")
                        startDestination = if (role == "Admin") {
                            Routes.AdminDashBoard.route
                        } else {
                            Routes.BottomNav.route
                        }
                    } else {
                        Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error retrieving user role", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    isUserChecked = true
                }
        } else {
            isUserChecked = true
        }
    }

    if (isUserChecked) {

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            // SignIn Screen
            composable(Routes.SignIn.route) {
                SignInScreen(navController)
            }

            // SignUp Screen
            composable(Routes.SignUp.route) {
                SignUpScreen(navController)
            }

            composable(Routes.BottomNav.route) {
                BottomNav(navController)
            }
            composable(Routes.Home.route) {
                Home()
            }
            composable(Routes.AboutUs.route) {
                AboutUs()
            }
            composable(Routes.Assign.route) {
                Assign()
            }
            composable(Routes.Gallery.route) {
                Gallery()
            }
            composable(Routes.Faculty.route) {
                Faculty(navController)
            }
            composable(Routes.AdminDashBoard.route) {
                AdminDashBoard(navController)
            }
            composable(Routes.ManageBanner.route) {
                ManageBanner(navController)
            }
            composable(Routes.ManageNotice.route) {
                ManageNotice(navController)
            }
            composable(Routes.ManageAssign.route) {
                ManageAssign(navController)
            }
            composable(Routes.ManageFaculty.route) {
                ManageFaculty(navController)
            }
            composable(Routes.FacultyDetailScreen.route) {
                val data = it.arguments!!.getString("catName")
                FacultyDetailScreen(navController, data!!)
            }
            composable(Routes.ManageCollegeInfo.route) {
                ManageCollegeInfo(navController)
            }
            composable(Routes.ManageGallery.route) {
                ManageGallery(navController)
            }
        }
    }
}
