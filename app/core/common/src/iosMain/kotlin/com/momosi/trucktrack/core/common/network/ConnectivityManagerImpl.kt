package com.momosi.trucktrack.core.common.network

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import platform.Network.nw_interface_type_cellular
import platform.Network.nw_interface_type_wifi
import platform.Network.nw_path_get_status
import platform.Network.nw_path_is_expensive
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_t
import platform.Network.nw_path_uses_interface_type
import platform.darwin.dispatch_queue_create

private data class PathState(val connectionType: ConnectivityManager.ConnectionType, val isMetered: Boolean)

@OptIn(ExperimentalForeignApi::class)
class ConnectivityManagerImpl : ConnectivityManager {

    private val pathStateFlow = MutableStateFlow(
        PathState(connectionType = ConnectivityManager.ConnectionType.TYPE_UNKNOWN, isMetered = false),
    )

    private val monitor = nw_path_monitor_create()
    private val monitorQueue = dispatch_queue_create("com.momosi.trucktrack.connectivity", null)

    init {
        nw_path_monitor_set_queue(monitor, monitorQueue)
        nw_path_monitor_set_update_handler(monitor) { path ->
            pathStateFlow.value = path.toPathState()
        }
        nw_path_monitor_start(monitor)
    }

    override val isNetworkAvailable: Boolean
        get() = pathStateFlow.value.connectionType.isOffline().not()

    override val connectionType: ConnectivityManager.ConnectionType
        get() = pathStateFlow.value.connectionType

    override val isNetworkConnectionMetered: Boolean
        get() = pathStateFlow.value.isMetered

    @OptIn(FlowPreview::class)
    override fun observeConnectionType(): Flow<ConnectivityManager.ConnectionType> = pathStateFlow.map { it.connectionType }.debounce(400)
}

@OptIn(ExperimentalForeignApi::class)
private fun nw_path_t.toPathState(): PathState {
    if (this == null || nw_path_get_status(this) != nw_path_status_satisfied) {
        return PathState(connectionType = ConnectivityManager.ConnectionType.TYPE_OFFLINE, isMetered = false)
    }
    val connectionType = when {
        nw_path_uses_interface_type(this, nw_interface_type_wifi) -> ConnectivityManager.ConnectionType.TYPE_WIFI
        nw_path_uses_interface_type(this, nw_interface_type_cellular) -> ConnectivityManager.ConnectionType.TYPE_MOBILE_CELLULAR
        else -> ConnectivityManager.ConnectionType.TYPE_MOBILE_UNKNOWN
    }
    return PathState(connectionType = connectionType, isMetered = nw_path_is_expensive(this))
}
