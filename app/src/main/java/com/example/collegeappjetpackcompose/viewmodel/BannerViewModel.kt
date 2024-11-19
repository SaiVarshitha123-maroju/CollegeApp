package com.example.collegeappjetpackcompose.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.identity.util.UUID
import com.example.collegeappjetpackcompose.models.BannerModel
import com.example.collegeappjetpackcompose.utils.Constant.BANNER
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
//import com.google.firebase.storage.storage
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.InputStream

class BannerViewModel: ViewModel() {

    private val bannerRef = Firebase.firestore.collection(BANNER)

  //  private  val storageRef = Firebase.storage.reference

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted : LiveData<Boolean> = _isPosted

    private val cloudinary: Cloudinary = Cloudinary(
        mapOf<String, String>(
            "cloud_name" to "dgzmk54lv",
            "api_key" to "538971727338748",
            "api_secret" to "_JBj2M9zFwPSktX9KxNTOsqiD2c"
        )
    )

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted : LiveData<Boolean> = _isDeleted

    private val _bannerList = MutableLiveData<List<BannerModel>>()
    val bannerList : LiveData<List<BannerModel>> = _bannerList

    fun saveImage(inputStream: InputStream) {
        _isPosted.postValue(false)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Upload to Cloudinary
                val uploadResult = cloudinary.uploader().upload(
                    inputStream,
                    ObjectUtils.asMap("folder", "banners/")
                )

                val imageUrl = uploadResult["url"] as String
                val randomUid = UUID.randomUUID().toString()
                uploadImage(imageUrl, randomUid)
            } catch (e: Exception) {
                e.printStackTrace()
                _isPosted.postValue(false)
            }
        }
    }


    private fun uploadImage(imageUrl: String, docId: String) {
        val map = mutableMapOf<String, String>()
        map["url"] = imageUrl
        map["docId"] = docId

        bannerRef.document(docId).set(map)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener {
                _isPosted.postValue(false)
            }
    }

    fun getBanner() {
        bannerRef.get().addOnSuccessListener { querySnapshot ->
            val list = querySnapshot.map { doc -> doc.toObject(BannerModel::class.java) }
            _bannerList.postValue(list)
        }
    }

    fun deleteBanner(bannerModel: BannerModel) {
        if (bannerModel.docId == null || bannerModel.url == null) {
            _isDeleted.postValue(false)
            return
        }

        bannerRef.document(bannerModel.docId).delete()
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val uri = Uri.parse(bannerModel.url)
                        val publicId = uri.lastPathSegment?.substringBefore(".") ?: ""
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap())
                        _isDeleted.postValue(true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _isDeleted.postValue(false)
                    }
                }
            }
            .addOnFailureListener {
                _isDeleted.postValue(false)
            }
    }


}