package com.example.collegeappjetpackcompose.screens

import android.widget.Gallery
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collegeappjetpackcompose.itemview.AssignItemView
import com.example.collegeappjetpackcompose.itemview.GalleryItemView
import com.example.collegeappjetpackcompose.viewmodel.AssignViewModel
import com.example.collegeappjetpackcompose.viewmodel.GalleryViewModel

@Composable
fun Assign() {

    // ViewModel for handling assignments
    val assignViewModel: AssignViewModel = viewModel()
    val assignmentList by assignViewModel.noticeList.observeAsState(emptyList()) // Get assignments list
    assignViewModel.getAssignments() // Fetch assignments

    // LazyColumn to display assignments
    LazyColumn {
        items(assignmentList) { assignment ->
            AssignItemView(assignment, delete = {
                // Delete the assignment when called
                assignViewModel.deleteAssignment(assignment)
            })
        }
    }
}
