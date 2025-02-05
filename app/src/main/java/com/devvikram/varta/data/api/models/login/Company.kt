package com.devvikram.varta.data.api.models.login

data class Company(
    val companyId: Int,
    val companyLogo: String,
    val companyName: String,
    val companyRole: String,
    val companyStatus: Boolean
)