package com.momosi.trucktrack.core.common.version.dto

import com.momosi.trucktrack.core.common.version.model.ServerVersion
import com.momosi.trucktrack.shared.version.BuildInfoDto

fun BuildInfoDto.toServerVersion(): ServerVersion = ServerVersion(
    version = version,
    builtAt = time,
)
