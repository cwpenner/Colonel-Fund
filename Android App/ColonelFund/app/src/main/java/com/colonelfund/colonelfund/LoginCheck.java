package com.colonelfund.colonelfund;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

/**
 * Launches main Login/Register screen if user is not
 * already logged in upon app launch.
 */
public class LoginCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        AccessToken token = AccessToken.getCurrentAccessToken();
        if(token == null && GoogleSignIn.getLastSignedInAccount(this) == null){
            Intent LoginIntent = new Intent(LoginCheck.this,LoginActivity.class);
            startActivity(LoginIntent);
            LoginCheck.this.finish();
        } else {
            Intent MainIntent = new Intent(LoginCheck.this, MainActivity.class);
            startActivity(MainIntent);
            LoginCheck.this.finish();
        }


    }

}
