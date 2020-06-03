package com.pamo.iparish.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pamo.iparish.R;
import com.pamo.iparish.home.HomeActivity;
import com.pamo.iparish.model.Parish;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
/**
 * Service for user registration and login
 *
 * This service is validating form and determining which action call
 * @see com.pamo.iparish.MainActivity
 */
public class UserService extends Service {

  FirebaseAuth mFirebaseAuth;
  FirebaseFirestore fStore;
  String userID;

  public UserService() {
    mFirebaseAuth = FirebaseAuth.getInstance();
    fStore = FirebaseFirestore.getInstance();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  public void createUser(String email, String pwd, Activity activity) {

    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
      if (!task.isSuccessful()) {
        Toast.makeText(activity, activity.getString(R.string.error_again), Toast.LENGTH_SHORT).show();
      } else {
        userID = mFirebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        documentReference.set(user).addOnSuccessListener(aVoid -> {
          Log.d(TAG, "onSuccess: " + userID);
          activity.startActivity(new Intent(activity, HomeActivity.class));
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));
      }
    });
  }

  public void signInUser(String email, String pwd, Activity activity) {

    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
      if (!task.isSuccessful()) {
        Toast.makeText(activity, activity.getString(R.string.error_again), Toast.LENGTH_SHORT).show();
      } else {
        Intent intToHome = new Intent(activity, HomeActivity.class);
        intToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intToHome);
        activity.finish();
      }
    });
  }

  public Parish getUserChurch(String userID) {
    Parish parish = new Parish();
    fStore.collection("users").document(userID).get()
            .addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                  Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                  parish.setId(document.getString("parish"));
                  parish.setName(document.getString("church"));
                  parish.setLocation(document.getGeoPoint("location"));
                } else {
                  Log.d(TAG, "No such document");
                }
              } else {
                Log.d(TAG, "get failed with ", task.getException());
              }
            });
    return parish;
  }
}
