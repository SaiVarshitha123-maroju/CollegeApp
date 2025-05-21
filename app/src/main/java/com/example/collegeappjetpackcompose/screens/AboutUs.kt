package com.example.collegeappjetpackcompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.rememberAsyncImagePainter
import com.example.collegeappjetpackcompose.ui.theme.SkyBlue
import com.example.collegeappjetpackcompose.viewmodel.CollegeInfoViewModel

@Composable
fun AboutUs() {
    val collegeInfoViewModel: CollegeInfoViewModel = viewModel()
    // Calling getCollegeInfo to fetch the college info if it hasn't been fetched already
    LaunchedEffect(Unit) {
       collegeInfoViewModel.getCollegeInfo()
    }

    val collegeInfo by collegeInfoViewModel.collegeInfo.observeAsState(emptyList())

    Column(modifier = Modifier.padding(8.dp)) {
        // Checking if the collegeInfo list is not empty
        if (collegeInfo.isNotEmpty()) {
            val info = collegeInfo[0] // Access the first element
            // Displaying the college image, name, description, and website link
            Image(
                painter = rememberAsyncImagePainter(model = info.imageUrl),
                contentDescription = "School Image",
                modifier = Modifier.height(220.dp).fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = info.name ?: "School Name Not Available",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = info.desc ?: "School Description Not Available",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = info.websiteLink ?: "School Website Link Not Available",
                color = SkyBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
        } else {
            Text(
                text = "School information not available.",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
