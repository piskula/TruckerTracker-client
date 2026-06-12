package sk.momosilabs.truckTrack.account.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import sk.momosilabs.truckTrack.account.entity.AccountEntity
import java.util.UUID

interface AccountRepository : JpaRepository<AccountEntity, UUID>
