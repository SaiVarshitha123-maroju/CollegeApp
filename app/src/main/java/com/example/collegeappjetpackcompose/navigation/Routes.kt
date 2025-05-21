package com.example.collegeappjetpackcompose.navigation

sealed class Routes(val route:String) {

    object Home: Routes("home")
    object Faculty: Routes("faculty")
    object Gallery: Routes("gallery")
    object AboutUs: Routes("about")
    object BottomNav: Routes("bottom")
    object AdminDashBoard: Routes("admin_dashboard")
    object ManageBanner: Routes("manage_banner")
    object ManageCollegeInfo: Routes("manage_college_info")
    object ManageAssign: Routes("manage_assign")
    object ManageFaculty: Routes("manage_faculty")
    object ManageGallery: Routes("manage_gallery")
    object ManageNotice: Routes("manage_notice")
    object FacultyDetailScreen: Routes("faculty_details/{catName}")
    object Assign: Routes("Assignments")
    object SignIn : Routes("signin")
    object SignUp : Routes("signup")
}