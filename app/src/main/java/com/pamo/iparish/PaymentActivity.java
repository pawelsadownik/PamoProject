package com.pamo.iparish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

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

    setFormData(cardDocument);

    payment_amount = findViewById(R.id.payment_amount);
    payment_currency = findViewById(R.id.payment_currency);

    //TO DO: set price for each product
    payment_amount.setText(Integer.toString(money));

    cardForm = findViewById(R.id.card_form);
    buy = findViewById(R.id.btnBuy);
    save = findViewById(R.id.btnSave);

    cardForm.cardRequired(true)
      .expirationRequired(true)
      .cvvRequired(true)
      .postalCodeRequired(true)
      .mobileNumberRequired(true)
      .mobileNumberExplanation("SMS is required on this number")
      .setup(PaymentActivity.this);

    cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

    if (money == 0) {
      buy.setVisibility(View.GONE);
      payment_currency.setVisibility(View.GONE);
      payment_amount.setVisibility(View.GONE);
      save(cardDocument);
    } else {
      save.setVisibility(View.GONE);
      buy(cardDocument);
    }
  }

  public void buy(DocumentReference cardDocument) {
    buy.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (cardForm.isValid()) {
          alertBuilder = new AlertDialog.Builder(PaymentActivity.this);
          alertBuilder.setTitle("Confirm before purchase");
          alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
            "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
            "Card CVV: " + cardForm.getCvv() + "\n" +
            "Postal code: " + cardForm.getPostalCode() + "\n" +
            "Phone number: " + cardForm.getMobileNumber());
          alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
              Toast.makeText(PaymentActivity.this, "Thank you for purchase", Toast.LENGTH_LONG).show();
              finish();
            }
          });
          alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
            }
          });
          AlertDialog alertDialog = alertBuilder.create();
          alertDialog.show();

        } else {
          Toast.makeText(PaymentActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
        }
      }
    });
  }


  public void save(DocumentReference cardDocument) {
    save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        checkValidation(cardDocument);
      }
    });
  }

  public void saveData(DocumentReference cardDocument) {
    Map<String, Object> user = new HashMap<>();
    user.put("cardNUmber", mCardNumber.getText().toString());
    user.put("expiration", mExpiration.getText().toString());
    user.put("cvv", mCvv.getText().toString());
    user.put("postalCode", mPostalCode.getText().toString());
    user.put("countryCode", mCountryCode.getText().toString());

    cardDocument.set(user).addOnSuccessListener((OnSuccessListener) (aVoid) -> {
      Log.d(TAG, "onSuccess: " + userID);
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "onFailure: " + e.toString());
      }
    });
  }

  public void checkValidation(DocumentReference cardDocument) {
    if (cardForm.isValid()) {
      saveData(cardDocument);
      Toast.makeText(PaymentActivity.this, "Data updated", Toast.LENGTH_LONG).show();
      finish();

    } else {
      Toast.makeText(PaymentActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
    }
  }

  public void setFormData(DocumentReference cardDocument) {

    fStore.collection("users")
      .get()
      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
              mMobileNumber.setText(document.getString("phoneNumber"));
            }

            cardDocument.addSnapshotListener(PaymentActivity.this, new EventListener<DocumentSnapshot>() {
              @Override
              public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                mCardNumber.setText(documentSnapshot.getString("cardNUmber"));
                mExpiration.setText(documentSnapshot.getString("expiration"));
                mCvv.setText(documentSnapshot.getString("cvv"));
                mPostalCode.setText(documentSnapshot.getString("postalCode"));
                mCountryCode.setText(documentSnapshot.getString("countryCode"));
              }
            });

          } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
          }
        }
      });
  }
}
