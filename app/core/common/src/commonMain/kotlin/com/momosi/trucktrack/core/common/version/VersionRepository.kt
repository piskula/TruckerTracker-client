package com.momosi.trucktrack.core.common.version

import com.momosi.trucktrack.core.common.version.model.ServerVersion

interface VersionRepository {

    suspend fun getServerVersion(): Result<ServerVersion>
}
