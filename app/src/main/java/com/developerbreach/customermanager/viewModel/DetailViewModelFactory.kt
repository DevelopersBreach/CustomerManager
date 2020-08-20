package com.developerbreach.customermanager.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developerbreach.customermanager.model.Customers

class DetailViewModelFactory internal constructor(
    private val application: Application,
    private val customers: Customers,
) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(application, customers) as T
        }
        throw IllegalArgumentException("Cannot create Instance for DetailViewModel class")
    }

}