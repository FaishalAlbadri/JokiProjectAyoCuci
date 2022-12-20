package com.fanonymous.ayocuci.data

import com.google.gson.annotations.SerializedName


data class BaseResponse(

    @field:SerializedName("msg")
    val msg: String
)
