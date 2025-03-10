package com.devvikram.varta.data.retrofit.models

data class RegisterModel(
    val username : String,
    val password : String,
    val gender :String ="M",
    val statusText : String = "Hey There ! I am using Varta"
)