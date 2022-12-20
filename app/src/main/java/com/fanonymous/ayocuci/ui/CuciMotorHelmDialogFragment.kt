package com.fanonymous.ayocuci.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fanonymous.ayocuci.R
import com.google.android.material.button.MaterialButton

class CuciMotorHelmDialogFragment : DialogFragment() {

    @BindView(R.id.btn_close)
    lateinit var btnClose: ImageView

    @BindView(R.id.btn_motor)
    lateinit var btnMotor: MaterialButton

    @BindView(R.id.btn_helm)
    lateinit var btnHelm: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cuci_motor_helm_dialog, container, false)
        ButterKnife.bind(this, view)

        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            val back = ColorDrawable(Color.TRANSPARENT)
            val inset = InsetDrawable(back, 24, 24, 24, 24)
            getDialog()!!.window!!.setBackgroundDrawable(inset)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    @OnClick(R.id.btn_close)
    fun onClickClose() {
        dismiss()
    }

    @OnClick(R.id.btn_motor)
    fun onClickMotor() {
        startActivity(Intent(requireActivity().applicationContext, CuciMotorActivity::class.java))
        dismiss()
    }

    @OnClick(R.id.btn_helm)
    fun onClickHelm() {
        startActivity(Intent(requireActivity().applicationContext, CuciHelmActivity::class.java))
        dismiss()
    }

}