package com.colonelfund.colonelfund;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import static android.app.Activity.RESULT_OK;

/**
 * Fragment that allows a user to create an event. Instantiated in Main Activity.
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    final private Calendar myCalendar = Calendar.getInstance();
    private EditText txtEventDescription, txtEventTitle, txtEventGoal;
    private EditText txtEventTime, txtEventDate;
    private EditText txtEventAddressLine1, txtEventAddressLine2, txtEventCity, txtEventState, txtEventZip;
    private String eventTypeText = "";
    private Spinner eventTypeSpinner;
    private Button btnCreateEvent;
    private ImageButton imageButton;
    private final String TAG = "CreateEventFragment";
    private final String URL_FOR_CREATE_EVENT = "https://wesll.com/colonelfund/create_event.php";
    private ImageView imageView;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    private static final int PICK_IMAGE = 100;
    Context ctx;
    View createEventView;

    /**
     * Overrides on create to initialize variables for page.
     * @param inflater inflater for fragment views
     * @param container view group for fragment.
     * @param savedInstanceState saved state of fragment.
     * @return view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_create_event, container, false);
        imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
        btnCreateEvent = (Button) rootView.findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(this);
        getActivity().setTitle("Create Event");

        return rootView;
    }

    /**
     * Sets information for creating event.
     * @param savedInstanceState saved state of fragment.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ctx = getActivity();
        createEventView = getView();

        // Progress dialog
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setCancelable(false);

        //sets view info
        txtEventTitle = createEventView.findViewById(R.id.txtEventTitle);
        txtEventDate = createEventView.findViewById(R.id.txtEventDate);
        txtEventTime = createEventView.findViewById(R.id.txtEventTime);
        txtEventAddressLine1 = createEventView.findViewById(R.id.txtAddress1);
        txtEventAddressLine2 = createEventView.findViewById(R.id.txtAddress2);
        txtEventCity = createEventView.findViewById(R.id.txtCity);
        txtEventState = createEventView.findViewById(R.id.txtState);
        txtEventZip = createEventView.findViewById(R.id.txtZipCode);
        eventTypeSpinner = createEventView.findViewById(R.id.eventTypeSpinner);
        txtEventGoal = createEventView.findViewById(R.id.txtEventGoal);
        txtEventGoal.addTextChangedListener(tw);
        txtEventDescription = createEventView.findViewById(R.id.txtEventDescription);
        imageView = createEventView.findViewById(R.id.createEventImage);

        //Date Picker
        final DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, this, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        txtEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        //Time Picker
        final TimePickerDialog timePickerDialog = new TimePickerDialog(ctx, this, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false);
        txtEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        //Event Type Spinner
        ArrayAdapter<CharSequence> eventTypeSpinnerAdapter = ArrayAdapter.createFromResource(ctx, R.array.eventTypes, android.R.layout.simple_spinner_item);
        eventTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(eventTypeSpinnerAdapter);
        eventTypeSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Formats Event Type text on spinner selection
     *
     * @param parent spinner adapter
     * @param view of spinner
     * @param position of selection in list
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        eventTypeText = (String) parent.getItemAtPosition(position);
        Log.d("CreateEvent", "Event Type is: " + eventTypeText);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        btnCreateEvent.setEnabled(false);
    }

    /**
     * Formats Event Date text on date selection
     *
     * @param view of Date Picker
     * @param year selected
     * @param month selected
     * @param dayOfMonth selected
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String monthString = String.valueOf(month + 1);
        String dayString = String.valueOf(dayOfMonth);

        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }

        if (dayString.length() == 1) {
            dayString = "0" + dayString;
        }

        String date = year + "-" + monthString + "-" + dayString;
        txtEventDate.setText(date);
    }

    /**
     * Formats Event Time text on time selection
     *
     * @param view of Time Picker
     * @param hourOfDay selected
     * @param minute selected
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String ampm = "AM";
        String minuteString = String.valueOf(minute);

        if (hourOfDay == 12) {
            ampm = "PM";
        }
        if (hourOfDay > 12) {
            hourOfDay -= 12;
            ampm = "PM";
        } else if (hourOfDay == 0) {
            hourOfDay = 12;
        }

        if (minuteString.length() == 1) {
            minuteString = "0" + minuteString;
        }

        String time = hourOfDay + ":" + minuteString + " " + ampm;
        txtEventTime.setText(time);
    }

    /**
     * On click listener for creating an event.
     * @param v current create event view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateEvent: {
                String strEventTitle = txtEventTitle.getText().toString();
                String strEventMember = User.getCurrentUser().getUserID();
                String strEventDate = txtEventDate.getText().toString();
                String strEventTime = txtEventTime.getText().toString();
                String strAddress1 = txtEventAddressLine1.getText().toString();
                String strAddress2 = txtEventAddressLine2.getText().toString();
                String strCity = txtEventCity.getText().toString();
                String strState = txtEventState.getText().toString();
                String strZip = txtEventZip.getText().toString();
                String strEventGoal = txtEventGoal.getText().toString();
                String strEventDescription = txtEventDescription.getText().toString();
                String strEventType = eventTypeText;
                if (strEventTitle.isEmpty() || strEventMember.isEmpty() ||
                        strEventDate.isEmpty() || strEventGoal.isEmpty() ||
                        strEventDescription.isEmpty() || eventTypeText.equals("") ||
                        strEventTime.isEmpty() || strAddress1.isEmpty() ||
                        strCity.isEmpty() || strState.isEmpty() || strZip.isEmpty()) {
                    Log.e(TAG, "Event Creation Error: ");
                    Toast.makeText(ctx, "field contains empty value, Event not created", Toast.LENGTH_LONG).show();
                    hideDialog();
                } else {
                    createEvent(strEventTitle, strEventMember, strEventDate, strEventGoal,
                            strEventDescription, strEventType, strEventTime, strAddress1, strAddress2, strCity, strState, strZip);
                }
                Intent MainIntent = new Intent(ctx, MainActivity.class);
                startActivity(MainIntent);
                break;
            }
            case R.id.imageButton: {
                openGallery();
                break;
            }
        }
    }

    /**
     * Converts a bitmap image as string.
     * @param bitmap image for event
     * @return string of event picture
     */
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

    /**
     *  Creates an event from provided variables.
     * @param eventTitle event title
     * @param eventMember event member
     * @param eventDate event date
     * @param eventGoal event goal $
     * @param eventDescription event description
     * @param eventType event type
     * @param eventTime event time
     * @param addressLine1 event address line 1
     * @param addressLine2 event address line 2
     * @param city event address city
     * @param state event address state
     * @param zip event address zip
     */
    private void createEvent(final String eventTitle, final String eventMember,
                             final String eventDate, final String eventGoal,
                             final String eventDescription, final String eventType,
                             final String eventTime, final String addressLine1,
                             final String addressLine2, final String city,
                             final String state, final String zip) {
        final Address address = new Address(addressLine1, addressLine2, city, state, zip);

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
                params.put("eventTime", eventTime);
                params.put("address", address.toString());
                return params;
            }
        };
        // Adding request to request queue
        new AppSingleton(ctx).getInstance(ctx).addToRequestQueue(strReq, cancel_event_tag);
    }

    /**
     * Opens gallery for image selection.
     */
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    /**
     * Listener for activity result progress.
     * @param requestCode request code
     * @param resultCode result code
     * @param data data for request
     */
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

    /**
     * Shows progress diag
     */
    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * Hides progress diag
     */
    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    //EditText Input Control
    TextWatcher tw = new TextWatcher() {
        int cursor = -1;
        int initialLength = -1;
        int finalLength = -1;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            cursor = start + count;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String newString = s.toString();
            if (!newString.isEmpty()) {

                int maxDecimalPlaces = 2;
                int currentDecimalPlaces = -1;

                txtEventGoal.removeTextChangedListener(this);

                initialLength = newString.length();

                BigDecimal parsed;
                if (newString.contains(".")) {
                    currentDecimalPlaces = newString.substring(newString.indexOf(".")).length() - 1;
                    if (currentDecimalPlaces >= maxDecimalPlaces) {
                        parsed = new BigDecimal(newString).setScale(maxDecimalPlaces, BigDecimal.ROUND_FLOOR);
                        newString = String.valueOf(parsed);
                    } else if (currentDecimalPlaces == 1) {
                        parsed = new BigDecimal(newString).setScale(currentDecimalPlaces, BigDecimal.ROUND_FLOOR);
                        newString = String.valueOf(parsed);
                    } else {
                        // leave as text
                    }
                } else {
                    // leave as text
                }

                finalLength = newString.length();
                cursor = cursor + finalLength - initialLength;


                txtEventGoal.setText(newString);
                txtEventGoal.setSelection(cursor);

                txtEventGoal.addTextChangedListener(this);
            }
        }
    };
}
