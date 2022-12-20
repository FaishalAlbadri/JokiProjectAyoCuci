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

class CuciMobilActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.btn_back)
    lateinit var btnBack: ImageView

    @BindView(R.id.edt_jenis_mobil)
    lateinit var edtJenisMobil: EditText

    @BindView(R.id.spinner_jenis_service)
    lateinit var spinnerJenisService: Spinner

    @BindView(R.id.btn_tanggal)
    lateinit var btnTanggal: TextView

    @BindView(R.id.btn_booking)
    lateinit var btnBooking: MaterialButton

    private val alert: AlertDialogManager = AlertDialogManager()

    var pilihanJenisService = ""
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
        setContentView(R.layout.activity_cuci_mobil)
        ButterKnife.bind(this)

        setView()

        setSpinner()
    }

    private fun setView() {
        pd = ProgressDialog(this)
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Sedang Booking")

        sessionManager = SessionManager(this)

        apiInterface = APIClient.getRetrofit(this)!!.create(APIInterface::class.java)
    }

    private fun setSpinner() {
        val jenisService = resources.getStringArray(R.array.jenis_service_mobil)
        pilihanJenisService = jenisService[0]
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisService)
        spinnerJenisService.adapter = adapter
        spinnerJenisService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                pilihanJenisService = jenisService[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    @OnClick(R.id.btn_booking)
    fun onBtnBookingClicked() {
        if (edtJenisMobil.text.trim().length > 0 && !btnTanggal.text.equals("Pilih Tanggal Booking") && pilihanJenisService.trim().length > 0) {
            AlertDialog.Builder(this@CuciMobilActivity)
                .setTitle("Booking Cuci Mobil?")
                .setMessage("Yakinkah kamu ingin booking untuk cuci mobil?")
                .setPositiveButton("Booking") { dialogInterface, i ->
                    pd.show()
                    booking()
                }
                .setNegativeButton("Batal") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .show()
        } else {
            alert.showAlertDialog(this@CuciMobilActivity, "Booking gagal..", "Data masih ada yang kosong!!!", false)
        }
    }

    private fun booking() {
        baseResponseCall = apiInterface.bookingAdd(sessionManager.getIdUser()!!, "Mobil", edtJenisMobil.text.toString(), pilihanJenisService, btnTanggal.text.toString())
        baseResponseCall.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                try {
                    pd.dismiss()
                    if (response.body()!!.msg.equals("Berhasil")) {
                        Toast.makeText(this@CuciMobilActivity, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        alert.showAlertDialog(this@CuciMobilActivity, "Booking gagal..", response.body()!!.msg, false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                pd.dismiss()
                alert.showAlertDialog(this@CuciMobilActivity, "Booking gagal..", Server.CHECK_INTERNET_CONNECTION, false)
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
            DatePickerDialog(this@CuciMobilActivity, this@CuciMobilActivity, year, month, day)
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
            this@CuciMobilActivity, this@CuciMobilActivity, hour, minute,
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