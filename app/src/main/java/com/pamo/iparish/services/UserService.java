package com.pamo.iparish.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pamo.iparish.HomeActivity;
import com.pamo.iparish.R;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserService extends Service {

  FirebaseAuth mFirebaseAuth;
  FirebaseFirestore fStore;
  EditText emailId, password;
  String userID;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  public void validateForm(String email, String pwd, View view, Activity activity) {

    emailId = view.findViewById(R.id.editText);
    fStore = FirebaseFirestore.getInstance();

    if (email.isEmpty()) {
      emailId.setError(Resources.getSystem().getString(R.string.enter_email));
      emailId.requestFocus();
    } else if (pwd.isEmpty()) {
      password.setError(Resources.getSystem().getString(R.string.enter_password));
      password.requestFocus();
    } else if (email.isEmpty() && pwd.isEmpty()) {
      Toast.makeText(activity, activity.getString(R.string.error_empty), Toast.LENGTH_SHORT).show();

    } else if (!(email.isEmpty() && pwd.isEmpty())) {
      if (activity.getLocalClassName().equals("register.MainActivity")) {
        createUser(email, pwd, activity);
      } else {
        signInUser(email, pwd, activity);
      }

    } else {
      Toast.makeText(activity, activity.getString(R.string.error), Toast.LENGTH_SHORT).show();
    }
  }

  public void createUser(String email, String pwd, Activity activity) {

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
      if (!task.isSuccessful()) {
        Toast.makeText(activity, activity.getString(R.string.error_again), Toast.LENGTH_SHORT).show();
      } else {
        userID = mFirebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        documentReference.set(user).addOnSuccessListener((OnSuccessListener) (aVoid) -> {
          Log.d(TAG, "onSuccess: " + userID);
          activity.startActivity(new Intent(activity, HomeActivity.class));
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));
      }
    });
  }

  public void signInUser(String email, String pwd, Activity activity) {

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
      if (!task.isSuccessful()) {
        Toast.makeText(activity, activity.getString(R.string.error_again), Toast.LENGTH_SHORT).show();
      } else {
        Intent intToHome = new Intent(activity, HomeActivity.class);
        activity.startActivity(intToHome);
      }
    });
  }
}
