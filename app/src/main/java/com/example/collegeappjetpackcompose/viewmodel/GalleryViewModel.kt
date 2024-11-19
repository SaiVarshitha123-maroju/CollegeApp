package com.example.collegeappjetpackcompose.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.collegeappjetpackcompose.models.GalleryModel
import com.example.collegeappjetpackcompose.utils.Constant.GALLERY
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
//import com.google.firebase.storage.storage
import com.cloudinary.Cloudinary
import java.io.InputStream
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel: ViewModel() {

    private val galleryRef = Firebase.firestore.collection(GALLERY)

    //private  val storageRef = Firebase.storage.reference

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted : LiveData<Boolean> = _isPosted

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted : LiveData<Boolean> = _isDeleted

    private val _galleryList = MutableLiveData<List<GalleryModel>>()
    val galleryList : LiveData<List<GalleryModel>> = _galleryList

    private val cloudinary: Cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dgzmk54lv",
            "api_key" to "538971727338748",
            "api_secret" to "_JBj2M9zFwPSktX9KxNTOsqiD2c"
        )
    )

    fun saveGalleryImage(inputStream: InputStream?, category: String, isCategory: Boolean) {
        _isPosted.postValue(false)
        if (inputStream == null) {
            _isPosted.postValue(false)
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val uploadResult = cloudinary.uploader().upload(
                    inputStream,
                    ObjectUtils.asMap("folder", "gallery/")
                )
                val imageUrl = uploadResult["url"] as String
                if (isCategory) {
                    uploadCategory(imageUrl, category)
                } else {
                    updateImage(imageUrl, category)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _isPosted.postValue(false)
            }
        }
    }

    private fun uploadCategory(image:String,category: String) {

        val map = mutableMapOf<String, Any>()
        map["category"] = category
        map["images"]= FieldValue.arrayUnion(image)

        galleryRef.document(category).set(map)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener{
                _isPosted.postValue(false)
            }
    }


    private fun updateImage(image:String,category: String) {


        galleryRef.document(category).update("images", FieldValue.arrayUnion(image))
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener{
                _isPosted.postValue(false)
            }

    }


    fun getGallery() {
        galleryRef.get().addOnSuccessListener { querySnapshot ->
            val list = querySnapshot.map { doc -> doc.toObject(GalleryModel::class.java) }
            _galleryList.postValue(list)
        }
    }

    fun deleteGallery(galleryModel: GalleryModel) {
        if (galleryModel.category.isNullOrEmpty() || galleryModel.images.isNullOrEmpty()) {
            _isDeleted.postValue(false)
            return
        }

        galleryRef.document(galleryModel.category).delete()
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        galleryModel.images.forEach { imageUrl ->
                            val uri = Uri.parse(imageUrl)
                            val publicId = uri.lastPathSegment?.substringBefore(".") ?: ""
                            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap())
                        }
                        _isDeleted.postValue(true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _isDeleted.postValue(false)
                    }
                }
            }
            .addOnFailureListener { _isDeleted.postValue(false) }
    }

    fun deleteImage(category: String, image: String) {
        galleryRef.document(category).update("images", FieldValue.arrayRemove(image))
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val uri = Uri.parse(image)
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