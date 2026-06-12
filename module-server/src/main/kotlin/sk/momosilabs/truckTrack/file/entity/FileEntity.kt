package sk.momosilabs.truckTrack.file.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotNull
import sk.momosilabs.truckTrack.account.entity.AccountEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity(name = "file")
class FileEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @field:NotNull
    val uuid: UUID = UUID.randomUUID(),

    @field:NotNull
    val bucket: String,

    @field:NotNull
    val storageLocation: String,

    @field:NotNull
    val filename: String,

    @field:NotNull
    val contentType: String,

    val sizeBytes: Long,

    @ManyToOne(optional = false)
    @field:NotNull
    val uploadedBy: AccountEntity,

    @field:NotNull
    val uploadedAtUtc: LocalDateTime,

)
