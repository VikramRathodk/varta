package com.devvikram.varta.data.retrofit

import com.devvikram.varta.data.room.models.ProContacts

class ProContactsResponse (
    val status: Boolean = false,
    val statusCode: Int,
    val message: String,
    val data: List<ProContacts>,
    val error: String = ""
)
