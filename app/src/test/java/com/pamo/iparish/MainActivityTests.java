package com.pamo.iparish;

import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTests {
  FirebaseAuth firebaseAuth;
  FirebaseFirestore firestore;

  @Before
  public void setUp() {
    FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().getContext());
    firebaseAuth = FirebaseAuth.getInstance();
    firestore = FirebaseFirestore.getInstance();
  }

  @Test
  public void MainActivityCreationTest() {
    MainActivity activity = Robolectric.setupActivity(MainActivity.class);
    assertNotNull(activity);
  }

  @Test
  public void MainActivityButtonCreationTest() {
    MainActivity activity = Robolectric.setupActivity(MainActivity.class);
    assertNotNull(activity.btnSignIn);
  }

  @Test
  public void MainActivityEditTextCreationTest() {
    MainActivity activity = Robolectric.setupActivity(MainActivity.class);
    assertNotNull(activity.password);
    assertNotNull(activity.emailId);
  }

  @Test
  public void MainActivityEditTextFillingTest() {
    MainActivity activity = Robolectric.setupActivity(MainActivity.class);
    EditText login = activity.findViewById(R.id.edittext_login);
    login.setText("test@test.com");
    EditText passwd = activity.findViewById(R.id.editext_password);
    passwd.setText("12345678");
    assertEquals("test@test.com", login.getText().toString());
    assertEquals("12345678", passwd.getText().toString());
  }

  @Test
  public void MainActivityRegisterText() {
    MainActivity activity = Robolectric.setupActivity(MainActivity.class);
    TextView registerText = activity.findViewById(R.id.textview_register);
    assertEquals("…or click here to sign up now!", registerText.getText().toString());
  }
}
