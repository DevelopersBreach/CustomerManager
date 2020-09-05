package com.developerbreach.customermanager.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.developerbreach.customermanager.auth.AuthenticationState
import com.developerbreach.customermanager.auth.FirebaseUserLiveData

class LoginViewModel : ViewModel() {

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}