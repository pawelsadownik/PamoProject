package com.pamo.iparish.home;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pamo.iparish.R;

public class HomeFragment extends Fragment {

  private long mLastClickTime = 0;

  @Override
  public View onCreateView(
    LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.fragment_home, container, false);
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    view.findViewById(R.id.button_quick_offering).setOnClickListener(view1 -> {
      if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
        return;
      }
      mLastClickTime = SystemClock.elapsedRealtime();

      NavHostFragment.findNavController(HomeFragment.this)
        .navigate(R.id.action_HomeFragment_to_QuickOfferingFragment);
    });
    view.findViewById(R.id.button_custom_offering).setOnClickListener(view12 -> {
      if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
        return;
      }
      mLastClickTime = SystemClock.elapsedRealtime();

      NavHostFragment.findNavController(HomeFragment.this)
        .navigate(R.id.action_HomeFragment_to_CustomOfferingFragment);
    });

    view.findViewById(R.id.button_prayer).setOnClickListener(view13 -> {
      if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
        return;
      }
      mLastClickTime = SystemClock.elapsedRealtime();

      NavHostFragment.findNavController(HomeFragment.this)
        .navigate(R.id.action_HomeFragment_to_prayerFragment);
    });
  }
}
