package com.devvikram.varta.data.retrofit.models

data class RegisterModel(
    val username : String,
    val password : String,
    val gender :String ="Male"
)