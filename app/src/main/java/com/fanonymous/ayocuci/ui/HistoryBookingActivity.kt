package com.fanonymous.ayocuci.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fanonymous.ayocuci.R
import com.fanonymous.ayocuci.adapter.HistoryBookingAdapter
import com.fanonymous.ayocuci.api.APIClient
import com.fanonymous.ayocuci.api.APIInterface
import com.fanonymous.ayocuci.api.Server
import com.fanonymous.ayocuci.data.booking.BookingItem
import com.fanonymous.ayocuci.data.booking.BookingResponse
import com.fanonymous.ayocuci.data.user.UserItem
import com.fanonymous.ayocuci.data.user.UserResponse
import com.fanonymous.ayocuci.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryBookingActivity : AppCompatActivity() {

    @BindView(R.id.btn_back)
    lateinit var btnBack: ImageView

    @BindView(R.id.rv_history)
    lateinit var rvHistory: RecyclerView

    private lateinit var pd: ProgressDialog
    private lateinit var sessionManager: SessionManager

    private lateinit var apiInterface: APIInterface
    private lateinit var bookingResponseCall: Call<BookingResponse>
    private lateinit var historyBookingAdapter: HistoryBookingAdapter
    private var bookingItem: ArrayList<BookingItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_booking)
        ButterKnife.bind(this)
        setView()
    }

    private fun setView() {
        pd = ProgressDialog(this)
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Loading")
        pd.show()

        historyBookingAdapter = HistoryBookingAdapter(this, bookingItem)
        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = historyBookingAdapter

        sessionManager = SessionManager(this)

        apiInterface = APIClient.getRetrofit(this)!!.create(APIInterface::class.java)
        loadHistory()
    }

    private fun loadHistory() {
        bookingResponseCall = apiInterface.bookingGet(sessionManager.getIdUser()!!)
        bookingResponseCall.enqueue(object : Callback<BookingResponse> {
            override fun onResponse(
                call: Call<BookingResponse>,
                response: Response<BookingResponse>
            ) {
                try {
                    pd.dismiss()
                    if (response.body()!!.msg.equals("Berhasil")) {
                        val bookingResponse = response.body()!!
                        historyBookingAdapter.delete()
                        bookingItem.clear()
                        bookingItem.addAll(bookingResponse.booking)
                        historyBookingAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@HistoryBookingActivity, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                Toast.makeText(
                    this@HistoryBookingActivity,
                    Server.CHECK_INTERNET_CONNECTION,
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressed()
            }
        })
    }

    @OnClick(R.id.btn_back)
    fun onBtnBackClicked() {
        onBackPressed()
    }
}