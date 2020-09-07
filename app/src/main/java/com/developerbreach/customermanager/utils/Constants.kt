package com.developerbreach.customermanager.utils

import com.firebase.ui.auth.AuthUI


const val COLLECTION_PATH: String = "customers"
const val COLLECTION_PATH_FIELD_STATUS: String = "status"

const val DELIVERY_STATUS_PENDING = 0
const val DELIVERY_STATUS_COMPLETED = 1

const val SIGN_IN_REQUEST_CODE = 9001
val PROVIDERS = arrayListOf(
    AuthUI.IdpConfig.GoogleBuilder().build(),
    AuthUI.IdpConfig.EmailBuilder().build()
)