package com.devvikram.varta.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devvikram.varta.data.retrofit.models.Company
import com.devvikram.varta.data.retrofit.models.Project
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Entity(tableName = "pro_contacts")
@Serializable
data class ProContacts(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    @SerializedName("id")
    val userId: Int = 0,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String = "",

    @ColumnInfo(name = "email")
    @SerializedName("email")
    val email: String = "",

    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    val gender: String = "",

    @ColumnInfo(name = "designation")
    @SerializedName("designation")
    val designation: String = "",

    @ColumnInfo(name = "profile_pic")
    @SerializedName("profilePic")
    val profilePic: String ="",

    @ColumnInfo(name = "user_status")
    @SerializedName("userStatus")
    val userStatus: Boolean = false,

    @ColumnInfo(name = "companies")
    @SerializedName("companies")
    val companies: List<Company> = emptyList(),

    @ColumnInfo(name = "projects")
    @SerializedName("projects")
    val projects: List<Project> = emptyList(),

    @ColumnInfo(name = "local_profile_pic_path")
    val localProfilePicPath: String = "",
)
