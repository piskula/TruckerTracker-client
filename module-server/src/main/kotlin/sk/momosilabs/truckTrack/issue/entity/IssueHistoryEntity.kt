package sk.momosilabs.truckTrack.issue.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotNull
import sk.momosilabs.truckTrack.account.entity.AccountEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity(name = "issue_history")
class IssueHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID(0, 0),

    @field:NotNull
    @ManyToOne(optional = false)
    val issue: IssueEntity,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val type: IssueHistoryEventType,

    @field:NotNull
    @ManyToOne(optional = false)
    val performedBy: AccountEntity,

    @field:NotNull
    val createdAtUtc: LocalDateTime,

    @Enumerated(EnumType.STRING)
    var statusFrom: IssueStatus?,

    @Enumerated(EnumType.STRING)
    var statusTo: IssueStatus?,

    var commentText: String?,
)
