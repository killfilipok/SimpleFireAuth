package co.sispo.simplefireauth.CustomVIews

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import co.sispo.libmaker.R
import co.sispo.libmaker.Utils


open class PopUp(var context: Context, root: ConstraintLayout, val id: Int? = null) {

    val popUp by lazy { ConstraintLayout(context) }
    var visible = false
    val title = TextView(context)
    val button = TextView(context)
    val backBtn = ImageView(context)
    val bgImg = ImageView(context)
    var onBackPressed: (() -> Unit)? = null
    var next: (() -> Unit)? = null

    fun dp(x: Int): Int {
        return (x * context.resources.displayMetrics.density).toInt()
    }

    init {
        val popUpParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        )

        if(id == null) {
            popUp.id = View.generateViewId()
        } else {
            popUp.id = id
        }

        Utils.createMargins(popUpParams, root.id)

        bgImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_auth_bg_2))
        bgImg.scaleType = ImageView.ScaleType.FIT_START

        val bgParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                dp(1000)
        )

        bgParams.leftToLeft = popUp.id
        bgParams.topToTop = popUp.id
//        bgParams.bottomToBottom = popUp.id
        bgParams.rightToRight = popUp.id

        bgImg.layoutParams = bgParams

        backBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.back_btn))
        val backParams = ConstraintLayout.LayoutParams(
                dp(48),
                dp(48)
        )

        backParams.leftToLeft = popUp.id
        backParams.topToTop = popUp.id

        backParams.setMargins((5 * context.resources.displayMetrics.density).toInt(), (21 * context.resources.displayMetrics.density).toInt(), 0, 0)

        backBtn.setOnClickListener({
            onBackPressed?.invoke()
        })

        popUp.addView(bgImg, bgParams)
        popUp.addView(backBtn, backParams)
        root.addView(popUp, popUpParams)

        val titleParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                dp(26)
        )

        titleParams.leftToLeft = popUp.id
        titleParams.topToTop = popUp.id

        titleParams.setMargins(dp(20), dp(90), 0, 0)

        title.textSize = 20f
        title.setTextColor(Color.WHITE)

        popUp.addView(title, titleParams)
        val buttonParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                dp(48)
        )
        buttonParams.leftToLeft = popUp.id
        buttonParams.rightToRight = popUp.id
        buttonParams.bottomToBottom = popUp.id

        buttonParams.setMargins(0, 0, 0, 0)

        button.textSize = 14f
        button.setBackgroundColor(Color.WHITE)
        button.gravity = Gravity.CENTER
        popUp.addView(button, buttonParams)
        popUp.isClickable = true
        button.setOnClickListener({
            next?.invoke()
        })
        backBtn.setOnClickListener({
            onBackPressed?.invoke()
        })
    }

    fun createInput(id: Int, titleId: Int, marginTop: Int, type: Int, title: String, root: ConstraintLayout) {
        val input = EditText(context)

        val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                dp(26)
        )

        input.id = id
        params.leftToLeft = root.id
        params.topToTop = root.id
        params.rightToRight = root.id

        params.setMargins(dp(20), marginTop, dp(20), 0)
        input.setPadding(0, 0, 0, dp(3))
        input.textSize = 17f
        input.inputType = InputType.TYPE_CLASS_TEXT or type
        input.setTextColor(Color.WHITE)
        input.setBackgroundResource(R.drawable.input_bg)
        root.addView(input, params)

        val description = TextView(context)
        val descriptionParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                dp(16)
        )

        description.id = titleId
        descriptionParams.leftToLeft = root.id
        descriptionParams.topToTop = root.id
        descriptionParams.setMargins(dp(20), marginTop - dp(26), 0, 0)
        description.textSize = 12f
        description.setTextColor(Color.WHITE)
        description.text = title
        root.addView(description, descriptionParams)
    }
}