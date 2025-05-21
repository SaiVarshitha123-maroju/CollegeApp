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
import android. widget. Toast
import android. content. Context
import android. util. Log

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
                var imageUrl = uploadResult["url"] as String
                imageUrl = imageUrl.replace("http://", "https://")
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

        map["timestamp"] = System.currentTimeMillis()

        galleryRef.document(category).set(map)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener{
                _isPosted.postValue(false)
            }
    }


    private fun updateImage(image: String, category: String) {
        val updates = mutableMapOf<String, Any>(
            "images" to FieldValue.arrayUnion(image),
            "timestamp" to System.currentTimeMillis() // Update the timestamp to the current time
        )

        galleryRef.document(category).update(updates)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener {
                _isPosted.postValue(false)
            }
    }



    fun getGallery() {
        galleryRef.get().addOnSuccessListener { querySnapshot ->
            val now = System.currentTimeMillis()
            val cutoff = now - 30 * 24 * 60 * 60 * 1000 // 30 days ago

            val validGalleries = mutableListOf<GalleryModel>()

            for (doc in querySnapshot.documents) {
                val gallery = doc.toObject(GalleryModel::class.java)
                if (gallery != null) {
                    Log.d("GalleryDebug", "Category: ${gallery.category}, Timestamp: ${gallery.timestamp}")
                        // Add valid galleries to the list
                        validGalleries.add(gallery)
                }
            }

            // Update the gallery list LiveData with valid galleries
            _galleryList.postValue(validGalleries)
        }
    }

    // Helper function to delete old galleries
    private fun deleteGalleryAutomatically(gallery: GalleryModel, documentId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                gallery.images?.forEach { imageUrl ->
                    val publicId = extractCloudinaryPublicId(imageUrl)
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        galleryRef.document(documentId).delete()
            .addOnSuccessListener {
                Log.d("GalleryViewModel", "Deleted old gallery: $documentId")
            }
            .addOnFailureListener { e ->
                Log.e("GalleryViewModel", "Failed to delete old gallery: $documentId", e)
            }
    }

    // Utility function to extract Cloudinary public ID from the image URL
    private fun extractCloudinaryPublicId(imageUrl: String): String {
        val urlSegments = imageUrl.split("/")
        val fileName = urlSegments.last()
        return fileName.substringBefore(".") // Extract public ID without the file extension
    }

    fun deleteGallery(galleryModel: GalleryModel, context: Context) {
        // Check if the gallery has images
        if (galleryModel.images!!.isNotEmpty()) {
            // Show a toast to inform the user
            Toast.makeText(
                context,
                "Please delete all images from the gallery before deleting the gallery.",
                Toast.LENGTH_LONG
            ).show()
            _isDeleted.postValue(false)
            return
        }
        if (galleryModel.images.isNullOrEmpty()) {
            galleryRef.document(galleryModel.category.toString()).delete()
                .addOnSuccessListener {
                    _isDeleted.postValue(true)
                    Toast.makeText(context, "Gallery deleted successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    _isDeleted.postValue(false)
                    Toast.makeText(context, "Failed to delete gallery.", Toast.LENGTH_SHORT).show()
                }
        }



        // Proceed to delete the gallery if no images are present

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