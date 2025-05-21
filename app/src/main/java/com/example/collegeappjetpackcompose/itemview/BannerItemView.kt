package com.example.collegeappjetpackcompose.itemview

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.crossfade
import coil3.request.ImageRequest
import com.example.collegeappjetpackcompose.R
import com.example.collegeappjetpackcompose.models.BannerModel

@Composable
fun BannerItemView(bannerModel: BannerModel,
                   delete: (docId: BannerModel) -> Unit) {

    OutlinedCard(modifier = Modifier.padding(4.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(bannerModel.url)
                        .crossfade(true) // Smooth transition
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            // Delete button
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        delete(bannerModel)
                    }
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BannerView() {
    // Example usage of BannerItemView with a dummy BannerModel
    BannerItemView(
        bannerModel = BannerModel(url = "https://res.cloudinary.com/dgzmk54lv/image/upload/v1734082463/banners/qd7bmyuj5bav2qwdizpj.jpg"),
        delete = { banner -> println("Deleted: ${banner.url}") }
    )
}
