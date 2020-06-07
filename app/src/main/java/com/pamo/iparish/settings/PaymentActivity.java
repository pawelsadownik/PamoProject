package com.pamo.iparish.settings;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pamo.iparish.R;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * PaymentActivity uses an external library for Credit Card Template
 * It uses firebase storage to get users data
 * This activity is called from offering fragments with money value
 * or from UserFormFragment to save Users Data
 */
public class PaymentActivity extends AppCompatActivity {

    CardForm cardForm;
    Button buy;
    Button save;
    AlertDialog.Builder alertBuilder;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    TextView payment_amount;
    TextView payment_currency;
    String userID;
    int money;

    TextView mMobileNumber;
    TextView mCardNumber;
    TextView mExpiration;
    TextView mCvv;
    TextView mPostalCode;
    TextView mCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();

        DocumentReference cardDocument = fStore.collection("creditCards").document(userID);

        money = Integer.parseInt(getIntent().getStringExtra("money"));

        mCardNumber = findViewById(R.id.bt_card_form_card_number);
        mExpiration = findViewById(R.id.bt_card_form_expiration);
        mCvv = findViewById(R.id.bt_card_form_cvv);
        mPostalCode = findViewById(R.id.bt_card_form_postal_code);
        mCountryCode = findViewById(R.id.bt_card_form_country_code);
        mMobileNumber = findViewById(R.id.bt_card_form_mobile_number);
        payment_amount = findViewById(R.id.payment_amount);
        payment_currency = findViewById(R.id.payment_currency);
        payment_amount.setText(Integer.toString(money));
        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);
        save = findViewById(R.id.btnSave);

        loadCardForm(cardDocument);
    }

    private void loadCardForm(DocumentReference cardDocument) {
        setFormData(cardDocument);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation(getString(R.string.mobile_number_explanation))
                .setup(PaymentActivity.this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        if (money == 0) {
            buy.setVisibility(View.GONE);
            payment_currency.setVisibility(View.GONE);
            payment_amount.setVisibility(View.GONE);
            save(cardDocument);
        } else {
            save.setVisibility(View.GONE);
            buy();
        }
    }

    private void buy() {
        buy.setOnClickListener(view -> {
            if (cardForm.isValid()) {
                alertBuilder = new AlertDialog.Builder(PaymentActivity.this);
                alertBuilder.setTitle(R.string.payment_confirmation_title);
                alertBuilder.setMessage(getString(R.string.card_number) + ": " +
                        cardForm.getCardNumber() + "\n" +
                        getString(R.string.card_expiry_date) + ": " +
                        cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                        getString(R.string.card_cvv) + ": " + cardForm.getCvv() + "\n" +
                        getString(R.string.postal_code) + ": " + cardForm.getPostalCode() + "\n" +
                        getString(R.string.phone_number) + ": " + cardForm.getMobileNumber());
                alertBuilder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Toast.makeText(PaymentActivity.this, R.string.purchase_thanks, Toast.LENGTH_LONG).show();
                    finish();
                });
                alertBuilder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();

            } else {
                Toast.makeText(PaymentActivity.this, R.string.form_not_complete_info, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void save(DocumentReference cardDocument) {
        save.setOnClickListener(view -> checkValidation(cardDocument));
    }

    private void saveData(DocumentReference cardDocument) {
        Map<String, Object> user = new HashMap<>();
        user.put("cardNUmber", cardForm.getCardNumber());
        user.put("expiration", cardForm.getExpirationDateEditText().getText().toString());
        user.put("cvv", cardForm.getCvv());
        user.put("postalCode", cardForm.getPostalCode());
        user.put("countryCode", cardForm.getCountryCode());
        user.put("mobileNumber", cardForm.getMobileNumber());

        cardDocument.set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: " + userID))
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));
    }

    private void checkValidation(DocumentReference cardDocument) {
        if (cardForm.isValid()) {
            saveData(cardDocument);
            Toast.makeText(PaymentActivity.this, R.string.data_updated, Toast.LENGTH_LONG).show();
            finish();

        } else {
            Toast.makeText(PaymentActivity.this, R.string.form_not_complete_info, Toast.LENGTH_LONG).show();
        }
    }

    private void setFormData(DocumentReference cardDocument) {
        cardDocument.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    mCardNumber.setText(document.getString("cardNUmber"));
                    mExpiration.setText(document.getString("expiration"));
                    mCvv.setText(document.getString("cvv"));
                    mPostalCode.setText(document.getString("postalCode"));
                    mCountryCode.setText(document.getString("countryCode"));
                    mMobileNumber.setText(document.getString("mobileNumber"));
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}
