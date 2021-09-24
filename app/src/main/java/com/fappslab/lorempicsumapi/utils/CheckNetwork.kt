package com.fappslab.lorempicsumapi.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckNetwork(
    context: Context
) : LiveData<Boolean>() {

    private val cnn by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkCallback: ConnectivityManager.NetworkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Network is available, but now check if has Internet connection
                // This may take a few seconds
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = HasInternet.execute()
                    withContext(Dispatchers.Main) {
                        postValue(hasInternet)
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }
        }
    }

    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        cnn.registerNetworkCallback(builder.build(), networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        cnn.unregisterNetworkCallback(networkCallback)
    }

    object HasInternet {
        private const val command = "/system/bin/ping -c 1 8.8.8.8"

        // Android won't let you run it on the main thread
        // Use a coroutine scope
        fun execute(): Boolean {
            return try {
                val process = Runtime.getRuntime().exec(command)
                process.waitFor() == 0
            } catch (e: Exception) {
                false
            }
        }
    }
}
