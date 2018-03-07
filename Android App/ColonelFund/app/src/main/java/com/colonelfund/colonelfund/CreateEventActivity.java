package com.colonelfund.colonelfund;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity for creating an event
 */
public class CreateEventActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();

    private EditText txtEventDescription, txtEventTitle, txtEventGoal;
    private EditText txtEventMember, txtEventDate, txtEventType;
    private Button btnCreateEvent;
    private final String TAG = "CreateEventActivity";
    private final String URL_FOR_CREATE_EVENT = "https://wesll.com/colonelfund/create_event.php";
    private ImageView imageView;
    ProgressDialog progressDialog;

    /**
     * Sets information for creating event.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        txtEventTitle = (EditText) findViewById(R.id.txtEventTitle);
        txtEventMember = (EditText) findViewById(R.id.txtEventMember);
        txtEventDate = (EditText) findViewById(R.id.txtEventDate);
        txtEventType = (EditText) findViewById(R.id.txtEventType);
        txtEventGoal = (EditText) findViewById(R.id.txtEventGoal);
        txtEventDescription = (EditText) findViewById(R.id.txtEventDescription);

//        String fixingGoal = txtEventGoal.getText().toString().replace(",", "");
//        final String finalGoal = fixingGoal.replace("$", "");

        imageView = (ImageView) findViewById(R.id.imageView);
        btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent(txtEventTitle.getText().toString(), txtEventMember.getText().toString(),
                        txtEventDate.getText().toString(), txtEventGoal.getText().toString(),
                        txtEventDescription.getText().toString(), txtEventType.getText().toString(), imageView);
            }
        });
    }

    private void createEvent(final String eventTitle, final String eventMember,
                             final String eventDate, final String eventGoal,
                             final String eventDescription, final String eventType,
                             final ImageView imageView) {

        String cancel_event_tag = "register";

        progressDialog.setMessage("Creating Event...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_CREATE_EVENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Event Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String title = jObj.getJSONObject("event").getString("title");
                        Toast.makeText(getApplicationContext(), "Event: \"" + title +
                                "\" successfully added " + eventTitle, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CreateEventActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Event Creation Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to create event url
                Map<String, String> params = new HashMap<String, String>();

                params.put("title", eventTitle);
                params.put("associatedMember", eventMember);
                params.put("eventDate", eventDate);
                params.put("fundGoal", eventGoal);
                params.put("currentFunds", "0");
                params.put("description", eventDescription);
                params.put("type", eventType);
                return params;
            }
        };
        // Adding request to request queue
        new AppSingleton(getApplicationContext()).getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_event_tag);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_you) {
            Intent intent = new Intent(this, ViewProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.your_history_events) {
            Intent intent = new Intent(this, MyHistoryEventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout_item) {
            AccessToken token = AccessToken.getCurrentAccessToken();
            if(token != null) {
                LoginManager.getInstance().logOut();
            }
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
