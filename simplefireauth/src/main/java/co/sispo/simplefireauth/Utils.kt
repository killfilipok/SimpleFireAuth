package co.sispo.simplefireauth.lib

import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog

object Utils {
    fun createMargins(params: ConstraintLayout.LayoutParams, id: Int){
        mapMargins(params, id)

        params.setMargins(0,0,0,0)
    }

    fun mapMargins(params: ConstraintLayout.LayoutParams, id: Int) {
        params.bottomToBottom = id
        params.topToTop = id
        params.rightToRight = id
        params.leftToLeft = id
    }

    fun fireAlert(context: Context, title: String, msg:String, calback: () -> Unit = {}){
        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(context)
        }

        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    calback()
                }
                .setOnCancelListener {
                    calback()
                }
                .show()
    }
}