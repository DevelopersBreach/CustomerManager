package com.developerbreach.customermanager.view.list

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.model.Customers
import com.developerbreach.customermanager.utils.AppTextWatcher
import com.developerbreach.customermanager.utils.COLLECTION_PATH
import com.developerbreach.customermanager.utils.COLLECTION_PATH_FIELD_STATUS
import com.developerbreach.customermanager.utils.itemViewAnimation
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


@BindingAdapter("bindCustomerItemClickListener")
fun ConstraintLayout.setCustomerItemClickListener(
    customers: Customers,
) {
    val parent = this
    parent.setOnClickListener {

        itemViewAnimation(
            context = context,
            view = parent,
            duration = 250L,
            animationProperty = R.anim.fade_enter_anim
        ).setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationEnd(p0: Animation?) {
                TransitionManager.beginDelayedTransition(parent, Fade())
                findNavController().navigate(
                    CustomerListFragmentDirections.customerToDetailFragment(customers),
                    FragmentNavigatorExtras(
                        parent to customers.billNumber.toString()
                    )
                )
            }

            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {}
        })
    }
}


@BindingAdapter("bindCustomerStatusItemImageView", "bindCustomerStatusItemCollection")
fun ImageView.setCustomerStatusItemImageView(
    customers: Customers,
    collection: CollectionReference,
) {
    if (customers.status) {
        this.setImageResource(R.drawable.ic_completed)
        this.setOnClickListener {
            val dialogTitle = "Mark Pending - ${customers.billNumber}"
            val dialogMessage = "Update ${customers.itemType} as pending. Change it now ?"
            val dialogPositiveButton = context.getString(R.string.status_pending)
            showStatusDialog(
                context, customers, collection, dialogTitle, dialogMessage, dialogPositiveButton
            )
        }
    } else {
        this.setImageResource(R.drawable.ic_clear)
        this.setOnClickListener {
            val dialogTitle = "Mark Completed - ${customers.billNumber}"
            val dialogMessage = "Update ${customers.itemType} as completed. Change it now ?"
            val dialogPositiveButton = context.getString(R.string.status_completed)
            showStatusDialog(
                context, customers, collection, dialogTitle, dialogMessage, dialogPositiveButton
            )
        }
    }
}

private fun showStatusDialog(
    context: Context,
    customers: Customers,
    collection: CollectionReference,
    dialogTitle: String,
    dialogMessage: String,
    dialogPositiveButton: String,
) {
    MaterialAlertDialogBuilder(context, R.style.Widget_Customer_Dialog)
        .setTitle(dialogTitle)
        .setMessage(dialogMessage)
        .setPositiveButton(dialogPositiveButton) { _, _ ->
            statusPositiveListener(context, customers, collection)
        }
        .setNegativeButton(context.getString(R.string.dialog_negative_button_cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

private fun statusPositiveListener(
    context: Context,
    customers: Customers,
    collection: CollectionReference,
) {
    collection.document(customers.billNumber.toString())
        .update(COLLECTION_PATH_FIELD_STATUS, !customers.status)
        .addOnSuccessListener {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
        }
}


@BindingAdapter("bindSearchBillNumberFirestore")
fun AppCompatEditText.setSearchBillNumberFirestore(
    firestore: FirebaseFirestore,
) {
    this.addTextChangedListener(object : AppTextWatcher() {
        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

            val query = text.toString()
            if (query.length == 4) {

                val docSnapShot: Task<DocumentSnapshot> =
                    firestore.collection(COLLECTION_PATH).document(query).get()
                docSnapShot.addOnSuccessListener { document ->
                    if (document.exists()) {
                        val customer = document.toObject(Customers::class.java)
                        showResultsDialog(customer)
                    } else {
                        showErrorDialog(query)
                    }
                }
            }
        }

        private fun showResultsDialog(customer: Customers?) {
            val dialog: AlertDialog = createMaterialDialog()
            setDialogViews(dialog, customer!!)
            dialog.show()
        }

        private fun setDialogViews(
            dialog: AlertDialog,
            customer: Customers,
        ) {
            dialog.setOnShowListener { dialogInterface ->
                dialogInterface as AlertDialog
                val billNumber: TextView =
                    dialogInterface.findViewById(R.id.search_dialog_bill_number_text_view)!!
                val customerName: TextView =
                    dialogInterface.findViewById(R.id.search_dialog_customer_name_text_view)!!
                val status: ImageView =
                    dialogInterface.findViewById(R.id.search_dialog_customer_status_image_view)!!

                billNumber.text = customer.billNumber
                customerName.text = customer.name
                if (customer.status) {
                    status.setImageResource(R.drawable.ic_completed)
                } else {
                    status.setImageResource(R.drawable.ic_clear)
                }

                billNumber.setOnClickListener {
                    dialog.dismiss()
                    findNavController().navigate(
                        CustomerListFragmentDirections.customerToDetailFragment(customer)
                    )
                }
            }
        }

        private fun createMaterialDialog(): AlertDialog =
            MaterialAlertDialogBuilder(context)
                .setView(R.layout.results_dialog)
                .create()

        private fun showErrorDialog(query: String) {
            MaterialAlertDialogBuilder(context, R.style.Widget_Customer_Dialog)
                .setTitle(context.getString(R.string.dialog_title_search_error))
                .setMessage("Bill number $query not found.")
                .setPositiveButton(context.getString(R.string.dialog_positive_button_ok)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    })
}


@BindingAdapter("bindClearSearchQueryImageView")
fun ImageView.setClearSearchQueryImageView(
    searchEditText: AppCompatEditText,
) {
    val imageView = this
    searchEditText.addTextChangedListener(object : AppTextWatcher() {
        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (searchEditText.text!!.isEmpty()) {
                imageView.visibility = View.INVISIBLE
            } else {
                imageView.visibility = View.VISIBLE
            }
        }
    })

    imageView.setOnClickListener {
        searchEditText.text = null
    }
}
