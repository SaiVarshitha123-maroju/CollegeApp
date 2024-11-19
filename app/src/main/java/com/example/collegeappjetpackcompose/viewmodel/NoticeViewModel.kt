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
import com.google.firebase.storage.storage
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class NoticeViewModel: ViewModel() {

    private val noticeRef = Firebase.firestore.collection(NOTICE)

    private  val storageRef = Firebase.storage.reference

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

    fun saveNotice(inputStream: InputStream?, title: String, link: String) {
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

                val imageUrl = uploadResult["url"] as String
                val randomUid = UUID.randomUUID().toString()
                uploadNotice(imageUrl, randomUid, title, link)
            } catch (e: Exception) {
                e.printStackTrace()
                _isPosted.postValue(false)
            }
        }
    }

    private fun uploadNotice(imageUrl: String, docId: String, title: String, link: String) {
        val map = mutableMapOf(
            "imageUrl" to imageUrl,
            "docId" to docId,
            "title" to title,
            "link" to link
        )

        noticeRef.document(docId).set(map)
            .addOnSuccessListener { _isPosted.postValue(true) }
            .addOnFailureListener { _isPosted.postValue(false) }
    }

    fun getNotice() {
        noticeRef.get().addOnSuccessListener { querySnapshot ->
            val list = querySnapshot.map { doc -> doc.toObject(NoticeModel::class.java) }
            _noticeList.postValue(list)
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