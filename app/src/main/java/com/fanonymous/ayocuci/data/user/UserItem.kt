package com.fanonymous.ayocuci.data.user

import com.google.gson.annotations.SerializedName

data class UserItem(

    @field:SerializedName("user_email")
    val userEmail: String,

    @field:SerializedName("user_password")
    val userPassword: String,

    @field:SerializedName("user_name")
    val userName: String,

    @field:SerializedName("id_user")
    val idUser: String,

    @field:SerializedName("user_token")
    val userToken: String
)
