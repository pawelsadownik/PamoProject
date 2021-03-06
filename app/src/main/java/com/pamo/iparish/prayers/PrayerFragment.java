package com.pamo.iparish.prayers;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

/**
 * This fragment allows user to read a prayer chosen by God - straight from heaven
 *
 * @author GOD
 */
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
            setPrayer(task, i);
          } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
          }
        }
      });

    return view;
  }

  /**
   * This method gets a prayer from firebase storage (heaven database)
   *
   * @param i This is number chosen by God
   */
  public void setPrayer(Task<QuerySnapshot> task, int i) {

    DocumentSnapshot document = task.getResult().getDocuments().get(i);
    prayer.setText(document.getString("content"));
    title.setText(document.getString("title"));
  }
}
