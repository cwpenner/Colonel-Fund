package com.colonelfund.colonelfund;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.GooglePayment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.BraintreePaymentResultListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.BraintreePaymentResult;
import com.braintreepayments.api.models.CardNonce;
import com.braintreepayments.api.models.GooglePaymentCardNonce;
import com.braintreepayments.api.models.GooglePaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;

import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
import com.loopj.android.http.*;

import java.math.BigDecimal;

import cz.msebera.android.httpclient.Header;

public class BraintreeActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,
        PaymentMethodNonceCreatedListener, BraintreeCancelListener, BraintreeErrorListener,
        BraintreePaymentResultListener {

    static final String EXTRA_PAYMENT_RESULT = "payment_result";
    static final String EXTRA_DEVICE_DATA = "device_data";
    static final String EXTRA_COLLECT_DEVICE_DATA = "collect_device_data";
    static final String EXTRA_ANDROID_PAY_CART = "android_pay_cart";

    private static final int DROP_IN_REQUEST = 1;
    private static final int ANDROID_PAY_REQUEST = 2;
    private static final int GOOGLE_PAYMENT_REQUEST = 3;
    private static final int CARDS_REQUEST = 4;
    private static final int PAYPAL_REQUEST = 5;
    private static final int VENMO_REQUEST = 6;
    private static final int VISA_CHECKOUT_REQUEST = 7;
    private static final int IDEAL_REQUEST = 8;

    private static final String KEY_NONCE = "nonce";

    //Constructor
    public void BraintreeActivityInitializer(EditText donationTextField, Button donateButton, TextView paymentDescriptionLabel, Button selectPaymentButton, ImageView paymentIconView) {
        this.donationTextField = donationTextField;
        this.donateButton = donateButton;
        this.paymentDescriptionLabel = paymentDescriptionLabel;
        this.selectPaymentButton = selectPaymentButton;
        this.paymentIconView = paymentIconView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //In activities using this class, setContentView needs to be the first line, in order for corresponding labels, images, and buttons to be properly manipulated by this class.
        //Respective labels, images, and buttons must then be initialized using findViewById
        //Finally, BraintreeActivityInitializer needs to be called
        //All of this needs to be done **BEFORE** super.onCreate is called to ensure null references aren't passed
        super.onCreate(savedInstanceState);

        donationTextField.addTextChangedListener(tw);

        this.fetchExistingPaymentMethod(this, token);

    }

    //Properties
    private double donationAmount = 0.00;
    private String memberName = ""; //Used for creating description for Google Pay //TODO: consider changing to Member object
    private String eventTitle = ""; //Used for creating description for Google Pay //TODO: consider changing to Event object

    //API Tokenization Key
    private String token = "sandbox_3swsvvz5_mhbr9s54673smz3g";

    //Payment info
    private PaymentMethodNonce paymentMethod;
    private boolean useGooglePay = false;
    private String googlePayNonce = "";

    //UI Components
    private EditText donationTextField;
    private Button donateButton;
    private TextView paymentDescriptionLabel;
    private Button selectPaymentButton;
    private ImageView paymentIconView;

    //Activity Methods
    public void donateButtonPressed() {
        donationAmount = Double.parseDouble(donationTextField.getText().toString());
        String nonce = "";

        if (this.useGooglePay) {
            try {
                BraintreeFragment btf = BraintreeFragment.newInstance(this, token);
                GooglePaymentRequest gpr = getGooglePaymentRequest(String.valueOf(donationAmount));
                GooglePayment.requestPayment(btf, gpr);
                nonce = googlePayNonce;
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            }
        } else {
            nonce = paymentMethod.getNonce();
        }
        Log.d("Donation", "Nonce: " + nonce);
        sendRequestPaymentToServer(nonce, String.valueOf(donationAmount));
    }

    public void selectPaymentButtonPressed() {
        launchDropIn();
    }

    //Braintree Functions
    public void launchDropIn() {
        startActivityForResult(getDropInRequest().getIntent(this), DROP_IN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("DropIn", "requestCode: " + requestCode + ", resultCode: " + resultCode);
        if (requestCode == DROP_IN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                if (result.getPaymentMethodType() == PaymentMethodType.GOOGLE_PAYMENT) {
                    this.setupGooglePay();
                } else {
                    this.useGooglePay = false;
                    this.paymentMethod = result.getPaymentMethodNonce();
                    this.updatePaymentMethod(this.paymentMethod);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("DropIn","User cancelled drop in selection");
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                Log.d("DropIn", "Google Pay");
                this.setupGooglePay();
            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("DropIn","Error selecting drop in: " + error.getMessage());
            }
        }
    }

    //Retrieves previously used payment methods, if client token is used
    public void fetchExistingPaymentMethod(Activity activity, String clientToken) {
        DropInResult.fetchDropInResult(activity, clientToken, new DropInResult.DropInResultListener() {
            @Override
            public void onError(Exception exception) {
                Log.d("DropInFetch", "Error fetching previous payment method");
            }

            @Override
            public void onResult(DropInResult result) {
                if (result.getPaymentMethodType() != null) {
                    if (result.getPaymentMethodType() == PaymentMethodType.GOOGLE_PAYMENT) {
                        setupGooglePay();
                    } else {
                        useGooglePay = false;
                        paymentMethod = result.getPaymentMethodNonce();
                        updatePaymentMethod(paymentMethod);
                    }
                } else {
                    Log.d("DropInFetch", "No previous payment method exists");
                }
            }
        });
    }

    public void updatePaymentMethod(PaymentMethodNonce paymentMethodNonce) {
        if (!paymentMethodNonce.getNonce().equals("")) {
            this.paymentIconView.setVisibility(View.VISIBLE);
            this.paymentDescriptionLabel.setVisibility(View.VISIBLE);
            PaymentMethodType paymentMethodType = PaymentMethodType.forType(paymentMethodNonce);
            this.paymentIconView.setImageResource(paymentMethodType.getDrawable());
            this.paymentDescriptionLabel.setText(paymentMethodNonce.getDescription());
            this.selectPaymentButton.setText(getResources().getString(R.string.changePaymentMethod));
            if (!this.donationTextField.getText().toString().equalsIgnoreCase("")) {
                this.donateButton.setEnabled(true);
            }
        } else {
            this.paymentIconView.setVisibility(View.INVISIBLE);
            this.paymentDescriptionLabel.setVisibility(View.INVISIBLE);
        }
    }

    public void setupGooglePay() {
        this.paymentIconView.setVisibility(View.VISIBLE);
        this.paymentDescriptionLabel.setVisibility(View.VISIBLE);
        PaymentMethodType paymentMethodType = PaymentMethodType.GOOGLE_PAYMENT;
        this.paymentIconView.setImageResource(paymentMethodType.getDrawable());
        this.paymentDescriptionLabel.setText(paymentMethodType.getLocalizedName());
        //this.paymentDescriptionLabel.setText(getResources().getString(R.string.googlePay));
        this.selectPaymentButton.setText(getResources().getString(R.string.changePaymentMethod));
        this.useGooglePay = true;
        if (!donationTextField.getText().toString().equalsIgnoreCase("")) {
            this.donateButton.setEnabled(true);
        }
    }

    public void sendRequestPaymentToServer(String nonce, String amount) {
        //**Change with own server URL
        String paymentURL = "https://localhost/braintree/pay.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("payment_method_nonce", nonce);
        params.put("payment_amount", amount);
        client.post(paymentURL, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "Successfully charged.",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "Transaction failed. Please try again.",Toast.LENGTH_LONG).show();
                        Log.d("transactionError", "Error: " + error.getMessage());
                    }
                    // Your implementation here
                }
        );
        //TODO: add check for successful transaction
        performIntent();
    }

    private DropInRequest getDropInRequest() {
        DropInRequest dropInRequest = new DropInRequest()
                .tokenizationKey(token);

        return dropInRequest;
    }

    @Override
    public void onCancel(int requestCode) {
        Log.d("onCancel", "Cancel received: " + requestCode);
    }

    @Override
    public void onError(Exception error) {
        Log.d("onError", "Error: " + error.getMessage());
        error.printStackTrace();
    }

    @Override
    public void onBraintreePaymentResult(BraintreePaymentResult result) {
        Log.d("BraintreePaymentResult", "Braintree Payment Result received: " + result.toString());
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        Log.d("PaymentNonceCreated", "Payment Method Nonce received: " + paymentMethodNonce.getTypeLabel());
        if (paymentMethodNonce instanceof GooglePaymentCardNonce) {
            GooglePaymentCardNonce card = (GooglePaymentCardNonce) paymentMethodNonce;
            this.googlePayNonce = card.getNonce();
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

//            String currency = NumberFormat.getCurrencyInstance().getCurrency().toString();
//            String currencySymbol = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
//            int fractionDigits = NumberFormat.getCurrencyInstance().getCurrency().getDefaultFractionDigits();
//            Log.d("TEXTCHANGE","currency: " + currency);
//            Log.d("TEXTCHANGE","currencySymbol: " + currencySymbol);
//            Log.d("TEXTCHANGE","fraction digits: " + fractionDigits);

                donationTextField.removeTextChangedListener(this);

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
                        // leave as eventView
                    }
                } else {
                    // leave as eventView
                }

                finalLength = newString.length();
                cursor = cursor + finalLength - initialLength;


                donationTextField.setText(newString);
                donationTextField.setSelection(cursor);

                donationTextField.addTextChangedListener(this);
            }
            if ((paymentMethod != null && !paymentMethod.getNonce().equalsIgnoreCase("")) || useGooglePay) {
                donateButton.setEnabled(!newString.equals(""));
            }
        }
    };

    //Google Pay
    public GooglePaymentRequest getGooglePaymentRequest(String amount) {
        GooglePaymentRequest googlePaymentRequest = new GooglePaymentRequest()
                .transactionInfo(TransactionInfo.newBuilder()
                        .setTotalPrice(amount)
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        .setCurrencyCode("USD")
                        .build());
        return googlePaymentRequest;
    }


    //Setters for Member Name and Event Title
    public void setMemberName(String newMemberName) {
        memberName = newMemberName;
    }

    public void setEventTitle(String newEventTitle) {
        eventTitle = newEventTitle;
    }

    public void performIntent() {
        Intent intent = new Intent(this, TransactionSummaryActivity.class);
        if (!memberName.equals("")) {
            intent.putExtra("name", "Member: " + memberName);
        } else if (!eventTitle.equals("")) {
            intent.putExtra("name", "Event: " + eventTitle);
        }
        intent.putExtra("amount", "$" + String.valueOf(donationAmount));
        intent.putExtra("paymentDescription", this.paymentDescriptionLabel.getText());
        if (useGooglePay) {
            intent.putExtra("paymentMethodImageType", "useGooglePay");
        } else {
            intent.putExtra("paymentMethodImageType", this.paymentMethod.getTypeLabel());
        }
        intent.putExtra("transactionID",""); //TODO: update with transaction ID from payment server response
        startActivity(intent);
    }
}
