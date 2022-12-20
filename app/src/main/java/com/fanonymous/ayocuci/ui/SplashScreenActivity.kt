package com.fanonymous.ayocuci.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.fanonymous.ayocuci.R
import com.fanonymous.ayocuci.util.SessionManager

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sessionManager = SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            if (sessionManager.getIdUser() == null) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }
        }, 3000)
    }
}