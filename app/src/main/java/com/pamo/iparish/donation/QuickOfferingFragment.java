package com.pamo.iparish.donation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pamo.iparish.R;
import com.pamo.iparish.settings.PaymentActivity;

public class QuickOfferingFragment extends Fragment implements View.OnClickListener {

  private TextView money;
  private int amount;
  private RatingBar rating;

  @Override
  public View onCreateView(
    LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    int amount = 10;

    View view = inflater.inflate(R.layout.fragment_quick_offering, container, false);

    Button increment = view.findViewById(R.id.increment);
    Button decrement = view.findViewById(R.id.decrement);
    Button pay = view.findViewById(R.id.pay);
    rating = view.findViewById(R.id.ratingBar);
    money = view.findViewById(R.id.money);

    increment.setOnClickListener(this);
    decrement.setOnClickListener(this);
    pay.setOnClickListener(this);

    money.setText(Integer.toString(amount));

    return view;
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.increment:
        amount += 5;
        money.setText(Integer.toString(amount));
        rating.setRating(amount / 20);
        break;
      case R.id.decrement:
        amount -= 1;
        rating.setRating(amount / 20);
        money.setText(Integer.toString(amount));
        break;
      case R.id.pay:
        Intent i = new Intent(getActivity(), PaymentActivity.class);
        i.putExtra("money", Integer.toString(amount));
        startActivity(i);
        break;
    }
  }
}
