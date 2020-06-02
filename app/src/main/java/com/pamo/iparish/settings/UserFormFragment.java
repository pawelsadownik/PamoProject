package com.pamo.iparish.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pamo.iparish.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * This fragment get and save Users data
 * Data are user in PaymentActivity to prepare form
 * @see PaymentActivity
 */
public class UserFormFragment extends Fragment {

  FirebaseFirestore fStore;
  FirebaseAuth fAuth;
  String userID;

  List<String> churches = new ArrayList<>();
  String church;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fAuth = FirebaseAuth.getInstance();
    fStore = FirebaseFirestore.getInstance();
    userID = fAuth.getCurrentUser().getUid();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_form, container, false);

    EditText phoneNumber = view.findViewById(R.id.phoneNumber);
    EditText name = view.findViewById(R.id.name);
    EditText surname = view.findViewById(R.id.surname);
    Button saveButton = view.findViewById(R.id.saveButton);
    Spinner spinner = view.findViewById(R.id.spinner);
    DocumentReference usersDocument = fStore.collection("users").document(userID);

    fStore.collection("churches")
      .get()
      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
              churches.add(document.getString("name"));
            }
            usersDocument.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
              @Override
              public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phoneNumber.setText(documentSnapshot.getString("phoneNumber"));
                name.setText(documentSnapshot.getString("name"));
                surname.setText(documentSnapshot.getString("surname"));
                spinner.setSelection(churches.indexOf(documentSnapshot.get("church")));
              }
            });
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, churches);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);

          } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
          }
        }
      });



    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
        church = churches.get(i);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        church = churches.get(0);
      }
    });

    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name.getText().toString());
        user.put("surname", surname.getText().toString());
        user.put("phoneNumber", phoneNumber.getText().toString());
        user.put("email", fAuth.getCurrentUser().getEmail());
        user.put("church", church);

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.set(user).addOnSuccessListener((OnSuccessListener) (aVoid) -> {
          NavHostFragment.findNavController(UserFormFragment.this)
            .navigate(R.id.action_userFormFragment_to_HomeFragment);
          Log.d(TAG, "onSuccess: " + userID);
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.d(TAG, "onFailure: " + e.toString());
          }
        });
      }
    });
    return view;
  }
}

