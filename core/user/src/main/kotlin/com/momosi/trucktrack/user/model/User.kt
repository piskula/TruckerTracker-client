package com.momosi.trucktrack.user.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
)
