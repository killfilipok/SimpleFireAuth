package co.sispo.simplefireauth.CustomVIews

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import co.sispo.simplefireauth.StringMaster

class ForgotPopUp(context: Context, authPopUp: AuthPopUp, claback: ()-> Unit, val forgotPassword : (String, ()-> Unit)-> Unit, id: Int,emailInputId: Int, hideKeyBoard : ()-> Unit) : AuthLeftPopUp(context, authPopUp.popUp, claback,id, hideKeyBoard) {

    init {
        val titleId = View.generateViewId()

        authPopUp.viewsToRemove.add(emailInputId)
        authPopUp.viewsToRemove.add(titleId)

        title.text = StringMaster.myStringMaster!!.email_forgot_title
        createInput(emailInputId, titleId , dp(171), InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, StringMaster.myStringMaster!!.email, popUp)
        button.text = StringMaster.myStringMaster!!.reset_password
        next = {forgotPassword(popUp.findViewById<EditText>(emailInputId).text.toString(), {
            authPopUp.hide()
        })}
    }
}