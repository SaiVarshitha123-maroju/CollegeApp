package com.example.collegeappjetpackcompose.screens

import com.example.collegeappjetpackcompose.R
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.collegeappjetpackcompose.models.BottomNavItem
import com.example.collegeappjetpackcompose.models.NavItem
import com.example.collegeappjetpackcompose.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navController: NavController) {

    val navController1 = rememberNavController()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

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
                        Toast.makeText(context, items.title, Toast.LENGTH_SHORT).show()

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
                topBar = { TopAppBar(title = { Text(text = "BRIGHTPATH") },
                    navigationIcon = {
                        IconButton(onClick = {scope.launch{drawerState.open()} }) {
                            Icon(painter = painterResource(id = R.drawable.menu), contentDescription = null)
                            
                        }
                    })
                },
                bottomBar = {
                    MyBottomNav(navController = navController1)
                }

            ) {padding ->
                NavHost(navController = navController1,
                    startDestination = Routes.Home.route,
                    modifier =  Modifier.padding(padding))
                {
                    composable(route = Routes.Home.route){
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
                    composable(Routes.Assign.route){
                        Assign()
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
            "Teachers",
            R.drawable.faculty,
            Routes.Faculty.route
        ),
        BottomNavItem(
            "Events",
            R.drawable.gallery,
            Routes.Gallery.route
        ),
        BottomNavItem(
            "About Us",
            R.drawable.info,
            Routes.AboutUs.route
        ),
        BottomNavItem(
            "Homeworks",
            R.drawable.homework,
            Routes.Assign.route
    )
    )


    BottomAppBar {
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
                icon = { Icon(painter = painterResource(id = it.icon), contentDescription = it.title,
                    modifier = Modifier.size(24.dp)
                )
                })
        }
    }
}
