package co.sispo.simplefireauth.CustomVIews

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.text.InputType
import android.view.Gravity
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import co.sispo.libmaker.R
import co.sispo.libmaker.Utils
import co.sispo.libmaker.Utils.fireAlert
import co.sispo.simplefireauth.CustomVIews.PopUp


class AuthPopUp(context: Context, var root: ConstraintLayout, var hideKeyboard: () -> Unit) : PopUp(context, root) {

    val viewsToRemove = ArrayList<Int>()
    var totalHeight = -1


    init {
        popUp.viewTreeObserver.addOnGlobalLayoutListener {
            if (totalHeight == -1) {
                totalHeight = root.height
                popUp.translationY = totalHeight.toFloat()
            }
        }
        onBackPressed = { hide() }
    }

    fun createSignIn(calback: (email: String, password: String) -> Unit, forgotPassword: (String, () -> Unit) -> Unit) {
        title.text = "login"
        button.text = "next"

        viewsToRemove.clear()
        for (i in 0..7) {
            viewsToRemove.add(View.generateViewId())
        }
        createInput(viewsToRemove[0], viewsToRemove[1], dp(171), InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, "email", popUp)
        createInput(viewsToRemove[2], viewsToRemove[3], dp(242), InputType.TYPE_TEXT_VARIATION_PASSWORD, "password", popUp)

        val forgotBtn = TextView(context)
        forgotBtn.id = viewsToRemove[4]
        val forgotParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                dp(48)
        )

        forgotParams.topToTop = popUp.id
        forgotParams.leftToLeft = popUp.id
        forgotParams.rightToRight = popUp.id

        forgotParams.setMargins(0, dp(273), 0, 0)

        forgotBtn.textSize = 12f
        forgotBtn.gravity = Gravity.CENTER
        forgotBtn.text = "forgot"
        forgotBtn.setTextColor(Color.WHITE)
        popUp.addView(forgotBtn, forgotParams)


        val temp = onBackPressed

        val left = ForgotPopUp(context, this, {
            onBackPressed = temp
        }, forgotPassword, viewsToRemove[5],viewsToRemove[6], hideKeyboard)


        forgotBtn.setOnClickListener {
            left.popUp.findViewById<EditText>(viewsToRemove[6]).requestFocus()
            onBackPressed = left.onBackPressed
            left.show()
        }

        next = {
            hideKeyboard()
            calback(popUp.findViewById<EditText>(viewsToRemove[0]).text.toString(),
                    popUp.findViewById<EditText>(viewsToRemove[2]).text.toString())
        }
    }

    fun createSignUp(calback: (email: String, password: String, name: String) -> Unit) {
        title.text = "sign_up_name"
        button.text = "sign_up_next"

        viewsToRemove.clear()
        for (i in 0..6) {
            viewsToRemove.add(View.generateViewId())
        }
        createInput(viewsToRemove[0], viewsToRemove[1], dp(171), InputType.TYPE_CLASS_TEXT, "name", popUp)
        val emailTemp = onBackPressed

        val emailLeft = InputLeftPopUp(
                context,
                popUp,
                { onBackPressed = emailTemp },
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                "email",
                "sign_up_email",
                "sign_up_pre_final",
                viewsToRemove[2],
                viewsToRemove[3],
                hideKeyboard
        )
        val passTemp = emailLeft.onBackPressed

        val passLeft = InputLeftPopUp(
                context,
                emailLeft.popUp,
                { onBackPressed = passTemp },
                InputType.TYPE_TEXT_VARIATION_PASSWORD,
                "password",
                "sign_up_password",
                "sign_up_final",
                viewsToRemove[4],
                viewsToRemove[5],
                hideKeyboard)

        val hintText = TextView(context)
        val hintIcon = ImageView(context)
        hintIcon.setPadding(dp(4),dp(4),dp(4),dp(4))
        hintIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.s_close_white))

        hintText.text = "password_hint"
        hintText.textSize = 12f
        hintText.setTextColor(Color.WHITE)

        val iconParams = ConstraintLayout.LayoutParams(
                dp(16),
                dp(18)
        )
        iconParams.topToTop = passLeft.popUp.id
        iconParams.leftToLeft = passLeft.popUp.id
        iconParams.setMargins(dp(16), dp(203), 0 ,0)

        val txtParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        txtParams.topToTop = passLeft.popUp.id
        txtParams.leftToLeft = passLeft.popUp.id
        txtParams.setMargins(dp(44), dp(203), 0 ,0)

        passLeft.popUp.addView(hintIcon, iconParams)
        passLeft.popUp.addView(hintText, txtParams)

        val nameField = popUp.findViewById<EditText>(viewsToRemove[0])
        val emailField = emailLeft.popUp.findViewById<EditText>(viewsToRemove[3])
        val passwordField = passLeft.popUp.findViewById<EditText>(viewsToRemove[5])
        passwordField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (passwordField.text.length >= 6){
                    hintIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.s_checkmark_white))
                } else {
                    hintIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.s_close_white))
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        });
        emailLeft.next = {
            if (emailField.text.toString().isNotEmpty()) {
                passwordField.requestFocus()
                passLeft.show()
                onBackPressed = passLeft.onBackPressed
            } else {
                hideKeyboard()
                fireAlert(context, "fields_empty_title", "email_field_empty_msg")
            }
        }

        passLeft.next = {
            if (passwordField.text.length >= 6){
                hideKeyboard()
                if (passwordField.text.toString().isNotEmpty()) {
                    calback(emailField.text.toString(), passwordField.text.toString(), nameField.text.toString())
                } else {
                    fireAlert(context, "fields_empty_title", "fields_empty_msg")
                }
            } else {
                fireAlert(context,"fields_empty_title","fields_empty_msg")
            }
        }

        next = {
            if (nameField.text.toString().isNotEmpty()) {
                onBackPressed = emailLeft.onBackPressed
                emailField.requestFocus()
                emailLeft.show()
            } else {
                hideKeyboard()
                fireAlert(context, "fields_empty_title", "fields_empty_msg")
            }
        }
    }

    fun show() {
        visible = true
        popUp.animate()
                .translationY(0f)
                .duration = 500
    }

    fun hide() {
        next = null
        hideKeyboard()
        visible = false
        popUp.animate()
                .translationY(totalHeight.toFloat())
                .withEndAction({ destroyViews() })
                .duration = 500
    }

    fun destroyViews() {
        viewsToRemove.forEach({ e ->
            popUp.removeView(popUp.findViewById(e))
        })
        viewsToRemove.clear()
    }
}