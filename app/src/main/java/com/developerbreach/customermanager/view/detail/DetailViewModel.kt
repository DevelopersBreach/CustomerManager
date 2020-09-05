package com.developerbreach.customermanager.view.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.developerbreach.customermanager.model.Customers
import com.developerbreach.customermanager.utils.COLLECTION_PATH
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class DetailViewModel(
    application: Application,
    customers: Customers,
) : AndroidViewModel(application) {

    var collection: CollectionReference

    init {
        val firestore = FirebaseFirestore.getInstance()
        collection = firestore.collection(COLLECTION_PATH)
    }

    private val _selectedCustomer: Customers = customers
    val selectedCustomer: Customers
        get() = _selectedCustomer
}