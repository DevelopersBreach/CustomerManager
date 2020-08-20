package com.developerbreach.customermanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.developerbreach.customermanager.model.Customers

class DetailViewModel(
    application: Application,
    customers: Customers,
) : AndroidViewModel(application) {

    private val _selectedCustomer: Customers = customers
    val selectedCustomer: Customers
        get() = _selectedCustomer
}