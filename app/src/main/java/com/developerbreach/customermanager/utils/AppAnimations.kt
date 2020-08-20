package com.developerbreach.customermanager.utils


import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

fun itemViewAnimation(
    context: Context,
    view: View,
    duration: Long,
    animationProperty: Int
): Animation {
    val animation = AnimationUtils.loadAnimation(context, animationProperty)
    animation.duration = duration
    view.startAnimation(animation)
    return animation
}