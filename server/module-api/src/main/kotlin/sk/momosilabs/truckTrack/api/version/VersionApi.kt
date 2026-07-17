package sk.momosilabs.truckTrack.api.version

import sk.momosilabs.truckTrack.api.version.dto.BuildInfoDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping

@Tag(name = "Version")
interface VersionApi {

    companion object {
        private const val ENDPOINT = "/api/v1/version"
    }

    @Operation(summary = "Get build info")
    @GetMapping(ENDPOINT)
    fun getBuildInfo(): BuildInfoDto
}
