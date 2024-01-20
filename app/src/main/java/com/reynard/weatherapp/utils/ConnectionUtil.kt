package com.reynard.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
object ConnectionUtil {

    private val mainHandler = Handler(Looper.getMainLooper())

    fun callback(disconnected: () -> Unit) : ConnectivityManager.NetworkCallback{
        return object : ConnectivityManager.NetworkCallback(){
            override fun onLost(network: Network) {
                super.onLost(network)
                mainHandler.post {
                    disconnected()
                }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                mainHandler.post {
                    disconnected()
                }
            }
        }
    }

    fun isNotConnectedWifiAndCellular(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return true
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return true

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> false
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> false
            else -> true
        }
    }
}