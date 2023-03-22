package com.example.book_tracker.core.data.data_source.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Class for observe network connectivity of device
 *
 *
 *
 *
 *
 * */
class NetworkConnectivityObserver @Inject constructor(context: Context) : ConnectivityObserver {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val currentConnectionState: ConnectivityObserver.ConnectionState =
        getCurrentConnectivity(connectivityManager)

    /** Check if active network hasCapability of NET_CAPABILITY_INTERNET */
    private fun getCurrentConnectivity(connectivityManager: ConnectivityManager): ConnectivityObserver.ConnectionState {
        val connected =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        return if (connected) ConnectivityObserver.ConnectionState.Available
        else ConnectivityObserver.ConnectionState.Unavailable
    }

    override fun observe(): Flow<ConnectivityObserver.ConnectionState> {
        return callbackFlow {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
//
                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(ConnectivityObserver.ConnectionState.Unavailable) }
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(ConnectivityObserver.ConnectionState.Available) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(ConnectivityObserver.ConnectionState.Unavailable) }
                }
            }

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }.distinctUntilChanged()
    }
}