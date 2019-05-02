package co.sispo.libmaker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import co.sispo.simplefireauth.CustomVIews.AuthPopUp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    fun hideKeyboard() {
        val view = getCurrentFocus();

        if (view != null) {
            view.clearFocus()
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    val popUp: AuthPopUp by lazy { AuthPopUp(this, root, ::hideKeyboard) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        popUp.createSignUp(::popUpSignUpWithEmail)
        popUp.show()
    }

    fun popUpSignUpWithEmail(x: String,t: String,s: String) {

    }
}
