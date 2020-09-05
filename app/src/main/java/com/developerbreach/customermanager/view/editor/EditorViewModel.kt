package com.developerbreach.customermanager.view.editor

import android.app.Application
import android.text.Editable
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.utils.COLLECTION_PATH
import com.developerbreach.customermanager.utils.isNetworkConnected
import com.google.android.material.textfield.TextInputLayout
import com.google.common.base.Strings.isNullOrEmpty
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class EditorViewModel(application: Application) : AndroidViewModel(application) {

    var collection: CollectionReference
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

    private val _isConnected: Boolean
    val isConnected: Boolean
        get() = _isConnected

    init {
        val firestore = FirebaseFirestore.getInstance()
        collection = firestore.collection(COLLECTION_PATH)

        _currentDate = calendar.get(Calendar.DATE)
        _currentMonth = calendar.get(Calendar.MONTH)
        _currentYear = calendar.get(Calendar.YEAR)

        _isConnected = isNetworkConnected(application.applicationContext)
    }

    fun validateField(
        text: String?,
        textInputLayout: TextInputLayout,
    ): String {
        val context = textInputLayout.context
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

        val context = textInputLayout.context
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
        val context = textInputLayout.context
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