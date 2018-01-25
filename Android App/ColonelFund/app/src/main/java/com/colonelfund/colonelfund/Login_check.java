

package com.colonelfund.colonelfund;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class Login_check extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        AccessToken token = AccessToken.getCurrentAccessToken();
        if(token == null && GoogleSignIn.getLastSignedInAccount(this) == null){
            Intent LoginIntent = new Intent(Login_check.this,LoginActivity.class);
            startActivity(LoginIntent);
            Login_check.this.finish();
        }else {
            Intent MainIntent = new Intent (Login_check.this,MainActivity.class);
            startActivity(MainIntent);
            Login_check.this.finish();
        }


    }
}
