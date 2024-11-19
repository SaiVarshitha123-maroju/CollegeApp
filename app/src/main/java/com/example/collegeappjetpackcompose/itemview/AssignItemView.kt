package com.example.collegeappjetpackcompose.itemview

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import com.example.collegeappjetpackcompose.utils.Constant.isAdmin
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.collegeappjetpackcompose.R
import com.example.collegeappjetpackcompose.models.AssignModel
import com.example.collegeappjetpackcompose.ui.theme.SkyBlue

@Composable
fun AssignItemView(
    noticeModel: AssignModel,
    delete: (noticeModel: AssignModel) -> Unit
) {
    val context = LocalContext.current

    OutlinedCard(modifier = Modifier.padding(4.dp)) {
        ConstraintLayout {
            val (title, link, deleteBtn) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .padding(12.dp)
            ) {
                Text(
                    text = noticeModel.fileTitle ?: "No Title",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (!noticeModel.fileUrl.isNullOrEmpty()) {
                    Text(
                        text = "Download/View File",
                        color = SkyBlue,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clickable {
                                val uri = Uri.parse(noticeModel.fileUrl)
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(intent)
                            }
                            .padding(vertical = 4.dp)
                    )
                } else {
                    Text(
                        text = "No file attached",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            if (isAdmin) { // Check admin status dynamically
                Card(
                    modifier = Modifier
                        .constrainAs(deleteBtn) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .padding(8.dp)
                        .clickable {
                            delete(noticeModel)
                        }
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
}
