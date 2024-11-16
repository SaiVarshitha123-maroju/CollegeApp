package com.example.collegeappjetpackcompose.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.identity.util.UUID
import com.example.collegeappjetpackcompose.models.BannerModel
import com.example.collegeappjetpackcompose.models.NoticeModel
import com.example.collegeappjetpackcompose.utils.Constant.BANNER
import com.example.collegeappjetpackcompose.utils.Constant.NOTICE
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class NoticeViewModel: ViewModel() {

    private val noticeRef = Firebase.firestore.collection(NOTICE)

    private  val storageRef = Firebase.storage.reference

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted : LiveData<Boolean> = _isPosted

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted : LiveData<Boolean> = _isDeleted

    private val _noticeList = MutableLiveData<List<NoticeModel>>()
    val noticeList : LiveData<List<NoticeModel>> = _noticeList

    fun saveNotice(uri: Uri, title: String, link: String){
        _isPosted.postValue(false)
        val randomUid = UUID.randomUUID().toString()


        val imageRef = storageRef.child("$NOTICE/${randomUid}.jpg")

        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                uploadNotice(it.toString(),randomUid, title,link)
            }
        }



    }

    private fun uploadNotice(imageUrl: String, docId: String,title: String, link : String) {

        val map = mutableMapOf<String,String>()
        map["imageUrl"] = imageUrl
        map["docId"] = docId
        map["title"] = title
        map["link"] = link



        noticeRef.document(docId).set(map)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener{
                _isPosted.postValue(false)
            }




    }

    fun getNotice(){
        noticeRef.get().addOnSuccessListener {
            val list = mutableListOf<NoticeModel>()

            for (doc in it){
                list.add(doc.toObject(NoticeModel::class.java))
            }

            _noticeList.postValue(list)

        }
    }

    fun deleteNotice(noticeModel: NoticeModel){


        noticeRef.document(noticeModel.docId!!).delete()
            .addOnSuccessListener {
                Firebase.storage.getReferenceFromUrl(noticeModel.imageUrl!!).delete()
                _isDeleted.postValue(true)
            }
            .addOnFailureListener {
                _isDeleted.postValue(false)
            }

    }


}