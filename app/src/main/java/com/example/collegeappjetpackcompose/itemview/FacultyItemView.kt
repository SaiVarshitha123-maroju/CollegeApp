package com.example.collegeappjetpackcompose.itemview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.collegeappjetpackcompose.R


@Composable
fun FacultyItemView(catName: String,
                   delete:(catName:String)->Unit,
                    onClick:(catName:String)->Unit) {

    OutlinedCard(modifier = Modifier.padding(4.dp).fillMaxWidth().clickable{
        onClick(catName)
    }) {
         ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
             val (category, delete) = createRefs()
             Text(
                 text = catName,
                 modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth().constrainAs(
                     category
                 ){
                     start.linkTo(parent.start)
                     end.linkTo(delete.start)
                     top.linkTo(parent.top)
                     bottom.linkTo(parent.bottom)
                 },
                 fontWeight = FontWeight.Bold,
                 fontSize = 16.sp
             )
             

             Card(modifier = Modifier
                 .constrainAs(delete) {
                     top.linkTo(parent.top)
                     end.linkTo(parent.end)
                 }
                 .padding(4.dp)
                 .clickable {
                     delete(catName)
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
