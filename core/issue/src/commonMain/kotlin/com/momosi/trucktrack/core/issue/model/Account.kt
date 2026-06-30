package com.momosi.trucktrack.core.issue.model

data class Account(val id: String, val username: String, val firstName: String, val lastName: String) {
    val fullName: String get() = "$firstName $lastName".trim()
}
