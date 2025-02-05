package com.devvikram.varta.data.retrofit.models

import kotlinx.serialization.Serializable


@Serializable
data class Company(
    val companyId: Int,
    val companyLogo: String,
    val companyRole: String,
    val companyName: String,
    val companyStatus: Boolean
)
