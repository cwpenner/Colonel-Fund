package com.colonelfund.colonelfund;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Calendar;

/**
 * Activity for creating an event
 */
public class CreateEventActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();

    /**
     * Sets information for creating event.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }
}
