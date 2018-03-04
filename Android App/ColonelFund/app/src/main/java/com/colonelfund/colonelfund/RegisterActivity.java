package com.colonelfund.colonelfund;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtRegUserID, txtRegFirstName, txtRegLastName, txtRegEmail, txtRegPhoneNumber, txtRegPassword;
    private View register_progress, register_form;
    private Button btnSubmitRegister;
    private final String TAG = "RegisterActivity";
    private final String URL_FOR_REGISTRATION = "https://wesll.com/colonelfund/register.php";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        register_form = findViewById(R.id.register_form);
        register_progress = findViewById(R.id.register_progress);

        txtRegUserID = (EditText) findViewById(R.id.txtRegUserID);
        txtRegFirstName = (EditText) findViewById(R.id.txtRegFirstName);
        txtRegLastName = (EditText) findViewById(R.id.txtRegLastName);
        txtRegEmail = (EditText) findViewById(R.id.txtRegEmail);
        txtRegPassword = (EditText) findViewById(R.id.txtRegPassword);
        txtRegPhoneNumber = (EditText) findViewById(R.id.txtRegPhoneNumber);

        btnSubmitRegister = (Button) findViewById(R.id.btnSubmitRegister);

        btnSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(txtRegUserID.getText().toString(), txtRegFirstName.getText().toString(),
                        txtRegLastName.getText().toString(), txtRegEmail.getText().toString(),
                        txtRegPassword.getText().toString(), txtRegPhoneNumber.getText().toString());
            }
        });
    }

    private void registerUser(final String userID, final String firstName,
                              final String lastName, final String emailAddress,
                              final String password, final String phoneNumber) {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String user = jObj.getJSONObject("user").getString("userID");

                        Toast.makeText(getApplicationContext(), "Hi " + user +
                                ", You are successfully Added!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("userID", userID);
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("emailAddress", emailAddress);
                params.put("password", password);
                params.put("phoneNumber", phoneNumber);
                return params;
            }
        };
        // Adding request to request queue
        new AppSingleton(getApplicationContext()).getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
