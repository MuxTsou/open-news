package ai.nextstop.opennews.data.local.connectivity

import android.content.Context
import android.net.*
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

class ConnectivityLiveData(val context: Context) : LiveData<ConnectionState>() {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @RequiresApi(Build.VERSION_CODES.S)
    private val callback =  object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network : Network) {
            try {
                val command = "ping -c 1 google.com"
                val result = Runtime.getRuntime().exec(command).waitFor() == 0
                val state = if(result) ConnectionState.Available else ConnectionState.Unavailable
                postValue(state)
            } catch (e: Exception) {
                postValue(ConnectionState.Unavailable)
            }

        }

        override fun onLost(network : Network) {
            postValue(ConnectionState.Unavailable)
        }

        override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {

        }

        override fun onLinkPropertiesChanged(network : Network, linkProperties : LinkProperties) {

        }
    }

    init {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isConnected = if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                true
            } else {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }
        } else {
            false
        }
        value = if(isConnected) ConnectionState.Available else ConnectionState.Unavailable
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onActive() {
        super.onActive()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(callback)
    }
}