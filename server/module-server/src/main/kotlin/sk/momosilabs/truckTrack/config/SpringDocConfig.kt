package sk.momosilabs.truckTrack.config

import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverter
import io.swagger.v3.core.converter.ModelConverterContext
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.OAuthFlow
import io.swagger.v3.oas.annotations.security.OAuthFlows
import io.swagger.v3.oas.annotations.security.OAuthScope
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.security.SecurityRequirement
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
    name = "oauth2",
    type = SecuritySchemeType.OAUTH2,
    flows = OAuthFlows(
        authorizationCode = OAuthFlow(
            authorizationUrl = "\${springdoc.oauth2.authorization-url}",
            tokenUrl = "\${springdoc.oauth2.token-url}",
            scopes = [OAuthScope(name = "openid", description = "OpenID Connect")]
        )
    )
)
class SpringDocConfig(
    private val buildProperties: BuildProperties,
) {

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(Info().title("TruckTrack API").version(buildProperties.version))
        .addSecurityItem(SecurityRequirement().addList("oauth2"))
}
