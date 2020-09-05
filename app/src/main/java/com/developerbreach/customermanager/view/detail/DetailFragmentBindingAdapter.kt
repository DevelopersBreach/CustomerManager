package com.developerbreach.customermanager.view.detail

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.model.Customers
import com.google.android.material.floatingactionbutton.FloatingActionButton


@BindingAdapter("bindDetailFragmentNavigationIcon")
fun Toolbar.setDetailFragmentNavigationIcon(
    activity: FragmentActivity,
) {
    ((activity) as AppCompatActivity).setSupportActionBar(this)
    this.title = ""
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
