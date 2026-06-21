package com.momosi.trucktrack.core.common.network

import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.ext.SdkExtensions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityManagerImpl @Inject constructor(private val androidConnectivityManager: android.net.ConnectivityManager) : ConnectivityManager {

    private var networkConnectionFlow = MutableStateFlow(androidConnectivityManager.getNetworkCapabilities(androidConnectivityManager.activeNetwork).mapToNetworkCapabilities())

    private val callback: android.net.ConnectivityManager.NetworkCallback = object : android.net.ConnectivityManager.NetworkCallback() {

        override fun onLost(network: Network) {
            networkConnectionFlow.tryEmit(null.mapToNetworkCapabilities())
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            networkConnectionFlow.tryEmit(networkCapabilities.mapToNetworkCapabilities())
        }
    }

    init {
        androidConnectivityManager.registerDefaultNetworkCallback(callback)
    }

    override val isNetworkAvailable: Boolean
        get() = networkConnectionFlow.value != ConnectivityManager.ConnectionType.TYPE_OFFLINE

    override val connectionType: ConnectivityManager.ConnectionType
        get() = networkConnectionFlow.value

    override val isNetworkConnectionMetered: Boolean
        get() = androidConnectivityManager.isActiveNetworkMetered

    override fun observeConnectionType(): Flow<ConnectivityManager.ConnectionType> = networkConnectionFlow.debounce(400)
}

private fun NetworkCapabilities?.mapToNetworkCapabilities(): ConnectivityManager.ConnectionType = when {
    this == null || this.isOnlyVpn() -> ConnectivityManager.ConnectionType.TYPE_OFFLINE
    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectivityManager.ConnectionType.TYPE_WIFI
    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectivityManager.ConnectionType.TYPE_MOBILE_CELLULAR
    else -> ConnectivityManager.ConnectionType.TYPE_MOBILE_UNKNOWN
}

private fun NetworkCapabilities.isOnlyVpn(): Boolean = hasTransport(NetworkCapabilities.TRANSPORT_VPN) &&
    !hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) &&
    !hasTransport(NetworkCapabilities.TRANSPORT_WIFI) &&
    !hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) &&
    !hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) &&
    !hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) &&
    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) !hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) else true) &&
    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) !hasTransport(NetworkCapabilities.TRANSPORT_USB) else true) &&
    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) >= 7) !hasTransport(NetworkCapabilities.TRANSPORT_THREAD) else true) &&
    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) >= 12) !hasTransport(NetworkCapabilities.TRANSPORT_SATELLITE) else true)
