package com.fanonymous.ayocuci.ui

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fanonymous.ayocuci.R
import com.fanonymous.ayocuci.api.APIClient
import com.fanonymous.ayocuci.api.APIInterface
import com.fanonymous.ayocuci.api.Server
import com.fanonymous.ayocuci.data.BaseResponse
import com.fanonymous.ayocuci.util.AlertDialogManager
import com.fanonymous.ayocuci.util.SessionManager
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CuciMotorActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener  {

    @BindView(R.id.btn_back)
    lateinit var btnBack: ImageView

    @BindView(R.id.edt_jenis_motor)
    lateinit var edtJenisMotor: EditText

    @BindView(R.id.btn_tanggal)
    lateinit var btnTanggal: TextView

    @BindView(R.id.btn_booking)
    lateinit var btnBooking: MaterialButton

    private val alert: AlertDialogManager = AlertDialogManager()

    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var dayS: String = ""
    var monthS: String = ""
    var hourS: String = ""
    var minuteS: String = ""

    private lateinit var pd: ProgressDialog
    private lateinit var apiInterface: APIInterface
    private lateinit var baseResponseCall: Call<BaseResponse>
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuci_motor)
        ButterKnife.bind(this)
        setView()
    }

    private fun setView() {
        pd = ProgressDialog(this)
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Sedang Booking")

        sessionManager = SessionManager(this)

        apiInterface = APIClient.getRetrofit(this)!!.create(APIInterface::class.java)
    }


    @OnClick(R.id.btn_booking)
    fun onBtnBookingClicked() {
        if (edtJenisMotor.text.trim().length > 0 && !btnTanggal.text.equals("Pilih Tanggal Booking")) {
            AlertDialog.Builder(this@CuciMotorActivity)
                .setTitle("Booking Cuci Motor?")
                .setMessage("Yakinkah kamu ingin booking untuk cuci motor?")
                .setPositiveButton("Booking") { dialogInterface, i ->
                    pd.show()
                    booking()
                }
                .setNegativeButton("Batal") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .show()
        } else {
            alert.showAlertDialog(this@CuciMotorActivity, "Booking gagal..", "Data masih ada yang kosong!!!", false)
        }
    }

    private fun booking() {
        baseResponseCall = apiInterface.bookingAdd(sessionManager.getIdUser()!!, "Motor", edtJenisMotor.text.toString(), "Steam - Cuci Manual | Rp20.000", btnTanggal.text.toString())
        baseResponseCall.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                try {
                    pd.dismiss()
                    if (response.body()!!.msg.equals("Berhasil")) {
                        Toast.makeText(this@CuciMotorActivity, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        alert.showAlertDialog(this@CuciMotorActivity, "Booking gagal..", response.body()!!.msg, false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                pd.dismiss()
                alert.showAlertDialog(this@CuciMotorActivity, "Booking gagal..", Server.CHECK_INTERNET_CONNECTION, false)
            }

        })
    }

    @OnClick(R.id.btn_tanggal)
    fun onBtnTanggalClicked() {
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(this@CuciMotorActivity, this@CuciMotorActivity, year, month, day)
        datePickerDialog.show()
    }

    @OnClick(R.id.btn_back)
    fun onBtnBackClicked() {
        onBackPressed()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        day = dayOfMonth
        this.year = year
        this.month = month + 1
        if (day.toString().trim().length == 1) {
            dayS = "0" + day.toString()
        } else {
            dayS = day.toString()
        }
        if (this.month.toString().trim().length == 1) {
            monthS = "0" + this.month.toString()
        } else {
            monthS = this.month.toString()
        }
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this@CuciMotorActivity, this@CuciMotorActivity, hour, minute,
            DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        hour = hourOfDay
        this.minute = minute
        if (hour.toString().trim().length == 1) {
            hourS = "0" + hour.toString()
        } else {
            hourS = hour.toString()
        }
        if (this.minute.toString().trim().length == 1) {
            minuteS = "0" + this.minute.toString()
        } else {
            minuteS = this.minute.toString()
        }
        btnTanggal.text =
            year.toString() + "-" + monthS + "-" + dayS + " " + hourS + ":" + minuteS + ":00"
    }
}