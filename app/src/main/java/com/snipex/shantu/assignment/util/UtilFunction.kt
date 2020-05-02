package com.snipex.shantu.assignment.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by Ashish Ingale on 27-01-2020.
 */
object UtilFunction {
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}