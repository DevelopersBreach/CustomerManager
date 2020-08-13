package com.developerbreach.customermanager.view.editor


import android.util.Patterns
import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter
import com.developerbreach.customermanager.R
import com.google.android.material.textfield.TextInputEditText
import com.google.common.base.Strings


@BindingAdapter(
    "bindBillNumberEditText", "bindContactEditText",
    "bindMailEditText", "bindSubmitButton"
)
fun Button.setValidateButton(
    billNumberEditText: TextInputEditText,
    contactEditText: TextInputEditText,
    mailEditText: TextInputEditText,
    submitButton: Button
) {

    this.setOnClickListener {
        val length = billNumberEditText.text!!.length
        val validContact = contactEditText.text!!.length
        val mail = mailEditText.text.toString()
        val checkMail = !Strings.isNullOrEmpty(mail) &&
                Patterns.EMAIL_ADDRESS.matcher(mail).matches()

        if (length < 4) {
            billNumberEditText.error =
                context.getString(R.string.field_validation_error_message)
        }

        if (validContact < 10) {
            contactEditText.error =
                context.getString(R.string.field_validation_error_message)
        }

        if (length >= 4 && validContact == 10 && checkMail) {
            this.visibility = View.GONE
            submitButton.visibility = View.VISIBLE
        } else {
            this.visibility = View.VISIBLE
            submitButton.visibility = View.GONE
        }
    }
}