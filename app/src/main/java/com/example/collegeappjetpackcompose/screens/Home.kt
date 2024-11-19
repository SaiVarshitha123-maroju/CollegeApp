package com.example.collegeappjetpackcompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Home() {
    val bannerViewModel : BannerViewModel = viewModel()
    val bannerList by bannerViewModel.bannerList.observeAsState(null)
    val collegeInfoViewModel : CollegeInfoViewModel = viewModel()
    bannerViewModel.getBanner()

    val collegeInfo by collegeInfoViewModel.collegeInfo.observeAsState(null)
    //collegeInfoViewModel.getCollegeInfo()

    val noticeViewModel : NoticeViewModel = viewModel()
    val noticeList by noticeViewModel.noticeList.observeAsState(null)
    noticeViewModel.getNotice()

    val pagerState= com.google.accompanist.pager.rememberPagerState(initialPage = 0)

    val imageSlider= ArrayList<AsyncImagePainter>()
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
        item{
            if(collegeInfo!=null){
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
                Text(text = "Notices",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
        items(noticeList?: emptyList()){
            NoticeItemView(it, delete = { docId ->
                noticeViewModel.deleteNotice(docId)
            })
        }
    }
}