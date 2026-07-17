package sk.momosilabs.truckTrack.version.service.getVersion

import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.security.annotation.IsUser
import sk.momosilabs.truckTrack.version.model.BuildInfoModel
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class GetVersion(
    private val buildProperties: BuildProperties,
) : GetVersionUseCase {

    @IsUser
    @Transactional(readOnly = true)
    override fun get(): BuildInfoModel = BuildInfoModel(
        version = buildProperties.version ?: "unknown",
        time = buildProperties.time?.atOffset(ZoneOffset.UTC) ?: OffsetDateTime.now(ZoneOffset.UTC),
        name = buildProperties.name ?: "unknown",
    )
}
