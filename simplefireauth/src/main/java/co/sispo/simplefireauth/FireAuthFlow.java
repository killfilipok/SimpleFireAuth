package co.sispo.simplefireauth;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import kotlin.jvm.functions.Function3;

public class FireAuthFlow {
    public static void s(Context c, String message){

        Toast.makeText(c,message,Toast.LENGTH_SHORT).show();

    }

    public static AuthFlow getFlow(Activity var1, ConstraintLayout var2, Function3 var3, FirebaseAuth var4, FirebaseFirestore var5, StringMaster var6){
        return new AuthFlow(var1,var2,var3,var4,var5,var6);
    }
}
