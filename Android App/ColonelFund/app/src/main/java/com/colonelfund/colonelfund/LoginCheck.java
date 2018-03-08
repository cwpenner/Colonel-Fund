package com.colonelfund.colonelfund;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Launches main Login/Register screen if user is not
 * already logged in upon app launch.
 */
public class LoginCheck extends AppCompatActivity {

    // Current User Properties
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String profilePicURL;
    private String facebookID;
    private String googleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        AccessToken fbToken = AccessToken.getCurrentAccessToken();
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(fbToken == null && googleAccount == null){
            //Not logged in. Go to Login Activity
            Log.d("loginCheck", "Not logged in. Go to Login Activity");
            Intent LoginIntent = new Intent(LoginCheck.this,LoginActivity.class);
            startActivity(LoginIntent);
            LoginCheck.this.finish();
        } else if (fbToken != null) {
            //Facebook logged in. Load Facebook info into currentUser and go to Main Activity
            Log.d("loginCheck", "Facebook logged in. Load Facebook info into currentUser and go to Main Activity");
            GraphRequest req = GraphRequest.newMeRequest(fbToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Intent MainIntent = new Intent(LoginCheck.this, MainActivity.class);
                    Log.d("loginCheck", "Facebook object:" + object.toString());
                    firstName = object.getString("first_name");
                    lastName = object.getString("last_name");
                    emailAddress = object.getString("email");
                    profilePicURL = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    Log.d("login", profilePicURL);
                    facebookID = object.getString("id");
                    Member member = new Member("", firstName, lastName, emailAddress, "");
                    member.setProfilePicURL(profilePicURL);
                    member.setFacebookID(facebookID);
                    User.setCurrentUser(member);
                    Toast.makeText(LoginCheck.this, User.currentUser.getFormattedFullName() + " Signed in successfully", Toast.LENGTH_LONG).show();
                    startActivity(MainIntent);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, picture");
            req.setParameters(parameters);
            req.executeAsync();
            LoginCheck.this.finish();
        } else if (googleAccount != null) {
            //Google logged in. Load Google info into currentUser and go to Main Activity
            Log.d("loginCheck", "Google logged in. Load Google info into currentUser and go to Main Activity");
            firstName = googleAccount.getGivenName();
            lastName = googleAccount.getFamilyName();
            emailAddress = googleAccount.getEmail();
            profilePicURL = googleAccount.getPhotoUrl().toString();
            googleID = googleAccount.getId();
            Member member = new Member("", firstName, lastName, emailAddress, "");
            member.setProfilePicURL(profilePicURL);
            member.setGoogleID(googleID);
            User.setCurrentUser(member);
            Toast.makeText(LoginCheck.this, User.currentUser.getFormattedFullName() + " Signed in successfully", Toast.LENGTH_LONG).show();
            Intent MainIntent = new Intent(LoginCheck.this, MainActivity.class);
            startActivity(MainIntent);
            LoginCheck.this.finish();
        }


    }

}
