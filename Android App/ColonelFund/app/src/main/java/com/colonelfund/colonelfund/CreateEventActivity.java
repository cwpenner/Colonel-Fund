package com.colonelfund.colonelfund;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Activity for creating an event
 */
public class CreateEventActivity extends Fragment implements View.OnClickListener {
    Calendar myCalendar = Calendar.getInstance();

    private EditText txtEventDescription, txtEventTitle, txtEventGoal;
    private EditText txtEventMember, txtEventDate, txtEventType;
    private Button btnCreateEvent;
    private ImageButton imageButton;
    private final String TAG = "CreateEventActivity";
    private final String URL_FOR_CREATE_EVENT = "https://wesll.com/colonelfund/create_event.php";
    private ImageView imageView;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    private static final int PICK_IMAGE = 100;
    Context ctx;
    View createEventView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = (ViewGroup) inflater.inflate(R.layout.activity_create_event, container, false);
        imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
        btnCreateEvent = (Button) rootView.findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(this);

        return rootView;
    }

    /**
     * Sets information for creating event.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ctx = getActivity();
        createEventView = getView();
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_create_event);

        // Progress dialog
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setCancelable(false);

        txtEventTitle = (EditText) createEventView.findViewById(R.id.txtEventTitle);
        txtEventMember = (EditText) createEventView.findViewById(R.id.txtEventMember);
        txtEventDate = (EditText) createEventView.findViewById(R.id.txtEventDate);
        txtEventType = (EditText) createEventView.findViewById(R.id.txtEventType);
        txtEventGoal = (EditText) createEventView.findViewById(R.id.txtEventGoal);
        txtEventDescription = (EditText) createEventView.findViewById(R.id.txtEventDescription);

        imageView = (ImageView) createEventView.findViewById(R.id.imageView);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateEvent: {
                String strEventTitle = txtEventTitle.getText().toString();
                String strEventMember = txtEventMember.getText().toString();
                String strEventDate = txtEventDate.getText().toString();
                String strEventGoal = txtEventGoal.getText().toString();
                String strEventDescription = txtEventDescription.getText().toString();
                String strEventType = txtEventType.getText().toString();
                if (strEventTitle.isEmpty() || strEventMember.isEmpty() ||
                        strEventDate.isEmpty() || strEventGoal.isEmpty() ||
                        strEventDescription.isEmpty() || strEventType.isEmpty()) {
                    Log.e(TAG, "Event Creation Error: ");
                    Toast.makeText(ctx, "field contains empty value, Event not created", Toast.LENGTH_LONG).show();
                    hideDialog();
                } else {
                    createEvent(strEventTitle, strEventMember, strEventDate, strEventGoal,
                            strEventDescription, strEventType);
                }
            }
            case R.id.imageButton: {
                openGallery();
            }
        }
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imgBytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return "default";
        }
    }

    private void createEvent(final String eventTitle, final String eventMember,
                             final String eventDate, final String eventGoal,
                             final String eventDescription, final String eventType) {

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
                        Toast.makeText(ctx, "Event: \"" + title +
                                "\" successfully added " + eventTitle, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ctx, MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(ctx, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Event Creation Error: " + error.getMessage());
                Toast.makeText(ctx,
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
                params.put("image", imageToString(bitmap));
                return params;
            }
        };
        // Adding request to request queue
        new AppSingleton(ctx).getInstance(ctx).addToRequestQueue(strReq, cancel_event_tag);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_you) {
            Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.your_history_events) {
            Intent intent = new Intent(getActivity(), MyHistoryEventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout_item) {
            AccessToken token = AccessToken.getCurrentAccessToken();
            //TODO: Add Google logout code
            if(token != null) {
                LoginManager.getInstance().logOut();
            }
            User.logout();
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
        } else if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                bitmap = Bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), true);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
