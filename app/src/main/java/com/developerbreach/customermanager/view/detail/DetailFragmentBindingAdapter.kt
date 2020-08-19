package com.developerbreach.customermanager.view.detail

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.model.Customers
import com.google.android.material.floatingactionbutton.FloatingActionButton


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


@BindingAdapter("bindDetailStatusImageView")
fun ImageView.setDetailStatusImageView(
    customers: Customers
) {
    if (customers.status) {
        this.setImageResource(R.drawable.ic_completed)
    } else {
        this.setImageResource(R.drawable.ic_clear)
    }
}
