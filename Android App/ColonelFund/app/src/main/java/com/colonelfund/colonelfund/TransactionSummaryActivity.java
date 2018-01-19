package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.braintreepayments.api.dropin.utils.PaymentMethodType;

public class TransactionSummaryActivity extends AppCompatActivity {

    private String name = "";
    private String amount = "";
    private String paymentDescription = "";
    private String paymentMethodImageType = "";
    private String transactionID = "";

    public TextView nameTextView;
    public TextView amountTextView;
    public TextView paymentDescriptionTextView;
    public ImageView paymentMethodImageView;
    public TextView transactionIDTextView;
    public Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_summary);

        nameTextView = findViewById(R.id.nameTextView);
        amountTextView = findViewById(R.id.amountTextView);
        paymentDescriptionTextView = findViewById(R.id.paymentDescriptionTextView);
        paymentMethodImageView = findViewById(R.id.paymentMethodImageView);
        transactionIDTextView = findViewById(R.id.transactionIDTextView);
        continueButton = findViewById(R.id.continueButton);

        getIntentData();

        nameTextView.setText(name);
        amountTextView.setText(amount);
        paymentDescriptionTextView.setText(paymentDescription);
        PaymentMethodType paymentMethodType;
        if (paymentMethodImageType.equalsIgnoreCase("useGooglePay")) {
            paymentMethodType = PaymentMethodType.GOOGLE_PAYMENT;
        } else {
            paymentMethodType = PaymentMethodType.forType(paymentMethodImageType);
        }
        paymentMethodImageView.setImageResource(paymentMethodType.getDrawable());
        transactionIDTextView.setText(transactionID);
    }

    public void getIntentData() {
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            System.out.println("Error getting donation info");
        } else {
            name = extras.getString("name");
            amount = extras.getString("amount");
            paymentDescription = extras.getString("paymentDescription");
            paymentMethodImageType = extras.getString("paymentMethodImageType");
            transactionID = extras.getString("transactionID");
        }
        System.out.println("name: " + name);
        System.out.println("amount: " + amount);
        System.out.println("paymentDescription: " + paymentDescription);
        System.out.println("paymentMethodImageType: " + paymentMethodImageType);
        System.out.println("transactionID: " + transactionID);
    }

    public void returnToMainScreen(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
