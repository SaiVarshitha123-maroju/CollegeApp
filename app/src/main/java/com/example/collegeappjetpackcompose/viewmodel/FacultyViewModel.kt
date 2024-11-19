package com.example.collegeappjetpackcompose.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.identity.util.UUID
import com.cloudinary.utils.ObjectUtils
import com.example.collegeappjetpackcompose.models.FacultyModel
import com.cloudinary.Cloudinary
import com.example.collegeappjetpackcompose.utils.Constant.FACULTY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
//import com.google.firebase.storage.storage
import java.io.InputStream

class FacultyViewModel: ViewModel() {

    private val facultyRef = Firebase.firestore.collection(FACULTY)

    //private  val storageRef = Firebase.storage.reference

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted : LiveData<Boolean> = _isPosted

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted : LiveData<Boolean> = _isDeleted

    private val _facultyList = MutableLiveData<List<FacultyModel>>()
    val facultyList : LiveData<List<FacultyModel>> = _facultyList

    private val _categoryList = MutableLiveData<List<String>>()
    val categoryList : LiveData<List<String>> = _categoryList

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dgzmk54lv",
            "api_key" to "538971727338748",
            "api_secret" to "_JBj2M9zFwPSktX9KxNTOsqiD2c"
        )
    )

    fun saveFaculty(
        inputStream: () -> InputStream?,
        name: String,
        email: String,
        position: String,
        catName: String
    ) {
        _isPosted.postValue(false)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val stream = inputStream() ?: return@launch
                val uploadResult = cloudinary.uploader().upload(
                    stream,
                    ObjectUtils.asMap("folder", "faculty/")
                )
                val imageUrl = uploadResult["url"] as String
                val randomUid = UUID.randomUUID().toString()
                uploadFaculty(imageUrl, randomUid, name, email, position, catName)
            } catch (e: Exception) {
                _isPosted.postValue(false)
            }
        }
    }

    private fun uploadFaculty(
        imageUrl: String,
        docId: String,
        name: String,
        email: String,
        position: String,
        catName: String
    ) {
        val map = mutableMapOf(
            "imageUrl" to imageUrl,
            "docId" to docId,
            "name" to name,
            "email" to email,
            "position" to position,
            "catName" to catName
        )

        facultyRef.document(catName).collection("teacher").document(docId).set(map)
            .addOnSuccessListener { _isPosted.postValue(true) }
            .addOnFailureListener { _isPosted.postValue(false) }
    }


    fun uploadCategory(category: String) {

        val map = mutableMapOf<String,String>()
        map["catName"] = category

        facultyRef.document(category).set(map)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener{
                _isPosted.postValue(false)
            }

    }
    fun getCategory(){
        facultyRef.get().addOnSuccessListener {
                val list = mutableListOf<String>()

            for (doc in it){
                list.add(doc.get("catName").toString())
            }
            _categoryList.postValue(list)

        }
    }

    fun getFaculty(catName: String){
        facultyRef.document(catName).collection("teacher").get().addOnSuccessListener {
            val list = mutableListOf<FacultyModel>()
            for (doc in it){
                list.add(doc.toObject(FacultyModel::class.java))
            }
            _facultyList.postValue(list)

        }
    }

    fun deleteFaculty(facultyModel: FacultyModel) {
        _isDeleted.postValue(false)
        if (facultyModel.docId.isNullOrEmpty() || facultyModel.imageUrl.isNullOrEmpty()) {
            _isDeleted.postValue(false)
            return
        }

        facultyRef.document(facultyModel.catName!!)
            .collection("teacher").document(facultyModel.docId).delete()
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val uri = Uri.parse(facultyModel.imageUrl)
                        val publicId = uri.lastPathSegment?.substringBefore(".") ?: ""
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap())
                        _isDeleted.postValue(true)
                    } catch (e: Exception) {
                        _isDeleted.postValue(false)
                    }
                }
            }
            .addOnFailureListener { _isDeleted.postValue(false) }
    }


    fun deleteCategory(category: String){
        facultyRef.document(category).delete()
            .addOnSuccessListener {
                _isDeleted.postValue(true)
            }
            .addOnFailureListener {
                _isDeleted.postValue(false)
            }

    }


}