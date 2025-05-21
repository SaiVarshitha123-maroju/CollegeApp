package com.example.collegeappjetpackcompose.screens

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.collegeappjetpackcompose.itemview.NoticeItemView
import com.example.collegeappjetpackcompose.ui.theme.SkyBlue
import com.example.collegeappjetpackcompose.viewmodel.BannerViewModel
import com.example.collegeappjetpackcompose.viewmodel.CollegeInfoViewModel
import com.example.collegeappjetpackcompose.viewmodel.NoticeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.style.TextOverflow
import com.example.collegeappjetpackcompose.models.NoticeModel // Adjust the import based on where Notice is defined
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.draw.clip
import androidx.compose.material3.ButtonDefaults

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Home() {
    val bannerViewModel : BannerViewModel = viewModel()
    val bannerList by bannerViewModel.bannerList.observeAsState(null)
    val collegeInfoViewModel : CollegeInfoViewModel = viewModel()
    bannerViewModel.getBanner()

    val collegeInfo by collegeInfoViewModel.collegeInfo.observeAsState(null)
    LaunchedEffect(Unit) {
        collegeInfoViewModel.getCollegeInfo()
    }

    val noticeViewModel : NoticeViewModel = viewModel()
    val noticeList by noticeViewModel.noticeList.observeAsState(null)
    noticeViewModel.getNotice()

    val pagerState= com.google.accompanist.pager.rememberPagerState(initialPage = 0)

    val imageSlider= ArrayList<AsyncImagePainter>()
    var showDialog = remember { mutableStateOf(false) }
    var selectedNotice = remember{ mutableStateOf<NoticeModel?>(null) }
    if(bannerList!=null) {
        bannerList!!.forEach {
            imageSlider.add(rememberAsyncImagePainter(model=it.url))
        }
    }
    LaunchedEffect(Unit) {
        try{
            while (true){
                yield()
                delay(2600)
                pagerState.animateScrollToPage(page=(pagerState.currentPage+1)%pagerState.pageCount)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    LazyColumn(modifier = Modifier.padding(8.dp)) {
        item{
            HorizontalPager(count = imageSlider.size, state = pagerState) {
                pager->
                Card(modifier = Modifier.height(220.dp)) {
                    Image(painter =  imageSlider[pager], contentDescription = "Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(220.dp).fillMaxWidth())
                }
            }
        }
        item{
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                HorizontalPagerIndicator(pagerState=pagerState,
                    modifier = Modifier.padding(8.dp))
            }
        }
        items(noticeList?: emptyList()){ notice ->
            NoticeItemView(notice = notice, onClick = { selectedNotice.value = notice; showDialog.value = true })
        }
    }
    if (showDialog.value && selectedNotice.value != null) {
        FullScreenDialog(
            notice = selectedNotice.value!!,
            onDismiss = { showDialog.value = false }
        )
    }
}
@Composable
fun NoticeItemView(notice: NoticeModel, onClick: (NoticeModel) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick(notice) }
        .padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp).heightIn(min = 200.dp, max = 400.dp)) {
            Text(text = notice.title ?: "No Title", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = notice.link ?: "No Description", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            notice.imageUrl?.let {
                Image(painter = rememberAsyncImagePainter(it), contentDescription = "Notice Image")
            } ?: run {
                // You can display a default image if the URL is null
                Image(painter = painterResource(id = R.drawable.ic_menu_crop
                ), contentDescription = "Default Image")
            }
            //Text(text = notice.date, style = MaterialTheme.typography.body2)
            Text(text = "More details", fontSize = 14.sp, color = SkyBlue)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenDialog(notice: NoticeModel, onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f) // Increased horizontal space
                .wrapContentHeight() // Adjust vertical height to content
                .padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(16.dp) // Softer corners for a modern look
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Title
                Text(
                    text = notice.title ?: "No Title",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Description
                Text(
                    text = notice.link ?: "No Description",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text="Event Date: ${notice.date?.toString() ?: "No date mentioned"}",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text="Event Time: ${notice.time?.toString() ?: "No Time mentioned"}",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Event venue: ${notice.venue?.toString() ?: "No Venue mentioned"}",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Image
                notice.imageUrl?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Notice Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp)) // Rounded edges for the image
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    Image(
                        painter = painterResource(id = R.drawable.ic_menu_crop),
                        contentDescription = "Default Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Action Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(50), // Pill-shaped button
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "Close", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}
