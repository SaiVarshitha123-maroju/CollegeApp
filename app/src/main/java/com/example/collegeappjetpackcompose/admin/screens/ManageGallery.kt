package com.example.collegeappjetpackcompose.admin.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.collegeappjetpackcompose.R
import com.example.collegeappjetpackcompose.itemview.GalleryItemView
import com.example.collegeappjetpackcompose.ui.theme.Purple40
import com.example.collegeappjetpackcompose.viewmodel.GalleryViewModel
import kotlin.collections.isNotEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageGallery(navController: NavController) {
    val context = LocalContext.current

    val galleryViewModel : GalleryViewModel = viewModel()

    val isUploaded by galleryViewModel.isPosted.observeAsState(false)
    val isDeleted by galleryViewModel.isDeleted.observeAsState(false)
    val galleryList by galleryViewModel.galleryList.observeAsState(null)

    val option=arrayListOf<String>()
    galleryViewModel.getGallery()

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var isImage by remember {
        mutableStateOf(false)
    }
    var isCategory by remember {
        mutableStateOf(false)
    }
    var mExpanded by remember {
        mutableStateOf(false)
    }


    var category by remember{
        mutableStateOf("")
    }


    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) {

        imageUri = it

    }

    LaunchedEffect(isUploaded) {
        if (isUploaded){
            Toast.makeText(context,"Data Added", Toast.LENGTH_SHORT).show()
            imageUri = null
            isCategory = false
            category=""


        }
    }

    LaunchedEffect(isDeleted) {
        if (isDeleted){
            Toast.makeText(context,"Data Deleted", Toast.LENGTH_SHORT).show()
        }
    }




    Scaffold(

        topBar = {
            TopAppBar(title = {
                Text(text = "Manage Events",
                    color = Color.White
                )
            },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Purple40),

                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack, contentDescription = null,
                            tint = Color.White
                        )


                    }
                },
            )
        },


        ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            Row(modifier = Modifier.padding(8.dp)){
                Card(modifier= Modifier.weight(1f).padding(4.dp).clickable {
                    isCategory = true
                    isImage=false
                }){
                    Text(text="Add an Event",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Card(modifier= Modifier.weight(1f).padding(4.dp).clickable {
                    isImage = true
                    isCategory = false
                }) {
                    Text(text="Add Images",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            if(isCategory)
                ElevatedCard(modifier = Modifier.padding(8.dp)) {

                    OutlinedTextField(value = category, onValueChange = {
                        category = it
                    },
                        placeholder = {
                            Text(text = "Event Name...")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)

                    )
                    Image(
                        painter = if (imageUri == null) painterResource(id = R.drawable.image_placeholder)
                        else rememberAsyncImagePainter(model = imageUri),

                        contentDescription = "banner_image",
                        modifier = Modifier
                            .height(220.dp)
                            .fillMaxWidth()
                            .clickable {
                                launcher.launch("image/*")
                            },
                        contentScale = ContentScale.Crop
                    )
                    Row{
                        Button(onClick ={
                            if (imageUri == null) {
                                Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                            } else if (category.isEmpty()) {
                                Toast.makeText(context, "Please provide a category name", Toast.LENGTH_SHORT).show()
                            } else {
                                val inputStream = context.contentResolver.openInputStream(imageUri!!)
                                galleryViewModel.saveGalleryImage(inputStream, category, true)
                            }
                        },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(4.dp)

                        ) {
                            Text(text = "Add Event")

                        }

                        OutlinedButton(onClick = { imageUri = null
                            isCategory = false
                            isImage=false},
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(4.dp)

                        ) {
                            Text(text = "Cancel")

                        }
                    }
                }
            if(isImage)
                ElevatedCard(modifier = Modifier.padding(8.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                            OutlinedTextField(value = category, onValueChange = {
                                category = it
                            },
                                readOnly = true,
                                placeholder = {
                                    Text(text = "Select your Gallery...")
                                },
                                label = { Text(text = "Select your Event Name") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = mExpanded)
                                }

                            )
                            DropdownMenu(
                                expanded = mExpanded,
                                onDismissRequest = { mExpanded = false }) {

                                if (galleryList!= null && galleryList!!.isNotEmpty()) {
                                    option.clear()
                                    for (data in galleryList!!) {
                                        option.add(data.category!!)
                                    }
                                }
                                option.forEach { selectedOption ->
                                    DropdownMenuItem(text = { Text(text = selectedOption) }, onClick = {
                                        category = selectedOption
                                        mExpanded = false
                                    },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            Spacer(
                                modifier = Modifier.matchParentSize().padding(10.dp)
                                    .clickable { mExpanded = !mExpanded })
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    Image(
                        painter = if (imageUri == null) painterResource(id = R.drawable.image_placeholder)
                        else rememberAsyncImagePainter(model = imageUri),

                        contentDescription = "banner_image",
                        modifier = Modifier
                            .height(220.dp)
                            .fillMaxWidth()
                            .clickable {
                                launcher.launch("image/*")
                            },
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Row{
                        Button( onClick ={
                                if (imageUri == null) {
                                    Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                                } else if (category.isEmpty()) {
                                    Toast.makeText(context, "Please provide a category name", Toast.LENGTH_SHORT).show()
                                } else {
                                    val inputStream = context.contentResolver.openInputStream(imageUri!!)
                                    galleryViewModel.saveGalleryImage(inputStream, category, false)
                                }
                        },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(4.dp)

                        ) {
                            Text(text = "Add Image")

                        }

                        OutlinedButton(onClick = { imageUri = null
                            isCategory = false
                            isImage=false},
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(4.dp)

                        ) {
                            Text(text = "Cancel")

                        }
                    }
                }


            LazyColumn {
                items(galleryList?: emptyList()){
                    GalleryItemView (it, delete = { docId ->
                        galleryViewModel.deleteGallery(docId, context )
                    }, deleteImage = {cat,imageUrl->
                        galleryViewModel.deleteImage(cat,imageUrl)
                    })
                }
            }
        }
    }
}