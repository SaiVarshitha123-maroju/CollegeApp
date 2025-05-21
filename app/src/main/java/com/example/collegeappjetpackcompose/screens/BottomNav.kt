package com.example.collegeappjetpackcompose.screens

import com.example.collegeappjetpackcompose.R
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.collegeappjetpackcompose.models.BottomNavItem
import com.example.collegeappjetpackcompose.models.NavItem
import com.example.collegeappjetpackcompose.navigation.Routes
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com. google. firebase. auth. FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navController: NavController) {

    val navController1 = rememberNavController()
    val monospaceFont = FontFamily.Monospace
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lightCreamColor = Color(0xFFFFF5E1)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val firebaseAuth = FirebaseAuth.getInstance()

    val list = listOf(
        NavItem(
            "Website",
            R.drawable.globe,
        ),
        NavItem(
            "Notice",
            R.drawable.notification,
        ),
        NavItem(
            "About Us",
            R.drawable.info,
        ),
        NavItem(
            "Events",
            R.drawable.gallery,
        ),
        NavItem(
            "Contact Us",
            R.drawable.write,
        ),
        NavItem("Logout", R.drawable.logout),
    )

    ModalNavigationDrawer(drawerState = drawerState
        , drawerContent = {
            ModalDrawerSheet {
                Image(painter = painterResource(id = R.drawable.icon), contentDescription = null,
                    modifier = Modifier.height(220.dp),
                    contentScale = ContentScale.Crop)

                Divider()
                Text(text = "")

                list.forEachIndexed { index, items ->
                NavigationDrawerItem(
                    label = {
                        Text(text = items.title)
                    },
                    selected = index == selectedItemIndex,
                    onClick = {
                        if (items.title == "Logout") {
                            firebaseAuth.signOut()
                            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate(Routes.SignIn.route) {
                                popUpTo(0) // Clear the back stack
                            }
                        } else {
                            Toast.makeText(context, items.title, Toast.LENGTH_SHORT).show()
                        }
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = items.icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    })
            }
        }
    } ,
        content = {
            Scaffold(
                modifier = Modifier.background(lightCreamColor),
                topBar = { TopAppBar(title = { Text(
                    text = "BRIGHTPATH",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = monospaceFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    ),
                    color = Color.White // Text color for contrast
                ) },
                    navigationIcon = {
                        IconButton(onClick = {scope.launch{drawerState.open()} }) {
                            Icon(
                                painter = painterResource(id = R.drawable.menu),
                                contentDescription = null
                            )
                        }
                        },
                        actions = {
                            IconButton(onClick = {
                                // Action for notifications
                                Toast.makeText(context, "Notifications clicked", Toast.LENGTH_SHORT).show()
                                // You can add more actions here, like opening a Notification screen
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.notification),
                                    contentDescription = "Notifications",
                                    modifier = Modifier.size(30.dp) // Make it bigger
                                )
                            }
                    },backgroundColor = Color(0xFF008080))
                },
                bottomBar = {
                    MyBottomNav(navController = navController1)
                }

            ) {padding ->
                Box(modifier = Modifier.fillMaxSize().background(lightCreamColor)) {
                    NavHost(
                        navController = navController1,
                        startDestination = Routes.Home.route,
                        modifier = Modifier.padding(padding)
                    )
                    {
                        composable(route = Routes.Home.route) {
                            Home()
                        }

                        composable(Routes.AboutUs.route) {
                            AboutUs()
                        }

                        composable(Routes.Gallery.route) {
                            Gallery()
                        }

                        composable(Routes.Faculty.route) {
                            Faculty(navController)
                        }
                        composable(Routes.Assign.route) {
                            Assign()
                        }
                    }
                }
            }
        })

}
@Composable
fun MyBottomNav(navController: NavController){
    val backStackEntry = navController.currentBackStackEntryAsState()

    val list = listOf(
        BottomNavItem(
            "Home",
            R.drawable.home,
            Routes.Home.route
        ),
        BottomNavItem(
            "Homeworks",
            R.drawable.homework,
            Routes.Assign.route
        ),
        BottomNavItem(
            "Events",
            R.drawable.gallery,
            Routes.Gallery.route
        ),
        BottomNavItem(
            "Teachers",
            R.drawable.faculty,
            Routes.Faculty.route
        ),

        BottomNavItem(
            "About Us",
            R.drawable.info,
            Routes.AboutUs.route
        ),

        )


    BottomAppBar( backgroundColor = Color.White) {
        list.forEach {

            val curRoute = it.routes
            val otherRoute =
                try {
                    backStackEntry.value!!.destination.route
                } catch (e: Exception){
                    Routes.Home.route
                }

            val selected = curRoute == otherRoute

            NavigationBarItem(selected = selected ,
                onClick = { navController.navigate(it.routes){
                    popUpTo(navController.graph.findStartDestination().id){
                        saveState = true
                    }
                    launchSingleTop = true
                } },
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.title,
                        modifier = Modifier.size(24.dp),
                        tint = if (selected) Color(0xFF008080) else Color.Gray // Highlight selected icon with teal color
                    )
                },
            )
        }
    }
}