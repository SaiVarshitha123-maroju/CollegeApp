package com.example.collegeappjetpackcompose.admin.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.collegeappjetpackcompose.R
import com.example.collegeappjetpackcompose.itemview.NoticeItemView
import com.example.collegeappjetpackcompose.ui.theme.Purple40
import com.example.collegeappjetpackcompose.viewmodel.NoticeViewModel
import java.time.LocalDate
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageNotice(navController: NavController) {
    val context = LocalContext.current
    val noticeViewModel: NoticeViewModel = viewModel()

    val isUploaded by noticeViewModel.isPosted.observeAsState(false)
    val isDeleted by noticeViewModel.isDeleted.observeAsState(false)
    val bannerList by noticeViewModel.noticeList.observeAsState(null)

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isNotice by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var venue by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        imageUri = it
    }
    noticeViewModel.getNotice()
    fun resetFields() {
        imageUri = null
        title = ""
        date = ""
        time = ""
        venue = ""
        link = ""
        isNotice = false
    }

    LaunchedEffect(isUploaded) {
        if (isUploaded) {
            Toast.makeText(context, "Notice Uploaded", Toast.LENGTH_SHORT).show()
            resetFields()
        }
    }


    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            Toast.makeText(context, "Notice Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Manage Notice", color = Color.White) },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Purple40),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { isNotice = true }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { padding ->
        // Use LazyColumn for scrollable content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp)
        ) {
            if (isNotice) {
                item {
                    ElevatedCard(modifier = Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text(text = "Notice Title") },
                            modifier = Modifier.fillMaxWidth().padding(4.dp)
                        )
                        OutlinedTextField(
                            value = link,
                            onValueChange = { link = it },
                            placeholder = { Text(text = "Notice Description") },
                            modifier = Modifier.fillMaxWidth().padding(4.dp)
                        )
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            placeholder = { Text(text = "Event Date (DD-MM-YYYY)") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(4.dp)
                        )
                        OutlinedTextField(
                            value = time,
                            onValueChange = { time = it },
                            placeholder = { Text(text = "Event Time (HH.MM)") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(4.dp)
                        )
                        OutlinedTextField(
                            value = venue,
                            onValueChange = { venue = it },
                            placeholder = { Text(text = "Venue of Event") },
                            modifier = Modifier.fillMaxWidth().padding(4.dp)
                        )
                        Image(
                            painter = if (imageUri == null) painterResource(id = R.drawable.image_placeholder)
                            else rememberAsyncImagePainter(model = imageUri),
                            contentDescription = "Notice Banner",
                            modifier = Modifier
                                .height(220.dp)
                                .fillMaxWidth()
                                .clickable { launcher.launch("image/*") },
                            contentScale = ContentScale.Crop
                        )
                        Row {
                            Button(
                                onClick = {
                                    if (validateFields(context, title, link, date, time, imageUri)) {
                                        val inputStream = context.contentResolver.openInputStream(imageUri!!)
                                        noticeViewModel.saveNotice(inputStream, title, link, date, time, venue)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(4.dp)
                            ) {
                                Text(text = "Add Notice")
                            }
                            OutlinedButton(
                                onClick = { resetFields() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(4.dp)
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    }
                }
            }
            if (bannerList.isNullOrEmpty()) {
                item {
                    Text(text = "No notices available", modifier = Modifier.padding(16.dp))
                }
            } else {
                items(bannerList ?: emptyList()) { notice ->
                    NoticeItemView(notice, delete = { docId ->
                        noticeViewModel.deleteNotice(docId)
                    })
                }
            }
        }
    }

}

private fun validateFields(context: Context, title: String, link: String, date: String, time: String, imageUri: Uri?): Boolean {
    // Regex patterns for validation
    val datePattern = Regex("\\d{2}-\\d{2}-\\d{4}")
    val timePattern = Regex("\\d{2}.\\d{2}")

    // Validate image
    if (imageUri == null) {
        Toast.makeText(context, "Please provide an image", Toast.LENGTH_SHORT).show()
        return false
    }

    // Validate title
    if (title.isEmpty()) {
        Toast.makeText(context, "Please provide a title", Toast.LENGTH_SHORT).show()
        return false
    }
    if (link.isEmpty()) {
        Toast.makeText(context, "Please provide description", Toast.LENGTH_SHORT).show()
        return false
    }

    // Validate date
    if (date.isEmpty() || !date.matches(datePattern)) {
        Toast.makeText(context, "Please provide a valid date in DD-MM-YYYY format", Toast.LENGTH_SHORT).show()
        return false
    } else {
        val (day, month, year) = date.split("-").map { it.toInt() }

        // Validate month and day range
        if (month !in 1..12) {
            Toast.makeText(context, "Please provide a valid month (1-12)", Toast.LENGTH_SHORT).show()
            return false
        }
        if (day !in 1..31) {
            Toast.makeText(context, "Please provide a valid day (1-31)", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check for valid day range in the given month
        val daysInMonth = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            else -> 0
        }
        if (day > daysInMonth) {
            Toast.makeText(context, "Invalid day for the given month", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate that the date is not in the past
        val enteredDate = LocalDate.of(year, month, day)
        val today = LocalDate.now()
        if (enteredDate.isBefore(today)) {
            Toast.makeText(context, "Notice date cannot be in the past", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    // Validate time
    if (time.isEmpty() || !time.matches(timePattern)) {
        Toast.makeText(context, "Please provide a valid time in HH:MM format", Toast.LENGTH_SHORT).show()
        return false
    } else {
        val (hours, minutes) = time.split(".").map { it.toInt() }
        if (hours !in 0..23 || minutes !in 0..59) {
            Toast.makeText(context, "Please provide a valid time (HH:MM)", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    return true
}


@Preview(showSystemUi = true)
@Composable
fun NoticeView() {
    ManageNotice(navController = rememberNavController())
}
