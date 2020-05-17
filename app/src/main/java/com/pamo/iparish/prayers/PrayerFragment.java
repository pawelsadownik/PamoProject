package com.pamo.iparish.prayers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pamo.iparish.R;

import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class PrayerFragment extends Fragment {

  TextView prayer;
  FirebaseFirestore fStore;
  TextView title;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fStore = FirebaseFirestore.getInstance();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_prayer, container, false);

    prayer = view.findViewById(R.id.prayer);
    title = view.findViewById(R.id.title);

    fStore.collection("prayers")
      .get()
      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful()) {
            int i = new Random().nextInt(3);

            DocumentSnapshot document = task.getResult().getDocuments().get(i);

            prayer.setText(document.getString("content"));
            title.setText(document.getString("title"));

          } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
          }
        }
      });

    return view;
  }
}
