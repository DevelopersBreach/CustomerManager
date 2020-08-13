package com.developerbreach.customermanager.utils

import android.text.Editable
import android.text.TextWatcher

abstract class AppTextWatcher: TextWatcher {

    override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {}
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}