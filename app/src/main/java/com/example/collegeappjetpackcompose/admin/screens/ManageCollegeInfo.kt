package com.example.collegeappjetpackcompose.admin.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.collegeappjetpackcompose.R
import com.example.collegeappjetpackcompose.ui.theme.Purple40
import com.example.collegeappjetpackcompose.utils.Constant
import com.example.collegeappjetpackcompose.viewmodel.CollegeInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCollegeInfo(navController: NavController) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val collegeInfoViewModel : CollegeInfoViewModel = viewModel()

    val isUploaded by collegeInfoViewModel.isPosted.observeAsState(false)

    val collegeInfo by collegeInfoViewModel.collegeInfo.observeAsState(null)


   //collegeInfoViewModel.getCollegeInfo()

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var title by remember{
        mutableStateOf("")
    }
    var imageUrl by remember{
        mutableStateOf("")
    }

    var desc by remember{
        mutableStateOf("")
    }
    var address by remember{
        mutableStateOf("")
    }
    var link by remember{
        mutableStateOf("")
    }
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    LaunchedEffect(isUploaded) {
        if (isUploaded){
            Toast.makeText(context,"College Info Updated", Toast.LENGTH_SHORT).show()
            imageUri = null
        }
    }
    LaunchedEffect(collegeInfo) {
        collegeInfo?.let {
            if (it.isNotEmpty()) {
                title = it[0].name ?: ""
                desc = it[0].desc ?: ""
                address = it[0].address ?: ""
                link = it[0].websiteLink ?: ""
                imageUrl= it[0].imageUrl ?: ""
            }
        }
    }


    Scaffold(

        topBar = {
            TopAppBar(title = {
                Text(text = "Manage School Info",
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
        }

    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

                ElevatedCard(modifier = Modifier.padding(8.dp)) {

                    OutlinedTextField(value = title, onValueChange = {
                        title = it
                    },
                        placeholder = {
                            Text(text = "School Name")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)

                    )
                    OutlinedTextField(value = desc, onValueChange = {
                        desc = it
                    },
                        placeholder = {
                            Text(text = "School Description")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)

                    )
                    OutlinedTextField(value = address, onValueChange = {
                        address = it
                    },
                        placeholder = {
                            Text(text = "School Address")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)

                    )

                    OutlinedTextField(value = link, onValueChange = {
                        link = it
                    },
                        placeholder = {
                            Text(text = "School Website link")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)

                    )
                    Image(
                        painter = if(imageUrl!="") rememberAsyncImagePainter(model = imageUrl) else if (imageUri == null) painterResource(id = R.drawable.image_placeholder)
                        else rememberAsyncImagePainter(model = imageUri),

                        contentDescription = Constant.BANNER,
                        modifier = Modifier
                            .height(220.dp)
                            .fillMaxWidth()
                            .clickable {
                                launcher.launch("image/*")
                            },
                        contentScale = ContentScale.Crop
                    )

                    Row{
                        Button(onClick = {
                            if(title == ""|| desc==""||address==""||link==""){
                                Toast.makeText(context,"please provide All Fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else if(imageUrl != "" ){
                                collegeInfoViewModel.uploadImage(imageUrl, title,address,desc, link)
                            }else if (imageUri == null){
                                Toast.makeText(context,"please provide image",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else {
                                collegeInfoViewModel.saveImage(
                                    inputStream = {
                                        imageUri?.let {
                                            contentResolver.openInputStream(
                                                it
                                            )
                                        }
                                    },
                                    name = title,
                                    address = address,
                                    desc = desc,
                                    websiteLink = link
                                )
                            }
                        },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(4.dp)

                        ) {
                            Text(text = "Update School Info")
                        }
                    }
                }
        }
    }
}
