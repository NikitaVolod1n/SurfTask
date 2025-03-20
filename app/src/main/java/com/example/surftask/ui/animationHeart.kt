package com.example.surftask.ui

import android.content.Context
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.surftask.R

fun animate(context : Context, favorite : Int, favorite_fill : Int, isFavorite : Boolean, heartIcon : ImageView) {
    val shrinkAnimation = AnimationUtils.loadAnimation(context, R.anim.heart_to_small)
    heartIcon.startAnimation(shrinkAnimation)

    if (isFavorite) {
        heartIcon.setImageResource(favorite)
    } else {
        heartIcon.setImageResource(favorite_fill)
    }

    heartIcon.postDelayed({
        val growAnimation = AnimationUtils.loadAnimation(context, R.anim.heart_to_big)
        heartIcon.startAnimation(growAnimation)
    }, 150)
}