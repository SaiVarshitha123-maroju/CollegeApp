package com.example.collegeappjetpackcompose.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID
import com.example.collegeappjetpackcompose.models.NoticeModel
import com.example.collegeappjetpackcompose.utils.Constant.NOTICE
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java. time. LocalDate
import java. time. format. DateTimeFormatter

class NoticeViewModel: ViewModel() {

    private val noticeRef = Firebase.firestore.collection(NOTICE)

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted : LiveData<Boolean> = _isPosted

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted : LiveData<Boolean> = _isDeleted

    private val _noticeList = MutableLiveData<List<NoticeModel>>()
    val noticeList : LiveData<List<NoticeModel>> = _noticeList

    private val cloudinary: Cloudinary = Cloudinary(
        mapOf<String, Any>(
            "cloud_name" to "dgzmk54lv",
            "api_key" to "538971727338748",
            "api_secret" to "_JBj2M9zFwPSktX9KxNTOsqiD2c",
            "secure" to true
        )
    )

    fun saveNotice(inputStream: InputStream?, title: String, link: String,date:String,time:String,venue:String) {
        _isPosted.postValue(false)
        if (inputStream == null) {
            _isPosted.postValue(false)
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val uploadResult = cloudinary.uploader().upload(
                    inputStream,
                    ObjectUtils.asMap("folder", "notices/",
                        "secure", true )
                )

                var imageUrl = uploadResult["url"] as String
                imageUrl = imageUrl.replace("http://", "https://")
                val randomUid = UUID.randomUUID().toString()
                uploadNotice(imageUrl, randomUid, title, link,date,time,venue)
            } catch (e: Exception) {
                e.printStackTrace()
                _isPosted.postValue(false)
            }
        }
    }

    private fun uploadNotice(imageUrl: String, docId: String, title: String, link: String,date: String,time: String,venue: String) {
        val map = mutableMapOf(
            "imageUrl" to imageUrl,
            "docId" to docId,
            "title" to title,
            "link" to link,
            "date" to date,
            "time" to time,
            "venue" to venue
        )

        noticeRef.document(docId).set(map)
            .addOnSuccessListener { _isPosted.postValue(true) }
            .addOnFailureListener { _isPosted.postValue(false) }
    }

    fun getNotice() {
        noticeRef.get().addOnSuccessListener { querySnapshot ->
            val today = LocalDate.now()

            querySnapshot.forEach { doc ->
                val notice = doc.toObject(NoticeModel::class.java)
                val noticeDate = try {
                    LocalDate.parse(notice.date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                } catch (e: Exception) {
                    null
                }

                if (noticeDate != null && noticeDate.isBefore(today)) {
                    // Delete outdated notice from the database
                    deleteNotice(notice)
                } else {
                    // Add valid notice to the live data list
                    _noticeList.postValue(
                        querySnapshot.mapNotNull { validDoc ->
                            val validNotice = validDoc.toObject(NoticeModel::class.java)
                            val validNoticeDate = try {
                                LocalDate.parse(validNotice.date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                            } catch (e: Exception) {
                                null
                            }
                            if (validNoticeDate == null || validNoticeDate.isBefore(today)) null else validNotice
                        }
                    )
                }
            }
        }
    }


    fun deleteNotice(noticeModel: NoticeModel) {
        if (noticeModel.docId == null || noticeModel.imageUrl == null) {
            _isDeleted.postValue(false)
            return
        }
        noticeRef.document(noticeModel.docId).delete()
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val uri = Uri.parse(noticeModel.imageUrl)
                        val publicId = uri.lastPathSegment?.substringBefore(".") ?: ""
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap())
                        _isDeleted.postValue(true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _isDeleted.postValue(false)
                    }
                }
            }
            .addOnFailureListener { _isDeleted.postValue(false) }
    }


}