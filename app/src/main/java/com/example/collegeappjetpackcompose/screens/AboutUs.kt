package com.example.collegeappjetpackcompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.collegeappjetpackcompose.ui.theme.SkyBlue
import com.example.collegeappjetpackcompose.viewmodel.CollegeInfoViewModel

@Composable
fun AboutUs() {
    val collegeInfoViewModel : CollegeInfoViewModel = viewModel()

    val collegeInfo by collegeInfoViewModel.collegeInfo.observeAsState(null)
    // collegeInfoViewModel.getCollegeInfo()
     Column(modifier = Modifier.padding(8.dp)){
         if(collegeInfo!=null){
             Image(painter = rememberAsyncImagePainter(model=collegeInfo!![0].imageUrl), contentDescription = "college Image",
                 modifier = Modifier.height(220.dp).fillMaxWidth(),
                 contentScale = ContentScale.Crop)
             Spacer(modifier = Modifier.height(10.dp))
             Text(text = collegeInfo!![0].name ?: "College Name Not Available",
                 color = Color.White,
                 fontWeight = FontWeight.Bold,
                 fontSize = 18.sp
             )
             Spacer(modifier = Modifier.height(8.dp))
             Text(text = collegeInfo!![0].desc ?: "College Description Not Available",
                 color = Color.White,
                 fontWeight = FontWeight.Bold,
                 fontSize = 18.sp
             )
             Spacer(modifier = Modifier.height(8.dp))
             Text(text = collegeInfo!![0].websiteLink ?: "College Description Not Available",
                 color = SkyBlue,
                 fontWeight = FontWeight.Bold,
                 fontSize = 18.sp
             )
             Spacer(modifier = Modifier.height(12.dp))

         }

     }
}