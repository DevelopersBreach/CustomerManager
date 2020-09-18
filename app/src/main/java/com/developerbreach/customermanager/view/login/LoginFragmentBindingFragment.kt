package com.developerbreach.customermanager.view.login

import android.app.Activity
import android.widget.Button
import androidx.databinding.BindingAdapter
import com.developerbreach.customermanager.R
import com.developerbreach.customermanager.utils.PROVIDERS
import com.developerbreach.customermanager.utils.SIGN_IN_REQUEST_CODE
import com.firebase.ui.auth.AuthUI


@BindingAdapter("bindLoginButton")
fun Button.setLoginButton(
    activity: Activity,
) {
    this.setOnClickListener {
        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(PROVIDERS)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.ic_completed)
                .setTheme(R.style.AppTheme_Customer_Firebase)
                .build(),
            SIGN_IN_REQUEST_CODE
        )
    }
}