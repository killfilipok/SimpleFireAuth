package co.sispo.simplefireauth.CustomVIews

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.view.View
import co.sispo.simplefireauth.StringMaster
import co.sispo.simplefireauth.CustomVIews.PopUp

open class AuthLeftPopUp(context: Context,val root: ConstraintLayout,var claback: ()-> Unit, id: Int,hideKeyBoard : ()-> Unit) : PopUp(context, root,id) {

    var totalwidth = -1
    val set = ConstraintSet()
    val shadow = View(context)

    init {
        popUp.viewTreeObserver.addOnGlobalLayoutListener {
            if (totalwidth== -1) {
                totalwidth= root.width + dp(24)
                popUp.translationX = totalwidth.toFloat()
            }
        }
        val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(StringMaster.myStringMaster!!.shadowColor, Color.TRANSPARENT))
        gd.cornerRadius = 0f
        gd.orientation = GradientDrawable.Orientation.LEFT_RIGHT

        shadow.setBackgroundDrawable(gd)
        shadow.id = View.generateViewId()
        val params = ConstraintLayout.LayoutParams(
                dp(24),
                ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        params.bottomToBottom = id
        params.topToTop = id
        params.leftToLeft = id
        popUp.addView(shadow, params)

        onBackPressed = {
            hideKeyBoard()
            hide()
        }
    }

    fun show() {
        shadow.animate()
                .alpha(0f)
                .duration = 200
        visible = true
        popUp.animate()
                .translationX(0f)
                .duration = 200
    }

    fun hide() {
        shadow.animate()
                .alpha(1f)
                .duration = 200
        claback()
        visible = false
        popUp.animate()
                .translationX(totalwidth.toFloat())
                .duration = 200
    }
}