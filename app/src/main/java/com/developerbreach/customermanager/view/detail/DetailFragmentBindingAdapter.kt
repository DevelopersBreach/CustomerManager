package com.developerbreach.customermanager.view.detail

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.model.Customers
import com.developerbreach.customermanager.utils.DELIVERY_STATUS_COMPLETED
import com.developerbreach.customermanager.utils.DELIVERY_STATUS_PENDING
import com.google.android.material.floatingactionbutton.FloatingActionButton


@BindingAdapter("bindDetailFragmentNavigationIcon")
fun Toolbar.setDetailFragmentNavigationIcon(
    activity: FragmentActivity,
) {
    ((activity) as AppCompatActivity).setSupportActionBar(this)
    this.setNavigationOnClickListener {
        findNavController().navigateUp()
    }
}


@BindingAdapter("bindDetailFabEditCustomer")
fun FloatingActionButton.setDetailFabEditCustomer(
    customers: Customers
) {
    this.setOnClickListener {
        Navigation.findNavController(this).navigate(
            DetailFragmentDirections.detailToEditorFragment(
                customers, context.getString(R.string.existing_customer_toolbar_title)
            )
        )
    }
}


@BindingAdapter("bindDetailStatusImageView", "bindDetailStatusTextView")
fun ImageView.setDetailStatusImageView(
    customers: Customers,
    statusTextView: TextView
) {
    if (customers.status == DELIVERY_STATUS_COMPLETED) {
        this.setImageResource(R.drawable.ic_completed)
        statusTextView.text = context.getString(R.string.status_completed)
    } else if (customers.status == DELIVERY_STATUS_PENDING) {
        this.setImageResource(R.drawable.ic_pending)
        statusTextView.text = context.getString(R.string.status_pending)
    }
}


@BindingAdapter("bindDetailDateGivenTextView")
fun TextView.setDetailDateGivenTextView(
    date: String
) {
    val formatDate = date.dropLast(5)
    this.text = formatDate
}


@BindingAdapter("bindDetailDateDeliveryTextView")
fun TextView.setDetailDateDeliveryTextView(
    date: String?
) {
    val formatDate = date?.dropLast(3)
    this.text = formatDate
}
