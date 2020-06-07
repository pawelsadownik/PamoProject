package com.pamo.iparish;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.pamo.iparish.home.HomeActivity;
import com.pamo.iparish.services.UserService;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

  AuthStateListener mAuthStateListener = this::onAuthStateChanged;
  UserService userService;
  FirebaseAuth mFirebaseAuth;

  EditText emailId;
  EditText password;
  Button btnSignIn;
  TextView tvSignUp;

  private static final Pattern EMAIL_PATTERN = Pattern.compile(
          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                  "\\@" +
                  "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                  "(" +
                  "\\." +
                  "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                  ")+"
  );

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

    btnSignIn.setOnClickListener(v -> btnSignInOnClickListener());

    tvSignUp.setOnClickListener(v -> tvSignUpOnClickListener());
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

  private void submitForm(boolean createUser) {
    String email = emailId.getText().toString();
    String pwd = password.getText().toString();

    if (!isValidEmail(email)) {
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

  private void btnSignInOnClickListener() {
    this.submitForm(false);
  }
  private void tvSignUpOnClickListener() {
    this.submitForm(true);
  }

  public static boolean isValidEmail(CharSequence email) {
    return (email !=null && email.length()>0 && EMAIL_PATTERN.matcher(email).matches());
  }
}
