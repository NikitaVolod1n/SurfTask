package com.example.surftask.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.surftask.R

fun showToast(context: Context, message: String, colorRes: Int) {
    val inflater = LayoutInflater.from(context)
    val toastLayout = inflater.inflate(R.layout.my_toast, null) as ViewGroup

    val toastText = toastLayout.findViewById<TextView>(R.id.toastMessage)
    val toastContainer = toastLayout.findViewById<ConstraintLayout>(R.id.toastContainer)

    toastText.text = message
    toastContainer.setBackgroundColor(ContextCompat.getColor(context, colorRes))

    val toast = Toast(context)
    toast.duration = Toast.LENGTH_LONG
    toast.view = toastLayout
    toast.show()

    val closeButton = toastLayout.findViewById<ImageView>(R.id.toastCloseButton)
    closeButton.setOnClickListener {
        TODO("Сделать кнопку закрытия тоста")
        Log.d("Toast", "Toast closed")
        toast.cancel()
    }
}