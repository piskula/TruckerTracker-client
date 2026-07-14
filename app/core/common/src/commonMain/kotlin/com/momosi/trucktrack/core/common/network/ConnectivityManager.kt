package com.momosi.trucktrack.core.common.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityManager {

    val isNetworkAvailable: Boolean
    val connectionType: ConnectionType
    val isNetworkConnectionMetered: Boolean
    fun observeConnectionType(): Flow<ConnectionType>

    enum class ConnectionType {
        TYPE_OFFLINE,
        TYPE_UNKNOWN,
        TYPE_WIFI,
        TYPE_MOBILE_UNKNOWN,
        TYPE_MOBILE_CELLULAR,
        ;

        fun isOffline(): Boolean = this == TYPE_OFFLINE

        fun isDataNetwork(): Boolean = isWifiNetwork().not() && isOffline().not()

        fun isWifiNetwork(): Boolean = this == TYPE_WIFI
    }
}
