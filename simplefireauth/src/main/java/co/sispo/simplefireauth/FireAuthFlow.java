package co.sispo.simplefireauth;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireAuthFlow {
    public static void s(Context c, String message){

        Toast.makeText(c,message,Toast.LENGTH_SHORT).show();
    }
}
