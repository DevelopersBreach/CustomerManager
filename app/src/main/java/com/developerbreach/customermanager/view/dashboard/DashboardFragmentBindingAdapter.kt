package com.developerbreach.customermanager.view.dashboard

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import com.developerbreach.customermanager.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


@BindingAdapter("bindDashboardFabListener")
fun FloatingActionButton.setDashboardFabListener(
    navController: NavController,
) {
    this.setOnClickListener {
        navController.navigate(
            DashboardFragmentDirections.dashboardToEditorFragment(
                null,
                context.getString(R.string.editor_fragment_label))
        )
    }
}


@BindingAdapter("bindCustomerDataLabelListener")
fun TextView.setCustomerDataLabelListener(
    navController: NavController,
) {
    this.setOnClickListener {
        navController.navigate(
            DashboardFragmentDirections.dashboardToCustomerListFragment()
        )
    }
}