package sk.momosilabs.truckTrack.issueAttachment.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotNull
import sk.momosilabs.truckTrack.file.entity.FileEntity
import sk.momosilabs.truckTrack.issueManagement.entity.IssueEntity

@Entity(name = "issue_attachment")
class IssueAttachmentEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(optional = false)
    @field:NotNull
    val issue: IssueEntity,

    @ManyToOne(optional = false)
    @field:NotNull
    val file: FileEntity,

)
