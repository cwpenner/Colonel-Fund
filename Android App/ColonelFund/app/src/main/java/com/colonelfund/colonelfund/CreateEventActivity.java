package com.colonelfund.colonelfund;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }
}
