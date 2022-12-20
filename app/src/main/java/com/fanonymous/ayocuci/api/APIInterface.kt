package com.fanonymous.ayocuci.api

import com.fanonymous.ayocuci.data.BaseResponse
import com.fanonymous.ayocuci.data.booking.BookingResponse
import com.fanonymous.ayocuci.data.user.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIInterface {

    @FormUrlEncoded
    @POST("user/profile.php")
    fun profile(
        @Field("id_user") id_user: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/login.php")
    fun login(
        @Field("user_email") user_email: String,
        @Field("user_password") user_password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/register.php")
    fun register(
        @Field("user_name") user_name: String,
        @Field("user_email") user_email: String,
        @Field("user_password") user_password: String
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("booking/add.php")
    fun bookingAdd(
        @Field("id_user") id_user: String,
        @Field("booking_jenis") booking_jenis: String,
        @Field("booking_merk") booking_merk: String,
        @Field("booking_jenis_service") booking_jenis_service: String,
        @Field("booking_tanggal") booking_tanggal: String
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("booking/get.php")
    fun bookingGet(
        @Field("id_user") id_user: String
    ): Call<BookingResponse>

}