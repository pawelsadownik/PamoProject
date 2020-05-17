package com.pamo.iparish.register;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pamo.iparish.HomeActivity;
import com.pamo.iparish.R;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity {
  EditText emailId, password;
  Button btnSignUp;
  TextView tvSignIn;
  FirebaseAuth mFirebaseAuth;
  FirebaseFirestore fStore;
  String userID;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mFirebaseAuth = FirebaseAuth.getInstance();
    fStore = FirebaseFirestore.getInstance();
    emailId = findViewById(R.id.editText);
    password = findViewById(R.id.editText2);
    btnSignUp = findViewById(R.id.button2);
    tvSignIn = findViewById(R.id.textView);

    btnSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        if(email.isEmpty()){
          emailId.setError(getString(R.string.enter_email));
          emailId.requestFocus();
        }
        else  if(pwd.isEmpty()){
          password.setError(getString(R.string.enter_password));
          password.requestFocus();
        }
        else  if(email.isEmpty() && pwd.isEmpty()){
          Toast.makeText(MainActivity.this,getString(R.string.error_empty),Toast.LENGTH_SHORT).show();
        }
        else  if(!(email.isEmpty() && pwd.isEmpty())){
          mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(!task.isSuccessful()){
                Toast.makeText(MainActivity.this,getString(R.string.error_again),Toast.LENGTH_SHORT).show();
              }
              else {
                userID = mFirebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("email", email);
                documentReference.set(user).addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                  Log.d(TAG, "onSuccess: " + userID);
                  startActivity(new Intent(MainActivity.this, HomeActivity.class));
                  }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                  }
                });
              }
            }
          });
        }
        else{
          Toast.makeText(MainActivity.this,getString(R.string.error),Toast.LENGTH_SHORT).show();
        }
      }
    });

    tvSignIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
      }
    });
  }
}
