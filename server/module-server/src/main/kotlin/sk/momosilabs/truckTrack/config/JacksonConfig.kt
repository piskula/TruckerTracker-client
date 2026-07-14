package sk.momosilabs.truckTrack.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// kotlin.time.Instant and kotlin.uuid.Uuid (used by the shared DTOs in :shared, consumed
// by the KMP client via kotlinx.serialization) have no built-in Jackson support — both
// serialize to/from the same plain string form kotlinx.serialization already uses, so the
// wire format is unaffected.
@OptIn(ExperimentalUuidApi::class)
@Configuration
class JacksonConfig {

    @Bean
    fun kotlinTimeAndUuidModule(): SimpleModule =
        SimpleModule().apply {
            addSerializer(Instant::class.java, InstantSerializer())
            addDeserializer(Instant::class.java, InstantDeserializer())
            addSerializer(Uuid::class.java, UuidSerializer())
            addDeserializer(Uuid::class.java, UuidDeserializer())
        }
}

private class InstantSerializer : StdSerializer<kotlin.time.Instant>(kotlin.time.Instant::class.java) {
    override fun serialize(value: kotlin.time.Instant, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.toString())
    }
}

private class InstantDeserializer : StdDeserializer<kotlin.time.Instant>(kotlin.time.Instant::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): kotlin.time.Instant =
        kotlin.time.Instant.parse(p.text)
}

@OptIn(ExperimentalUuidApi::class)
private class UuidSerializer : StdSerializer<Uuid>(Uuid::class.java) {
    override fun serialize(value: Uuid, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.toString())
    }
}

@OptIn(ExperimentalUuidApi::class)
private class UuidDeserializer : StdDeserializer<Uuid>(Uuid::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Uuid =
        Uuid.parse(p.text)
}
