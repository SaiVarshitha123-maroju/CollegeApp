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
import com.example.collegeappjetpackcompose.models.AssignModel

class AssignViewModel: ViewModel() {

    private val noticeRef = Firebase.firestore.collection("assignments")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    private val _noticeList = MutableLiveData<List<AssignModel>>()
    val noticeList: LiveData<List<AssignModel>> = _noticeList

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dgzmk54lv",
            "api_key" to "538971727338748",
            "api_secret" to "_JBj2M9zFwPSktX9KxNTOsqiD2c",
            "secure" to true
        )
    )

    fun saveAssignment(inputStream: InputStream?, title: String) {
        if (inputStream == null || title.isEmpty()) {
            _isPosted.postValue(false)
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val uploadResult = cloudinary.uploader().upload(
                    inputStream,
                    ObjectUtils.asMap(
                        "resource_type", "raw",
                        "folder", "assignments/",
                        "public_id", title,
                        "secure", true
                    )
                )

                val fileUrl = uploadResult["secure_url"] as String
                val docId = UUID.randomUUID().toString()
                uploadAssignment(fileUrl, docId, title)
            } catch (e: Exception) {
                e.printStackTrace()
                _isPosted.postValue(false)
            }
        }
    }

    private fun uploadAssignment(fileUrl: String, docId: String, title: String) {
        val map = mapOf(
            "fileUrl" to fileUrl,
            "fileTitle" to title,
            "docId" to docId
        )

        noticeRef.document(docId).set(map)
            .addOnSuccessListener { _isPosted.postValue(true)
                getAssignments() }
            .addOnFailureListener { _isPosted.postValue(false) }
    }

    fun getAssignments() {
        noticeRef.get().addOnSuccessListener { querySnapshot ->
            val list = querySnapshot.map { doc -> doc.toObject(AssignModel::class.java) }
            _noticeList.postValue(list)
        }
    }

    fun deleteAssignment(noticeModel: AssignModel) {
        noticeRef.document(noticeModel.docId ?: return).delete()
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val uri = Uri.parse(noticeModel.fileUrl)
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