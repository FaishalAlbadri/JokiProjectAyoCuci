package com.fanonymous.ayocuci.data.booking

import com.google.gson.annotations.SerializedName

data class BookingItem(

	@field:SerializedName("id_booking")
	val idBooking: String,

	@field:SerializedName("booking_merk")
	val bookingMerk: String,

	@field:SerializedName("booking_jenis_service")
	val bookingJenisService: String,

	@field:SerializedName("booking_tanggal")
	val bookingTanggal: String,

	@field:SerializedName("booking_jenis")
	val bookingJenis: String,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

	@field:SerializedName("id_user")
	val idUser: String
)