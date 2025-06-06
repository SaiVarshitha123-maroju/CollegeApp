package com.example.collegeappjetpackcompose.itemview

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.rememberAsyncImagePainter
import com.example.collegeappjetpackcompose.R
import com.example.collegeappjetpackcompose.models.NoticeModel

@Composable
fun NoticeItemView(noticeModel: NoticeModel,
                   delete:(noticeModel: NoticeModel)->Unit) {

    OutlinedCard(modifier = Modifier.padding(4.dp)) {
         ConstraintLayout {

             val (image, delete) = createRefs()

             Column {
                 Text(
                     text = noticeModel.title!!,
                     modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                     fontWeight = FontWeight.Bold,
                     fontSize = 16.sp
                 )
                 if (noticeModel.link != "")
                     Text(
                         text = noticeModel.link!!,
                         modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                         fontSize = 14.sp
                     )
                 if (noticeModel.date.toString() != "")
                     Text(
                         text = noticeModel.date.toString(),
                         modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                         fontSize = 14.sp
                     )
                 if (noticeModel.time.toString() != "")
                     Text(
                         text = noticeModel.time.toString(),
                         modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                         fontSize = 14.sp
                     )
                 if (noticeModel.venue != "")
                     Text(
                         text = noticeModel.venue!!,
                         modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                         fontSize = 14.sp
                     )
                 Image(
                     painter = rememberAsyncImagePainter(
                         model = noticeModel.imageUrl,
                         placeholder = painterResource(R.drawable.image_placeholder)
                     )
                     ,
                     contentDescription = null,
                     modifier = Modifier
                         .height(220.dp)
                         .fillMaxWidth(),
                     contentScale = ContentScale.Crop
                 )
                 Log.d("NoticeImageURL", "URL: ${noticeModel.imageUrl}")
             }

             Card(modifier = Modifier
                 .constrainAs(delete) {
                     top.linkTo(parent.top)
                     end.linkTo(parent.end)
                 }
                 .padding(4.dp)
                 .clickable {
                     delete(noticeModel)
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
