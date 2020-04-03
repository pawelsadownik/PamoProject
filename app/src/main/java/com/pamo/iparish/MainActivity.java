package com.pamo.iparish;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
  EditText emailId, password;
  Button btnSignUp;
  TextView tvSignIn;
  FirebaseAuth mFirebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mFirebaseAuth = FirebaseAuth.getInstance();
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
          emailId.setError(Resources.getSystem().getString(R.string.enter_email));
          emailId.requestFocus();
        }
        else  if(pwd.isEmpty()){
          password.setError(Resources.getSystem().getString(R.string.enter_password));
          password.requestFocus();
        }
        else  if(email.isEmpty() && pwd.isEmpty()){
          Toast.makeText(MainActivity.this,Resources.getSystem().getString(R.string.error_empty),Toast.LENGTH_SHORT).show();
        }
        else  if(!(email.isEmpty() && pwd.isEmpty())){
          mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(!task.isSuccessful()){
                Toast.makeText(MainActivity.this,Resources.getSystem().getString(R.string.error_again),Toast.LENGTH_SHORT).show();
              }
              else {
                startActivity(new Intent(MainActivity.this,HomeActivity.class));
              }
            }
          });
        }
        else{
          Toast.makeText(MainActivity.this,Resources.getSystem().getString(R.string.error),Toast.LENGTH_SHORT).show();

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
