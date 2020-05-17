package com.pamo.iparish.darkMode;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pamo.iparish.R;


public class DarkInitFragment extends Fragment {

  public DarkInitFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    return inflater.inflate(R.layout.fragment_dark_init, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    view.findViewById(R.id.gifInit).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        NavHostFragment.findNavController(DarkInitFragment.this)
          .navigate(R.id.action_darkInitFragment_to_darkmodeFragment);
      }
    });
  }
}
