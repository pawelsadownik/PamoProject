package com.pamo.iparish.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pamo.iparish.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {
  private long mLastClickTime = 0;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_settings, container, false);

    Button settingsButton = view.findViewById(R.id.button_settings);
    Button paymentButton = view.findViewById(R.id.button_payment);
    Button darkmodeButton = view.findViewById(R.id.dark_mode);

    settingsButton.setOnClickListener(this);
    paymentButton.setOnClickListener(this);
    darkmodeButton.setOnClickListener(this);

    return view;
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_settings:
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
          return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        NavHostFragment.findNavController(SettingsFragment.this)
          .navigate(R.id.action_settingsFragment_to_userFormFragment);
        break;
      case R.id.button_payment:
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
          return;
        }
        Intent i = new Intent(getActivity(), PaymentActivity.class);
        i.putExtra("money", Integer.toString(0));
        startActivity(i);
        break;
      case R.id.dark_mode:
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
          return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        NavHostFragment.findNavController(SettingsFragment.this)
          .navigate(R.id.action_global_darkInitFragment);
        break;
    }
  }
}
