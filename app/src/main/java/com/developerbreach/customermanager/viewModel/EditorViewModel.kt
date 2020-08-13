package com.developerbreach.customermanager.viewModel

import android.app.Application
import android.text.Editable
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.developerbreach.customermanager.R
import com.google.android.material.textfield.TextInputLayout
import com.google.common.base.Strings.isNullOrEmpty
import java.util.*


class EditorViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private var _isStitchCompleted = false
    var isStitchCompleted: Boolean
        get() = _isStitchCompleted
        set(value) {
            _isStitchCompleted = value
        }

    fun stitchStatus(status: Boolean): Boolean {
        _isStitchCompleted = status
        return status
    }

    fun validateDate(): String {
        val currentDate = Calendar.getInstance().time.toString()
        return currentDate.removeRange(11, 30).drop(4)
    }

    fun validateCustomerDetails(
        text: String?,
        textInputLayout: TextInputLayout
    ): String {
        if (text?.isEmpty()!!) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = context.getString(R.string.field_required_error_text)
        } else if (text.isNotEmpty()) {
            textInputLayout.isErrorEnabled = false
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
        } else {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = context.getString(R.string.invalid_mail_error_text)
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
                textInputLayout.error = context.getString(R.string.field_required_error_text)
            }
            customerContact.length != 10 -> {
                textInputLayout.isErrorEnabled = true
                textInputLayout.error = "Invalid"
            }
            !customerContact.isNullOrEmpty() || customerContact.length == 10 -> {
                textInputLayout.isErrorEnabled = false
                textInputLayout.error = null
            }
        }

        return customerContact.toString()
    }
}