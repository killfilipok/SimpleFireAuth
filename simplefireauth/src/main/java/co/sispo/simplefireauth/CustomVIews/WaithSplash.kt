package co.sispo.simplefireauth.CustomVIews

import android.app.Activity
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewCompat
import android.view.View
import android.widget.ProgressBar
import co.sispo.simplefireauth.Utils.createMargins

class WaitSplash(val activity: Activity, root: ConstraintLayout) {

    private var circleColor: Int = Color.WHITE
    private val progressBar by lazy { ProgressBar(activity) }
    private val waitSplash by lazy { ConstraintLayout(activity) }

    init {
        val splashParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        waitSplash.setBackgroundColor(Color.parseColor("#66000000"))
        createMargins(splashParams, root.id)
        root.addView(waitSplash, splashParams)

        val circleParams = ConstraintLayout.LayoutParams(
                (48 * activity.resources.displayMetrics.density).toInt(),
                (48 * activity.resources.displayMetrics.density).toInt()
        )
        waitSplash.id = ViewCompat.generateViewId()
        createMargins(circleParams, waitSplash.id)

        progressBar.layoutParams = circleParams

        progressBar.getIndeterminateDrawable().setColorFilter(circleColor, android.graphics.PorterDuff.Mode.MULTIPLY)

        waitSplash.addView(progressBar, circleParams)
        waitSplash.isClickable = true
        waitSplash.fitsSystemWindows = true
        ViewCompat.setElevation(waitSplash, 5f)
        hide()
    }

    fun show() {

        waitSplash.bringToFront()
        waitSplash.visibility = View.VISIBLE
        waitSplash.invalidate()
        waitSplash.requestLayout()
    }

    fun hide() {
        waitSplash.visibility = View.GONE
        waitSplash.invalidate()
        waitSplash.requestLayout()
    }
}
