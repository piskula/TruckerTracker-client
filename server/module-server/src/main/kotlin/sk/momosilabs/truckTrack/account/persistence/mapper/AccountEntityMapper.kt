package sk.momosilabs.truckTrack.account.persistence.mapper

import sk.momosilabs.truckTrack.account.entity.AccountEntity
import sk.momosilabs.truckTrack.account.model.AccountModel

fun AccountEntity.toModel() = AccountModel(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
)
