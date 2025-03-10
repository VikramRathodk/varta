package com.devvikram.varta.data.api.models.login

data class LoginUser(
    val companies: List<Company>,
    val statusText: String,
    val email: String,
    val gender: String,
    val name: String,
    val profilePic: String,
    val projects: List<Project>,
    val session: Session,
    val userStatus: Boolean
)