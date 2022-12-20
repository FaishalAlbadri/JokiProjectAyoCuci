package com.fanonymous.ayocuci.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fanonymous.ayocuci.R

class HomeActivity : AppCompatActivity() {

    @BindView(R.id.btn_profile)
    lateinit var btnProfile: ImageView

    @BindView(R.id.btn_mobil)
    lateinit var btnMobil: ImageView

    @BindView(R.id.btn_motor_helm)
    lateinit var btnMotorHelm: ImageView

    @BindView(R.id.btn_karpet)
    lateinit var btnKarpet: ImageView

    @BindView(R.id.btn_spinwhell)
    lateinit var btnSpinwhell: ImageView

    @BindView(R.id.btn_aboutus)
    lateinit var btnAboutus: ImageView

    var cuciMotorHelmDialogFragment: CuciMotorHelmDialogFragment = CuciMotorHelmDialogFragment()
    var fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_profile)
    fun onBtnProfileClicked(){
        startActivity(Intent(applicationContext, ProfileActivity::class.java))
    }

    @OnClick(R.id.btn_mobil)
    fun onBtnMobilClicked(){
        startActivity(Intent(applicationContext, CuciMobilActivity::class.java))
    }

    @OnClick(R.id.btn_motor_helm)
    fun onBtnMotorHelmClicked(){
        cuciMotorHelmDialogFragment.show(fragmentManager, "")
    }

    @OnClick(R.id.btn_karpet)
    fun onBtnKarpetClicked(){
        startActivity(Intent(applicationContext, CuciKarpetActivity::class.java))
    }

    @OnClick(R.id.btn_spinwhell)
    fun onBtnSpinwheelClicked(){
        startActivity(Intent(applicationContext, SpinWheelActivity::class.java))
    }

    @OnClick(R.id.btn_aboutus)
    fun onBtnAboutUsClicked(){
        startActivity(Intent(applicationContext, AboutUsActivity::class.java))
    }
}