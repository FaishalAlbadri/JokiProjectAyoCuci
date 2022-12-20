package com.fanonymous.ayocuci.ui

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fanonymous.ayocuci.R
import com.fanonymous.ayocuci.api.APIClient
import com.fanonymous.ayocuci.api.APIInterface
import com.fanonymous.ayocuci.api.Server
import com.fanonymous.ayocuci.data.user.UserItem
import com.fanonymous.ayocuci.data.user.UserResponse
import com.fanonymous.ayocuci.util.AlertDialogManager
import com.fanonymous.ayocuci.util.SessionManager
import com.fanonymous.ayocuci.util.luckydraw.LuckyWheel
import com.fanonymous.ayocuci.util.luckydraw.OnLuckyWheelReachTheTarget
import com.fanonymous.ayocuci.util.luckydraw.WheelItem
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SpinWheelActivity : AppCompatActivity() {

    @BindView(R.id.btn_back)
    lateinit var btnBack: ImageView

    @BindView(R.id.btn_spin)
    lateinit var btnSpin: MaterialButton

    @BindView(R.id.sw_kupon)
    lateinit var swKupon: LuckyWheel

    @BindView(R.id.txt_count)
    lateinit var txtGachaPoint: TextView

    private var item: MutableList<WheelItem> = ArrayList()
    private var points: Int = 0
    private var gachaPoints = 0

    private val alert: AlertDialogManager = AlertDialogManager()
    private lateinit var pd: ProgressDialog
    private lateinit var sessionManager: SessionManager

    private lateinit var apiInterface: APIInterface
    private lateinit var userResponseCall: Call<UserResponse>

    private val nama = arrayOf(
        "Zonk", "20%", "30%", "Zonk", "Zonk" , "10%"
    )
    private val warna = arrayOf(
        "#75caee", "#b072d3", "#49935a", "#da615c", "#ecc962" , "#4381e5"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spin_wheel)
        ButterKnife.bind(this)
        setView()
        setDataGacha()

        txtGachaPoint.text = "X " + gachaPoints.toString()

        pd = ProgressDialog(this)
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Sedang Loading")
        pd.show()

        sessionManager = SessionManager(this)

        apiInterface = APIClient.getRetrofit(this)!!.create(APIInterface::class.java)

        loadProfile(sessionManager.getIdUser()!!)

    }

    private fun loadProfile(idUser: String) {
        userResponseCall = apiInterface.profile(idUser)
        userResponseCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                try {
                    pd.dismiss()
                    if (response.body()!!.msg.equals("Berhasil")) {
                        val userResponse: UserResponse = response.body()!!
                        val userItem: List<UserItem> = userResponse.user
                        gachaPoints = userItem.get(0).userToken.toInt()
                        txtGachaPoint.text = "X " + gachaPoints.toString()
                    } else {
                        alert.showAlertDialog(this@SpinWheelActivity, "SpinWheel gagal..", response.body()!!.msg, false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                pd.dismiss()
                alert.showAlertDialog(this@SpinWheelActivity, "Profile gagal..", Server.CHECK_INTERNET_CONNECTION, false)
            }

        })
    }

    private fun setView() {
        swKupon.setLuckyWheelReachTheTarget(object : OnLuckyWheelReachTheTarget {
            override fun onReachTarget() {
                val data: WheelItem = item.get(points - 1)
                val nama = data.text
                if (nama.equals("Zonk")) {
                    Toast.makeText(applicationContext, "Yahh.. Kamu belum mendapatkan diskon", Toast.LENGTH_SHORT).show()
                } else {
                    AlertDialog.Builder(this@SpinWheelActivity)
                        .setTitle("Kupon Kamu")
                        .setMessage("Yeeyy.. Kamu mendapatkan kupon diskon sebesar " + nama)
                        .setPositiveButton("Claim") { dialogInterface, i ->
                            dialogInterface.dismiss()
                        }
                        .show()
                }
                randomData()
            }
        })
    }

    private fun setDataGacha() {
        for (i in 0..nama.size - 1) {
            item.add(
                WheelItem(
                    Color.parseColor(warna[i]),
                    BitmapFactory.decodeResource(resources, R.drawable.blank),
                    nama[i]
                )
            )
        }

        swKupon.addWheelItems(item, MediaPlayer.create(this, R.raw.luckyspin))

        randomData()
    }

    private fun randomData() {
        val random = Random()
        points = random.nextInt(item.size + 1)
        if (points == 0 || points == item.size + 1) {
            points = 1
        }
    }

    @OnClick(R.id.btn_spin)
    fun onBtnSpinClicked() {
        if (gachaPoints > 0) {
            gachaPoints = gachaPoints - 1
            txtGachaPoint.text = "X " + gachaPoints.toString()
            swKupon.rotateWheelTo(points)
        } else {
            Toast.makeText(this@SpinWheelActivity, "Yaahhh... Point gacha-mu sudah habis!!", Toast.LENGTH_SHORT).show()
        }
    }

    @OnClick(R.id.btn_back)
    fun onBtnBackClicked() {
        onBackPressed()
    }

    override fun onBackPressed() {
        swKupon.stopSound()
        super.onBackPressed()
    }
}