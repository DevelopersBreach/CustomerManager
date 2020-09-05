package com.developerbreach.customermanager.view.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.map
import com.developerbreach.customermanager.auth.AuthenticationState
import com.developerbreach.customermanager.auth.FirebaseUserLiveData

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}