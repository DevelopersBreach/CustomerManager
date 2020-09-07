package com.developerbreach.customermanager.view.editor


import android.app.DatePickerDialog
import android.content.Context
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.model.Customers
import com.developerbreach.customermanager.utils.DELIVERY_STATUS_PENDING
import com.developerbreach.customermanager.utils.isNetworkConnected
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.common.base.Strings
import java.text.DateFormat
import java.util.*


@BindingAdapter(
    "bindBillNumberEditText", "bindContactEditText",
    "bindMailEditText", "bindSubmitButton"
)
fun Button.setValidateButton(
    billNumberEditText: TextInputEditText,
    contactEditText: TextInputEditText,
    mailEditText: TextInputEditText,
    submitButton: Button,
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


@BindingAdapter(
    "bindSubmitButton", "bindBillNumberEditSubmission", "bindNumOfItemsEditSubmission",
    "bindCustomerNameEditSubmission", "bindMailEditSubmission", "bindContactEditSubmission",
    "bindItemTypeDropDownSubmission", "bindCurrentDateTextSubmission",
    "bindDeliveryDateTextSubmission"
)
fun Button.setSubmitButton(
    viewModel: EditorViewModel,
    billNumberEditText: TextInputEditText,
    numberOfItemsEditText: TextInputEditText,
    customerNameEditText: TextInputEditText,
    mailEditText: TextInputEditText,
    contactEditText: TextInputEditText,
    itemTypeDropDown: AutoCompleteTextView,
    currentDate: TextView,
    deliveryDate: TextView,
) {
    this.setOnClickListener {

        val customers = Customers(
            billNumberEditText.text.toString(),
            numberOfItemsEditText.text.toString(),
            itemTypeDropDown.text.toString(),
            customerNameEditText.text.toString(),
            mailEditText.text.toString(),
            contactEditText.text.toString(),
            DELIVERY_STATUS_PENDING,
            currentDate.text.toString(),
            deliveryDate.text.toString()
        )

        if (isNetworkConnected(context)) {
            viewModel.collection.document(billNumberEditText.text.toString()).set(customers)
                .addOnSuccessListener {
                    firestoreSuccessListener(
                        customerNameEditText.text.toString(),
                        billNumberEditText.text.toString()
                    )
                }
                .addOnFailureListener { firestoreFailureListener() }
        } else {
            showNetworkDialog(context)
        }
    }
}

fun showNetworkDialog(context: Context) {
    MaterialAlertDialogBuilder(context, R.style.Widget_Customer_Dialog)
        .setTitle(context.getString(R.string.dialog_error_title_text))
        .setMessage(context.getString(R.string.dialog_error_message_text))
        .setPositiveButton(context.getString(R.string.dialog_positive_button_ok)) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

private fun Button.firestoreSuccessListener(
    customerName: String,
    billNumber: String,
) {
    MaterialAlertDialogBuilder(context, R.style.Widget_Customer_Dialog)
        .setTitle("$customerName - $billNumber")
        .setMessage(context.getString(R.string.dialog_positive_message_text))
        .setPositiveButton(context.getString(R.string.dialog_positive_button_ok)) { dialog, _ ->
            dialog.dismiss()
            findNavController().navigateUp()
        }
        .show()
}

private fun Button.firestoreFailureListener() {
    val message = context.getString(R.string.customer_firestore_unsuccessful)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


@BindingAdapter("bindCurrentDateTextView")
fun TextView.setCurrentDateTextView(
    viewModel: EditorViewModel,
) {
    val dayOfMonth = viewModel.date
    val month = viewModel.month
    val year = viewModel.year
    val currentDate = "$dayOfMonth/${month + 1}/$year"
    this.text = currentDate
}


@BindingAdapter("bindDeliveryDateTextView")
fun TextView.setDeliveryDateTextView(
    viewModel: EditorViewModel,
) {
    val dayOfMonth = viewModel.date
    val month = viewModel.month
    val year = viewModel.year
    val deliveryDate = "${dayOfMonth + 1}/${month + 1}/$year"
    this.text = deliveryDate

    this.setOnClickListener {
        val datePickerDialog = DatePickerDialog(context, { _, i, i2, i3 ->
            onDateSet(i, i2, i3, viewModel, this)
        }, year, month, dayOfMonth)
        datePickerDialog.show()
    }
}

fun onDateSet(
    year: Int,
    month: Int,
    dayOfMonth: Int,
    viewModel: EditorViewModel,
    textView: TextView,
) {
    viewModel.calendar.set(Calendar.YEAR, year)
    viewModel.calendar.set(Calendar.MONTH, month)
    viewModel.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    val format =
        DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(viewModel.calendar.time)
    textView.text = format
}


@BindingAdapter("bindItemTypeDropDown")
fun AutoCompleteTextView.setItemTypeDropDown(
    context: Context,
) {
    val adapter: ArrayAdapter<String> = ArrayAdapter(
        context,
        R.layout.drop_down_item,
        resources.getStringArray(R.array.dropdown_content)
    )

    this.setAdapter(adapter)
    this.setText(adapter.getItem(0), false)
    this.setOnItemClickListener { _, _, itemId, _ ->
        when (itemId) {
            0 -> context.getString(R.string.item_type_dress)
            1 -> context.getString(R.string.item_type_blouse)
            2 -> context.getString(R.string.item_type_ghagra)
            3 -> context.getString(R.string.item_type_lehenga)
            4 -> context.getString(R.string.item_type_long_frock)
        }
    }
}


@BindingAdapter("bindEditorFragmentNavigationIcon", "bindEditorFragmentTitle")
fun Toolbar.setEditorFragmentNavigationIcon(
    activity: FragmentActivity,
    toolbarTitleArgs: String,
) {
    ((activity) as AppCompatActivity).setSupportActionBar(this)
    this.title = toolbarTitleArgs
    this.setNavigationOnClickListener {
        findNavController().navigateUp()
    }
}
