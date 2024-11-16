package com.example.collegeappjetpackcompose.itemview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.rememberAsyncImagePainter
import com.example.collegeappjetpackcompose.models.BannerModel

import com.example.collegeappjetpackcompose.R
import com.example.collegeappjetpackcompose.itemview.ImageItemView
import com.example.collegeappjetpackcompose.models.GalleryModel
import com.example.collegeappjetpackcompose.models.NoticeModel
import com.example.collegeappjetpackcompose.ui.theme.SkyBlue
import com.example.collegeappjetpackcompose.ui.theme.TITLE_SIZE
import com.example.collegeappjetpackcompose.utils.Constant.isAdmin


@Composable
fun GalleryItemView(galleryModel: GalleryModel,
                   delete:(galleryModel: GalleryModel)->Unit,
                    deleteImage:(cat:String,image:String)->Unit,
                    ){

    OutlinedCard(modifier = Modifier.padding(4.dp).fillMaxWidth())
     {
         ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
             val (category, delete) = createRefs()
             Text(
                 text = galleryModel.category!!,
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
             
             if(isAdmin)
             Card(modifier = Modifier
                 .constrainAs(delete) {
                     top.linkTo(parent.top)
                     end.linkTo(parent.end)
                 }
                 .padding(4.dp)
                 .clickable {
                     delete(galleryModel)
                 }


             ) {
                 Image(painter = painterResource(id = R.drawable.delete),
                     contentDescription = null,
                     modifier = Modifier.padding(8.dp)
                 )
             }
         }
         LazyRow()
         {
             items(galleryModel.images?:emptyList()){
                 ImageItemView (imageUrl=it,cat=galleryModel.category!!,delete={
                     cat:String,imageUrl->
                     deleteImage(cat,imageUrl)
                 })
             }
         }
    }
}
