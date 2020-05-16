package com.pamo.iparish.donation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pamo.iparish.R;
import com.pamo.iparish.settings.PaymentActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickOfferingFragment extends Fragment implements View.OnClickListener {

    TextView money;
    Button increment;
    Button decrement;
    Button pay;
    int amount;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        amount = 10;

        View view = inflater.inflate(R.layout.fragment_quick_offering, container, false);

        money = view.findViewById(R.id.money);
        increment = view.findViewById(R.id.increment);
        decrement = view.findViewById(R.id.decrement);
        pay = view.findViewById(R.id.pay);
        money.setText(Integer.toString(amount));

        increment.setOnClickListener(this);
        decrement.setOnClickListener(this);
        pay.setOnClickListener(this);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.increment:
        amount+=5;
        money.setText(Integer.toString(amount));
        break;
      case R.id.decrement:
        amount-=1;
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
