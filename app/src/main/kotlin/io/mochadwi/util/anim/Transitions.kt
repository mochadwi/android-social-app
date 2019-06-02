package io.mochadwi.util.anim

import android.transition.TransitionSet
import android.view.animation.Interpolator
import androidx.annotation.RequiresApi

@RequiresApi(21)
fun TransitionSet.setCommonInterpolator(interpolator: Interpolator): TransitionSet {
    (0 until transitionCount)
            .map { index -> getTransitionAt(index) }
            .forEach { transition -> transition.interpolator = interpolator }

    return this
}
