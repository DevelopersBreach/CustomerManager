package com.developerbreach.customermanager.viewModel

import android.app.Application
import android.text.Editable
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.google.common.base.Strings.isNullOrEmpty


class EditorViewModel(application: Application) : AndroidViewModel(application) {

    //private val context = application.applicationContext

    private val _showSubmitButton = MutableLiveData<Boolean>()
    val showSubmitButton: LiveData<Boolean>
        get() = _showSubmitButton


    init {
        _showSubmitButton.value = false
    }


    fun validateBaseId(
        text: String?,
        textInputLayout: TextInputLayout
    ): String {
        if (text?.isEmpty()!!) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = "Field required"
            _showSubmitButton.value = false
        } else if (text.isNotEmpty()) {
            textInputLayout.isErrorEnabled = false
            _showSubmitButton.value = true
        }

        return text.toString()
    }


    fun validateBillNumber(
        text: String?,
        textInputLayout: TextInputLayout
    ): String {
        if (text?.isEmpty()!!) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = "Field required"
            _showSubmitButton.value = false
        } else if (text.isNotEmpty()) {
            textInputLayout.isErrorEnabled = false
            _showSubmitButton.value = true
        }

        return text.toString()
    }


    fun validateNumOfItems(
        text: String?,
        textInputLayout: TextInputLayout
    ): String {
        if (text?.isEmpty()!!) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = "Field required"
            _showSubmitButton.value = false
        } else if (text.isNotEmpty()) {
            textInputLayout.isErrorEnabled = false
            _showSubmitButton.value = true
        }

        return text.toString()
    }


    fun validateCustomerName(
        text: String?,
        textInputLayout: TextInputLayout
    ): String {
        if (text?.isEmpty()!!) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = "Field required"
            _showSubmitButton.value = false
        } else if (text.isNotEmpty()) {
            textInputLayout.isErrorEnabled = false
            _showSubmitButton.value = true
        }

        return text.toString()
    }


    fun isValidEmail(
        customerMail: Editable?,
        textInputLayout: TextInputLayout
    ): String {
        val correctMail = !isNullOrEmpty(customerMail.toString()) &&
                Patterns.EMAIL_ADDRESS.matcher(customerMail.toString()).matches()

        if (correctMail) {
            textInputLayout.isErrorEnabled = false
            _showSubmitButton.value = false
        } else {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = "Invalid"
            _showSubmitButton.value = true
        }

        return customerMail.toString()
    }


    fun isValidContact(
        customerContact: Editable?,
        textInputLayout: TextInputLayout
    ): String {
        when {
            customerContact?.isEmpty()!! -> {
                textInputLayout.isErrorEnabled = true
                textInputLayout.error = "Field required"
                _showSubmitButton.value = false
            }
            customerContact.length != 10 -> {
                textInputLayout.isErrorEnabled = true
                textInputLayout.error = "Invalid"
                _showSubmitButton.value = false
            }
            !customerContact.isNullOrEmpty() || customerContact.length == 10 -> {
                textInputLayout.isErrorEnabled = false
                textInputLayout.error = null
                _showSubmitButton.value = true
            }
        }

        return customerContact.toString()
    }
}