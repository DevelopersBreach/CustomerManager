package com.developerbreach.customermanager.view.list

import androidx.lifecycle.ViewModel
import com.developerbreach.customermanager.utils.COLLECTION_PATH
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CustomerListViewModel : ViewModel() {

    var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var query: Query
    var collection: CollectionReference

    init {
        // Access a Cloud Firestore instance from your Activity
        query = firestore.collection(COLLECTION_PATH)
        collection = firestore.collection(COLLECTION_PATH)
    }
}