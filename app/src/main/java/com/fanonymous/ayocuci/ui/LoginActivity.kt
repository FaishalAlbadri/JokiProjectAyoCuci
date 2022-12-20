package com.fanonymous.ayocuci.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.btn_masuk)
    lateinit var btnLogin: Button

    @BindView(R.id.btn_register)
    lateinit var btnRegister: Button

    @BindView(R.id.edt_email)
    lateinit var edtEmail: EditText

    @BindView(R.id.edt_password)
    lateinit var edtPassword: EditText

    private val alert: AlertDialogManager = AlertDialogManager()
    private lateinit var pd: ProgressDialog
    private lateinit var sessionManager: SessionManager

    private lateinit var apiInterface: APIInterface
    private lateinit var userResponseCall: Call<UserResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        pd = ProgressDialog(this)
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("Sedang Login")

        sessionManager = SessionManager(this)

        apiInterface = APIClient.getRetrofit(this)!!.create(APIInterface::class.java)

    }

    @OnClick(R.id.btn_masuk)
    fun onBtnLoginClicked(){

        // Get username, password from EditText
        val email: String = edtEmail.getText().toString()
        val password: String = edtPassword.getText().toString()

        if (email.trim().length > 0 && password.trim().length > 0) {
            pd.show()
            login(email, password)
        } else {
            alert.showAlertDialog(this@LoginActivity, "Login gagal..", "Form tidak boleh kosong!", false)
        }

    }

    private fun login(email: String, password: String) {
        userResponseCall = apiInterface.login(email, password)
        userResponseCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                try {
                    pd.dismiss()
                    if (response.body()!!.msg.equals("Berhasil")) {
                        val userResponse: UserResponse = response.body()!!
                        val userItem: List<UserItem> = userResponse.user
                        val idUser: String = userItem.get(0).idUser
                        sessionManager.createUser(idUser)
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        finish()
                    } else {
                        alert.showAlertDialog(this@LoginActivity, "Login gagal..", response.body()!!.msg, false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                pd.dismiss()
                alert.showAlertDialog(this@LoginActivity, "Login gagal..", Server.CHECK_INTERNET_CONNECTION, false)
            }

        })
    }

    @OnClick(R.id.btn_register)
    fun onBtnRegisterClicked(){
        startActivity(Intent(applicationContext, RegisterActivity::class.java))
    }
}