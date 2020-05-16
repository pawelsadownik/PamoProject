package com.pamo.iparish;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
  private long mLastClickTime = 0;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    view.findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // mis-clicking prevention, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
          return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        NavHostFragment.findNavController(SettingsFragment.this)
                .navigate(R.id.action_settingsFragment_to_userFormFragment);
      }
    });

    view.findViewById(R.id.button_payment).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getActivity(),PaymentActivity.class);
        i.putExtra("money", Integer.toString(0));
        startActivity(i);
      }
    });

    view.findViewById(R.id.dark_mode).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // mis-clicking prevention, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
          return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        NavHostFragment.findNavController(SettingsFragment.this)
                .navigate(R.id.action_global_darkInitFragment);
      }
    });
  }
}
