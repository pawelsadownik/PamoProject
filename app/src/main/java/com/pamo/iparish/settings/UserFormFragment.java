package com.pamo.iparish.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pamo.iparish.R;
import com.pamo.iparish.model.Parish;

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

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;
    private DocumentReference usersDocument;

    private List<String> churches = new ArrayList<>();
    private List<Parish> parishes = new ArrayList<>();
    private String church;
    private Parish parish;
    private EditText phoneNumber;
    private EditText name;
    private EditText surname;
    private Button saveButton;
    private Spinner spinner;

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

        phoneNumber = view.findViewById(R.id.edittext_phone);
        name = view.findViewById(R.id.edittext_name);
        surname = view.findViewById(R.id.edittext_surname);
        saveButton = view.findViewById(R.id.button_save);
        spinner = view.findViewById(R.id.spinner_parish);
        usersDocument = fStore.collection("users").document(userID);

        fillChurchesSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                church = churches.get(i);
                parish = parishes.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                church = churches.get(0);
                parish = parishes.get(0);
            }
        });

        saveButton.setOnClickListener(v -> {
            Map<String, Object> user = new HashMap<>();
            user.put("name", name.getText().toString());
            user.put("surname", surname.getText().toString());
            user.put("phoneNumber", phoneNumber.getText().toString());
            user.put("email", fAuth.getCurrentUser().getEmail());
            user.put("church", church);
            user.put("parishId", parish.getId());
            user.put("parishLoc", parish.getLocation());

            userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userID);

            documentReference.set(user).addOnSuccessListener(aVoid -> {
                NavHostFragment.findNavController(UserFormFragment.this)
                        .navigate(R.id.action_userFormFragment_to_HomeFragment);
                Log.d(TAG, "onSuccess: " + userID);
            }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));
        });
        return view;
    }

    private void fillChurchesSpinner() {
        fStore.collection("churches")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            churches.add(document.getString("name"));
                            parishes.add(new Parish(
                                    document.getId(),
                                    document.getString("name"),
                                    document.getGeoPoint("location")
                            ));
                        }
                        if (getActivity() != null) {
                            usersDocument.addSnapshotListener(getActivity(), (documentSnapshot, e) -> {
                                phoneNumber.setText(documentSnapshot.getString("phoneNumber"));
                                name.setText(documentSnapshot.getString("name"));
                                surname.setText(documentSnapshot.getString("surname"));
                                spinner.setSelection(churches.indexOf(documentSnapshot.get("church")));
                            });
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, churches);
                            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spinner.setAdapter(adapter);
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}

