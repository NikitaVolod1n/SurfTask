package com.example.surftask.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.surftask.R

fun showToast(context: Context, message: String, colorRes: Int) {
    val inflater = LayoutInflater.from(context)
    val toastLayout = inflater.inflate(R.layout.my_toast, null) as ViewGroup

    val toastText = toastLayout.findViewById<TextView>(R.id.toastMessage)
    val toastContainer = toastLayout.findViewById<ConstraintLayout>(R.id.toastContainer)

    val backgroundDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = 30f
        setColor(ContextCompat.getColor(context, colorRes))
    }
    toastContainer.background = backgroundDrawable

    //тост не хочет обрезаться ни в разметке, ни тут

    toastText.text = message
    toastContainer.setBackgroundColor(ContextCompat.getColor(context, colorRes))

    val closeButton = toastLayout.findViewById<ImageView>(R.id.toastCloseButton)

    val popupWindow = PopupWindow(toastLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

    popupWindow.showAtLocation(toastLayout, android.view.Gravity.BOTTOM, 0, 200)

    closeButton.setOnClickListener {
        popupWindow.dismiss()
        Log.d("Toast", "Toast closed")
    }

    toastLayout.postDelayed({popupWindow.dismiss()}, 2500)
}