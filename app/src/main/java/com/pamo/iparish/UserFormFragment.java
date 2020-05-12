package com.pamo.iparish;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_list_item_1;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class UserFormFragment extends Fragment {
  FirebaseFirestore fStore;
  FirebaseAuth fAuth;
  EditText phoneNumber;
  EditText name;
  EditText surname;
  Button saveButton;
  String userID;

  Spinner spinner;
  List<String> churches = new ArrayList<>();
  String church;
  ArrayAdapter<String>arrayAdapter;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fAuth = FirebaseAuth.getInstance();
    fStore = FirebaseFirestore.getInstance();
    userID = fAuth.getCurrentUser().getUid();

    fStore.collection("churches")
      .get()
      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {

          for (QueryDocumentSnapshot document : task.getResult()) {
            churches.add(document.getString("name"));
          }
        } else {
          Log.d(TAG, "Error getting documents: ", task.getException());
        }
      }
    });

    arrayAdapter = new ArrayAdapter<String>( getActivity(), simple_list_item_1, churches);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_user_form, container, false);
    spinner = view.findViewById(R.id.spinner);
    spinner.setAdapter(arrayAdapter);

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

    phoneNumber = view.findViewById(R.id.phoneNumber);
    name = view.findViewById(R.id.name);
    surname = view.findViewById(R.id.surname);
    saveButton = view.findViewById(R.id.saveButton);

    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name.getText().toString());
        user.put("surname", surname.getText().toString());
        user.put("phoneNumber", phoneNumber.getText().toString());
        user.put("email", fAuth.getCurrentUser().getEmail());
        user.put("church", church);

        try {
          fStore.collection("users")
            .add(user)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
              @Override
              public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                NavHostFragment.findNavController(UserFormFragment.this)
                  .navigate(R.id.action_userFormFragment_to_HomeFragment);
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
              }
            });
        } catch (Exception e) {
          Log.d(TAG, "Exception : " + e.toString());
        }


      }
    });

    return view;
  }
}
