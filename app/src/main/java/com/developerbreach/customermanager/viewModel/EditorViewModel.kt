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
    val calendar: Calendar = Calendar.getInstance()

    private var _currentDate: Int = 0
    val date: Int
        get() = _currentDate

    private var _currentMonth: Int = 0
    val month: Int
        get() = _currentMonth

    private var _currentYear: Int = 0
    val year: Int
        get() = _currentYear

    init {
        _currentDate = calendar.get(Calendar.DATE)
        _currentMonth = calendar.get(Calendar.MONTH) + 1
        _currentYear = calendar.get(Calendar.YEAR)
    }

    fun validateCustomerDetails(
        text: String?,
        textInputLayout: TextInputLayout,
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
        textInputLayout: TextInputLayout,
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
        textInputLayout: TextInputLayout,
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