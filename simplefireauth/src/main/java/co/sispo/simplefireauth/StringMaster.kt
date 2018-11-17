package co.sispo.simplefireauth

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat

class StringMaster(context: Context,
                   var login:String = context.getString(R.string.login),
                   var sign_up_name:String = context.getString(R.string.sign_up_name),
                   var sign_up_email:String = context.getString(R.string.sign_up_email),
                   var sign_up_password:String = context.getString(R.string.sign_up_password),
                   var sign_up_password_length:String = context.getString(R.string.sign_up_password_length),
                   var sign_up_next:String = context.getString(R.string.sign_up_next),
                   var sign_up_pre_final:String = context.getString(R.string.sign_up_pre_final),
                   var sign_up_final:String = context.getString(R.string.sign_up_final),
                   var name:String = context.getString(R.string.name),
                   var email:String = context.getString(R.string.email),
                   var password:String = context.getString(R.string.password),
                   var password_hint:String = context.getString(R.string.password_hint),
                   var next:String = context.getString(R.string.next),
                   var forgot:String = context.getString(R.string.forgot),
                   var email_forgot_title:String = context.getString(R.string.email_forgot_title),
                   var reset_password:String = context.getString(R.string.reset_password),
                   var reset_password_done_title:String = context.getString(R.string.reset_password_done_title),
                   var reset_password_done_msg:String = context.getString(R.string.reset_password_done_msg),
                   var reset_password_err_title:String = context.getString(R.string.reset_password_err_title),
                   var reset_password_err_msg:String = context.getString(R.string.reset_password_err_msg),
                   var reset_password_empty_title:String = context.getString(R.string.reset_password_empty_title),
                   var email_field_empty_msg:String = context.getString(R.string.email_field_empty_msg),
                   var fields_empty_title:String = context.getString(R.string.fields_empty_title),
                   var fields_empty_msg:String = context.getString(R.string.fields_empty_msg),
                   var err_title:String = context.getString(R.string.err_title),
                   var err_msg:String = context.getString(R.string.err_msg),
                   var auth_fail_title:String = context.getString(R.string.auth_fail_title),
                   var auth_fail_msg:String = context.getString(R.string.auth_fail_msg),
                   var shadowColor: Int = Color.parseColor("#40000000"),
                   var accentColor: Int = Color.parseColor("#FFF5C437")
                   ){

    companion object CREATOR {
        var myStringMaster: StringMaster? = null
    }

}