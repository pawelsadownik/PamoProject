package com.pamo.iparish;

import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class FirebaseTests {

  FirebaseAuth fAuth;
  FirebaseFirestore fStore;

  @Before
  public void setUp() {
    FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().getContext());
    fAuth = FirebaseAuth.getInstance();
    fStore = FirebaseFirestore.getInstance();
  }

  @Test
  public void firebaseAuthConnectionTest() {
    assertNotNull(fAuth);
  }

  @Test
  public void firebaseStorageConnectionTest() {
    assertNotNull(fStore);
  }

  @Test
  public void firebaseHostTest() {
    assertEquals("firestore.googleapis.com", fStore.getFirestoreSettings().getHost());
  }

  @Test
  public void firebaseAppPriorityTest() {
    assertTrue(fStore.getApp().isDefaultApp());
  }

  @Test
  public void firebasePackageTest() {
    assertEquals("com.pamo.iparish", fStore.getApp().getApplicationContext().getPackageName());
  }

  @Test
  public void firebaseStorageNameTest() {
    assertEquals("iparish-f5b0a.appspot.com", fStore.getApp().getOptions().getStorageBucket());
  }
}
