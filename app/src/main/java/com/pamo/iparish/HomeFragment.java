package com.pamo.iparish;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private long mLastClickTime = 0;
    FirebaseAuth fAuth;
    String userId;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_quick_offering).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_QuickOfferingFragment);
            }
        });
        view.findViewById(R.id.button_custom_offering).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_CustomOfferingFragment);
            }
        });

      view.findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          // mis-clicking prevention, using threshold of 1000 ms
          if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
          }
          mLastClickTime = SystemClock.elapsedRealtime();

          NavHostFragment.findNavController(HomeFragment.this)
            .navigate(R.id.action_HomeFragment_to_userFormFragment);
        }
      });

      view.findViewById(R.id.button_payment).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent i = new Intent(getActivity(),PaymentActivity.class);
          startActivity(i);
        }
      });
    }
}
