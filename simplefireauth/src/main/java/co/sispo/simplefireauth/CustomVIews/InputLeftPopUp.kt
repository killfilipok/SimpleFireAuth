package co.sispo.simplefireauth.lib.CustomVIews

import android.content.ClipDescription
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast

class InputLeftPopUp(context: Context, root: ConstraintLayout, claback: ()-> Unit, inputType: Int, description: String, titleTxt: String, submitText:String, id: Int, inputId: Int,hideKeyBoard : ()-> Unit) : AuthLeftPopUp(context, root, claback,id, hideKeyBoard) {

    init {
        val titleId = View.generateViewId()

        title.text = titleTxt
        createInput(inputId, titleId , dp(151), inputType, description, popUp)
        button.text = submitText
    }
}