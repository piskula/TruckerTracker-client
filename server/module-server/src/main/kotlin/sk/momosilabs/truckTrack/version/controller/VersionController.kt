package sk.momosilabs.truckTrack.version.controller

import sk.momosilabs.truckTrack.api.version.dto.BuildInfoDto
import org.springframework.web.bind.annotation.RestController
import sk.momosilabs.truckTrack.api.version.VersionApi
import sk.momosilabs.truckTrack.version.model.BuildInfoModel
import sk.momosilabs.truckTrack.version.service.getVersion.GetVersionUseCase

@RestController
class VersionController(
    private val getVersion: GetVersionUseCase,
) : VersionApi {

    override fun getBuildInfo(): BuildInfoDto =
        getVersion.get().toDto()
}

private fun BuildInfoModel.toDto() = BuildInfoDto(
    version = version,
    time = time,
    name = name,
)