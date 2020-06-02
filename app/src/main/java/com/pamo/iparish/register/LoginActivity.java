package com.pamo.iparish.register;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pamo.iparish.HomeActivity;
import com.pamo.iparish.R;
import com.pamo.iparish.services.UserService;

/**
 * Part of UserAuthentication
 * Uses injection of UserService for further actions
 *
 * @see UserService
 */
public class LoginActivity extends AppCompatActivity {

  FirebaseAuth mFirebaseAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    View view = findViewById(android.R.id.content).getRootView();

    UserService userService = new UserService();

    EditText emailId = findViewById(R.id.editText);
    EditText password = findViewById(R.id.editText2);
    Button btnSignIn = findViewById(R.id.button2);
    TextView tvSignUp = findViewById(R.id.textView);

    mFirebaseAuth = FirebaseAuth.getInstance();
    mAuthStateListener = firebaseAuth -> {
      FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
      if (mFirebaseUser != null) {
        Toast.makeText(LoginActivity.this, getString(R.string.toast_Logged), Toast.LENGTH_SHORT).show();

        Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
        intToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intToHome);
        finish();
      } else {
        Toast.makeText(LoginActivity.this, getString(R.string.toast_Login), Toast.LENGTH_SHORT).show();
      }
    };

    btnSignIn.setOnClickListener(v -> {
      String email = emailId.getText().toString();
      String pwd = password.getText().toString();
      userService.validateForm(email, pwd, view, LoginActivity.this);
    });

    tvSignUp.setOnClickListener(v -> {
      Intent intSignUp = new Intent(LoginActivity.this, MainActivity.class);
      intSignUp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intSignUp);
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
  }
}
