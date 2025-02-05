package com.devvikram.varta.data.retrofit.models

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val projectId: Int,
    val projectName: String,
    val projectStatus: Boolean
)