package com.pamo.iparish;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.pamo.iparish.home.HomeActivity;
import com.pamo.iparish.services.UserService;

public class MainActivity extends AppCompatActivity {

  AuthStateListener mAuthStateListener = this::onAuthStateChanged;
  UserService userService;
  FirebaseAuth mFirebaseAuth;

  EditText emailId;
  EditText password;
  Button btnSignIn;
  TextView tvSignUp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    emailId = findViewById(R.id.edittext_login);
    password = findViewById(R.id.editext_password);
    btnSignIn = findViewById(R.id.button_login);
    tvSignUp = findViewById(R.id.textview_register);

    userService = new UserService();
    mFirebaseAuth = FirebaseAuth.getInstance();

    btnSignIn.setOnClickListener(v -> validateForm());

    tvSignUp.setOnClickListener(v -> validateForm(true));
  }

  @Override
  protected void onStart() {
    super.onStart();
    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
  }
  @Override
  protected void onDestroy() {
    super.onDestroy();
    mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
  }

  private void onAuthStateChanged(FirebaseAuth firebaseAuth) {
    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    if (mFirebaseUser != null) {
      Toast.makeText(MainActivity.this, getString(R.string.toast_Logged), Toast.LENGTH_SHORT).show();

      Intent intToHome = new Intent(MainActivity.this, HomeActivity.class);
      intToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intToHome);
      finish();
    } else {
      Toast.makeText(MainActivity.this, getString(R.string.toast_Login), Toast.LENGTH_SHORT).show();
    }
  }

  private void validateForm(boolean createUser) {
    String email = emailId.getText().toString();
    String pwd = password.getText().toString();

    if (email.isEmpty()) {
      emailId.setError(this.getString(R.string.enter_email));
      emailId.requestFocus();
    } else if (pwd.isEmpty()) {
      password.setError(this.getString(R.string.enter_password));
      password.requestFocus();
    } else {
      if (createUser) {
        userService.createUser(email, pwd, MainActivity.this);
      } else {
        userService.signInUser(email, pwd, MainActivity.this);
      }
    }
  }

  public void validateForm() {
    this.validateForm(false);
  }
}
