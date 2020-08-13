package com.developerbreach.customermanager.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

/**
 * This class has method to perform network connection test.
 * Performs for Android devices Q and below.
 *
 * @param context needs to be declared to call system services.
 * @return if false no network is available.
 */
fun isNetworkConnected(
    context: Context
): Boolean {

    val manager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork: Network? = manager.activeNetwork
    val capabilities: NetworkCapabilities? = manager.getNetworkCapabilities(activeNetwork)
    return capabilities != null &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

}