package com.pamo.iparish.register;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pamo.iparish.R;
import com.pamo.iparish.services.UserService;

/**
 * Part of UserAuthentication
 * Uses injection of UserService for further actions
 *
 * @see UserService
 */

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    View view = findViewById(android.R.id.content).getRootView();

    UserService userService = new UserService();

    EditText emailId = findViewById(R.id.editText);
    EditText password = findViewById(R.id.editText2);
    Button btnSignUp = findViewById(R.id.button2);
    TextView tvSignIn = findViewById(R.id.textView);

    btnSignUp.setOnClickListener(v -> {
      String email = emailId.getText().toString();
      String pwd = password.getText().toString();

      userService.validateForm(email, pwd, view, MainActivity.this);
    });

    tvSignIn.setOnClickListener(v -> {
      Intent i = new Intent(MainActivity.this, LoginActivity.class);
      startActivity(i);
    });
  }
}
