package com.fanonymous.ayocuci.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    @BindView(R.id.btn_back)
    lateinit var btnBack: ImageView

    @BindView(R.id.btn_history)
    lateinit var btnHistory: ConstraintLayout

    @BindView(R.id.txt_nama)
    lateinit var txtNama: TextView

    @BindView(R.id.txt_email)
    lateinit var txtEmail: TextView

    @BindView(R.id.btn_logout)
    lateinit var btnLogout: MaterialButton

    private val alert: AlertDialogManager = AlertDialogManager()
    private lateinit var pd: ProgressDialog
    private lateinit var sessionManager: SessionManager

    private lateinit var apiInterface: APIInterface
    private lateinit var userResponseCall: Call<UserResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        ButterKnife.bind(this)

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
                        txtNama.text = userItem.get(0).userName
                        txtEmail.text = userItem.get(0).userEmail
                    } else {
                        alert.showAlertDialog(this@ProfileActivity, "Profile gagal..", response.body()!!.msg, false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                pd.dismiss()
                alert.showAlertDialog(this@ProfileActivity, "Profile gagal..", Server.CHECK_INTERNET_CONNECTION, false)
            }

        })
    }

    @OnClick(R.id.btn_logout)
    fun onBtnLogoutClicked() {
        AlertDialog.Builder(this)
            .setTitle("Logout Akun")
            .setMessage("Yakinkah kamu ingin keluar dari akunmu?")
            .setPositiveButton("Logout") { dialogInterface, i ->
                sessionManager.logout()
            }
            .setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }

    @OnClick(R.id.btn_history)
    fun onBtnHistoryClicked() {
        startActivity(Intent(applicationContext, HistoryBookingActivity::class.java))
    }

    @OnClick(R.id.btn_back)
    fun onBtnBackClicked() {
        onBackPressed()
    }
}