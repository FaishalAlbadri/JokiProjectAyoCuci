package com.fanonymous.ayocuci.data.booking

import com.google.gson.annotations.SerializedName

data class BookingResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("booking")
	val booking: List<BookingItem>
)