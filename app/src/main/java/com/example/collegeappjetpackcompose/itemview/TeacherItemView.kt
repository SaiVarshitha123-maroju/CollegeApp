package com.example.collegeappjetpackcompose.itemview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.rememberAsyncImagePainter
import com.example.collegeappjetpackcompose.R
import com.example.collegeappjetpackcompose.models.FacultyModel
import com.example.collegeappjetpackcompose.ui.theme.SkyBlue
import com.example.collegeappjetpackcompose.utils.Constant


@Composable
fun TeacherItemView(facultyModel: FacultyModel,
                   delete:(facultyModel: FacultyModel)->Unit) {

    OutlinedCard(modifier = Modifier.padding(4.dp)) {
         ConstraintLayout {

             val (image, delete) = createRefs()

             Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)){
                 Spacer(modifier = Modifier.height(5.dp))
                 Image(
                     painter =  rememberAsyncImagePainter(model = facultyModel.imageUrl),
                     contentDescription = Constant.BANNER,
                     modifier = Modifier
                         .height(120.dp)
                         .width(120.dp)
                         .clip(CircleShape),
                     contentScale = ContentScale.Crop
                 )
                 Spacer(modifier = Modifier.height(5.dp))
                 Text(
                     text = facultyModel.name!!,
                     modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                     fontWeight = FontWeight.Bold,
                     fontSize = 14.sp
                 )

                 Text(
                     text = facultyModel.email!!,
                     modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                     fontSize = 14.sp,

                 )
                 Text(
                     text = facultyModel.position!!,
                     modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                     fontSize = 14.sp,
                     color = SkyBlue
                 )

             }

             Card(modifier = Modifier
                 .constrainAs(delete) {
                     top.linkTo(parent.top)
                     end.linkTo(parent.end)
                 }
                 .padding(4.dp)
                 .clickable {
                     delete(facultyModel)
                 }
             ) {
                 Image(painter = painterResource(id = R.drawable.delete),
                     contentDescription = null,
                     modifier = Modifier.padding(8.dp)
                 )
             }
         }
    }
}
