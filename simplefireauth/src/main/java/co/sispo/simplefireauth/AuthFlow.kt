package co.sispo.simplefireauth.lib

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.view.View
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import java.util.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import co.sispo.simplefireauth.CustomVIews.WaitSplash
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import co.sispo.simplefireauth.StringMaster
import co.sispo.simplefireauth.lib.CustomVIews.AuthPopUp
import co.sispo.simplefireauth.lib.Utils.fireAlert
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject
import java.lang.Exception

open class AuthFlow(
        var activity: Activity,
        var root: ConstraintLayout,
        var userHandler: ((uid:String, email: String, name: String) -> Unit),
        var mAuth: FirebaseAuth,
        var mDb: FirebaseFirestore,
        master: StringMaster? = null) {

    init {
        if(master == null){
            StringMaster.myStringMaster = StringMaster(activity)
        } else {
            StringMaster.myStringMaster = master
        }
    }

    val RC_SIGN_IN = 9001
    var waitSplash: WaitSplash = WaitSplash(activity, root)
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    fun hideKeyboard() {
        val view = activity.getCurrentFocus();

        if (view != null) {
            view.clearFocus()
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    var popUp: AuthPopUp = AuthPopUp(activity, root, ::hideKeyboard)
    private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }

    fun setUpGoogleLogin(
            webClientID: String,
            google_btn: View
    ) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientID)
                .requestEmail()
                .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso);
        google_btn.setOnClickListener({
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(activity, signInIntent, RC_SIGN_IN, null)

            waitSplash.show()
        })
    }

    fun setUpFacebookLogin(facebook_btn: View) {
        facebook_btn.setOnClickListener({
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"))
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    firebaseAuthWithFirebase(result.accessToken)

                }

                override fun onCancel() {
                    waitSplash.hide()
                }

                override fun onError(error: FacebookException?) {
                    fireAlert(activity, StringMaster.myStringMaster!!.err_title, StringMaster.myStringMaster!!.err_msg)
                    waitSplash.hide()
                }
            })
        })


    }


    fun setUpAuthWithEmail(sign_up_btn: View, sign_in_btn: View) {
        sign_up_btn.setOnClickListener({
            popUp.createSignUp(::popUpSignUpWithEmail)
            popUp.show()
        })

        sign_in_btn.setOnClickListener({
            popUp.createSignIn(::popUpSignInWithEmail, ::forgotPassword)
            popUp.show()
        })
    }

    fun forgotPassword(email: String, calback: () -> Unit) {
        hideKeyboard()
        if (email.isNotEmpty()) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            fireAlert(activity,  StringMaster.myStringMaster!!.reset_password_done_title, StringMaster.myStringMaster!!.reset_password_done_msg, { calback() })
                        }
                    }
                    .addOnFailureListener {
                        fireAlert(activity,StringMaster.myStringMaster!!.err_title, StringMaster.myStringMaster!!.err_msg)
                    }
        } else {
            fireAlert(activity, StringMaster.myStringMaster!!.fields_empty_title, StringMaster.myStringMaster!!.email_field_empty_msg)
        }

    }

    fun popUpSignUpWithEmail(email: String, password: String, name: String) {
        if (fieldsIsNotEmpty(arrayOf(email, password, name))) {
            waitSplash.show()
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, object : OnCompleteListener<AuthResult> {

                        override fun onComplete(task: Task<AuthResult>) {
                            if (task.isSuccessful()) {
                                createOrUpdateUserInDb(true, name)
                            } else {
                                fireAlert(activity, StringMaster.myStringMaster!!.auth_fail_title, StringMaster.myStringMaster!!.auth_fail_msg, { waitSplash.hide() })
                            }
                        }
                    })
        }
    }

    fun fieldsIsNotEmpty(arr: Array<String>): Boolean {
        arr.forEach({ e ->
            if (e.isEmpty()) {
                fireAlert(activity, StringMaster.myStringMaster!!.fields_empty_title, StringMaster.myStringMaster!!.fields_empty_msg)
                return false
            }
        })
        return true
    }

    fun popUpSignInWithEmail(email: String, password: String) {

        if (fieldsIsNotEmpty(arrayOf(email, password))) {
            waitSplash.show()
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser

                            if (user == null) {
                                waitSplash.hide()
                                fireAlert(activity, StringMaster.myStringMaster!!.err_title, StringMaster.myStringMaster!!.err_msg)
                            } else {
                                mDb.collection("users")
                                        .document(user.uid)
                                        .get()
                                        .addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                                            override fun onComplete(p0: Task<DocumentSnapshot>) {
                                                if (p0.isSuccessful) {
                                                    val document = p0.result as DocumentSnapshot
                                                    if (document.exists()) {
                                                        val data = document.data!!

                                                        waitSplash.hide()
                                                        userHandler(user.uid, data["email"].toString(), data["name"].toString())
                                                        popUp.hide()
                                                    } else {
                                                        waitSplash.hide()
                                                        fireAlert(activity, StringMaster.myStringMaster!!.err_title,StringMaster.myStringMaster!!.auth_fail_msg)
                                                    }
                                                }
                                            }
                                        })
                                        .addOnFailureListener {
                                            waitSplash.hide()
                                            fireAlert(activity, StringMaster.myStringMaster!!.err_title,StringMaster.myStringMaster!!.err_msg)
                                        }
                            }
                        } else {
                            fireAlert(activity, StringMaster.myStringMaster!!.auth_fail_title, StringMaster.myStringMaster!!.auth_fail_msg, { waitSplash.hide() })
                        }
                    }
        }
    }


    fun handleOnBackPressed(calback: (() -> Unit)) {
        if (popUp.visible) {
            popUp.onBackPressed?.invoke()
        } else {
            calback()
        }
    }

    fun HandleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                waitSplash.hide()
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun createOrUpdateUserInDb(inAuthPopUp: Boolean, name: String = "Anonymous") {
        val user = mAuth.currentUser
        var nameToPush = name
        if (user == null) {
            waitSplash.hide()
            fireAlert(activity, StringMaster.myStringMaster!!.err_title, StringMaster.myStringMaster!!.err_msg)
        } else {
            if (!user.displayName.isNullOrEmpty()) {
                nameToPush = user.displayName!!
            }
            val userObj = HashMap<String, Any>()
            userObj["name"] = nameToPush
            userObj["email"] = user.email!!

            mDb.collection("users")
                    .document(user.uid)
                    .set(userObj)
                    .addOnSuccessListener(object : OnSuccessListener<Void> {
                        override fun onSuccess(p0: Void?) {
                            waitSplash.hide()
                            userHandler(user.uid, user.email!!, nameToPush)
                            if(inAuthPopUp) popUp.hide()
                        }
                    })
                    .addOnFailureListener(object : OnFailureListener {
                        override fun onFailure(p0: Exception) {
                            waitSplash.hide()
                            fireAlert(activity, StringMaster.myStringMaster!!.err_title, p0.toString())
                        }
                    })
        }
    }

    fun handleAuthCredential(credential: AuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        createOrUpdateUserInDb(false)
                    } else {
                        fireAlert(activity, StringMaster.myStringMaster!!.auth_fail_title, StringMaster.myStringMaster!!.auth_fail_msg, { waitSplash.hide() })
                    }
                }
    }

    fun firebaseAuthWithFirebase(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        handleAuthCredential(credential)
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        handleAuthCredential(credential)
    }
}