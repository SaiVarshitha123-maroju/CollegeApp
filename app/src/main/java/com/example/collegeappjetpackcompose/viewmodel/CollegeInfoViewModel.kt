package com.example.collegeappjetpackcompose.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.Cloudinary
import com.example.collegeappjetpackcompose.models.CollegeInfoModel
import com.example.collegeappjetpackcompose.utils.Constant.COLLEGE_INFO
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
//import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import com.cloudinary.utils.ObjectUtils

class CollegeInfoViewModel: ViewModel() {

    private val collegeInfoRef = Firebase.firestore.collection(COLLEGE_INFO)

    //private  val storageRef = Firebase.storage.reference

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted : LiveData<Boolean> = _isPosted

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dgzmk54lv",
            "api_key" to "538971727338748",
            "api_secret" to "_JBj2M9zFwPSktX9KxNTOsqiD2c"
        )
    )

    private val _collegeInfo = MutableLiveData<List<CollegeInfoModel>>()
    val collegeInfo : LiveData<List<CollegeInfoModel>> = _collegeInfo

    fun saveImage(inputStream: () -> InputStream?, name: String, address: String, desc: String, websiteLink: String) {
        _isPosted.postValue(false)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val stream = inputStream() ?: return@launch
                val uploadResult = cloudinary.uploader().upload(
                    stream,
                    ObjectUtils.asMap("folder", "college_info/")
                )
                val imageUrl = uploadResult["url"] as String
                uploadImage(imageUrl, name, address, desc, websiteLink)
            } catch (e: Exception) {
                _isPosted.postValue(false)
            }
        }
    }

    fun uploadImage(imageUrl: String, name: String, address: String, desc: String, websiteLink: String) {
        val map = mutableMapOf<String, Any>()
        map["imageUrl"] = imageUrl
        map["websiteLink"] = websiteLink
        map["name"] = name
        map["address"] = address
        map["desc"] = desc

        collegeInfoRef.document("collegeDetails").set(map)
            .addOnSuccessListener { _isPosted.postValue(true) }
            .addOnFailureListener { e ->
                _isPosted.postValue(false)
                Log.e("Firestore Error", "Error uploading data", e)
            }
    }

    fun getCollegeInfo() {
        collegeInfoRef.document("collegeDetails").get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _collegeInfo.postValue(
                        listOf(
                            CollegeInfoModel(
                                document.data?.get("name")?.toString(),
                                document.data?.get("address")?.toString(),
                                document.data?.get("desc")?.toString(),
                                document.data?.get("websiteLink")?.toString(),
                                document.data?.get("imageUrl")?.toString()
                            )
                        )
                    )
                } else {
                    _collegeInfo.postValue(emptyList()) // Handle empty document
                }
            }
            .addOnFailureListener {
                _collegeInfo.postValue(emptyList()) // Handle failure
            }
    }


}