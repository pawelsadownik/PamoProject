package com.pamo.iparish.donation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.pamo.iparish.R;
import com.pamo.iparish.settings.PaymentActivity;


public class CustomOfferingFragment extends Fragment implements View.OnClickListener {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_custom_offering, container, false);

    ImageButton confession = view.findViewById(R.id.confession);
    ImageButton funeral = view.findViewById(R.id.funeral);
    ImageButton mass = view.findViewById(R.id.mass);

    confession.setOnClickListener(this);
    funeral.setOnClickListener(this);
    mass.setOnClickListener(this);

    return view;
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.funeral:
        Intent funeral = new Intent(getActivity(), PaymentActivity.class);
        funeral.putExtra("money", Integer.toString(5000));
        startActivity(funeral);
        break;
      case R.id.confession:
        Intent confession = new Intent(getActivity(), PaymentActivity.class);
        confession.putExtra("money", Integer.toString(100));
        startActivity(confession);
        break;
      case R.id.mass:
        Intent mass = new Intent(getActivity(), PaymentActivity.class);
        mass.putExtra("money", Integer.toString(500));
        startActivity(mass);
        break;
    }
  }
}
