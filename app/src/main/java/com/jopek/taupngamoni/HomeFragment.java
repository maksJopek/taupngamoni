package com.jopek.taupngamoni;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jopek.taupngamoni.placeholder.PlaceholderContent;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<Currency> currencies = new ArrayList<>();
    private ArrayList<Currency> selectedCurrencies = new ArrayList<>();
    private HomeRecyclerAdapter recyclerAdapter = new HomeRecyclerAdapter(selectedCurrencies);
    private RecyclerView recyclerView;
    private Currency selectedCurrency = Currency.USD;
    private MySpinnerAdapter spinnerAdapter;
    private Spinner spinner;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    // TODO: Customize parameter initialization
    public static HomeFragment newInstance(int columnCount) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currencies.add(Currency.USD);
        currencies.add(Currency.GBP);
        currencies.add(Currency.CHF);
        currencies.add(Currency.PLN);
        currencies.add(Currency.EUR);
        currencies.add(Currency.JPY);
        currencies.add(Currency.CNY);
        currencies.add(Currency.UAH);
        currencies.add(Currency.CZK);
        currencies.add(Currency.TWD);

        selectedCurrencies.add(Currency.USD);
        selectedCurrencies.add(Currency.GBP);
        selectedCurrencies.add(Currency.PLN);

        for (int i = 0; i < currencies.size(); i++) {
            Currency cur = currencies.get(i);
            RequestImage requestImage = new RequestImage();
            int finalI = i;
            requestImage.getBitmap("https://wise.com/public-resources/assets/flags/rectangle/" + cur.code.toLowerCase() + ".png", bitmap -> {
                cur.img = bitmap;
                cur.conversionTo = Currency.USD;
                if (finalI + 1 == currencies.size()) {
                    spinner.setAdapter(spinnerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                }
            });
        }
        spinnerAdapter = new MySpinnerAdapter(requireContext(), currencies);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_home_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        Log.d("maks", "onCreateView: " + view + container + recyclerView);

        spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        if (recyclerView instanceof RecyclerView) {
            Context context = view.getContext();
//            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(recyclerAdapter);
        }
        return view;
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
        Toast.makeText(requireContext(), currencies.get(position).code, Toast.LENGTH_LONG).show();
        for (Currency cur: selectedCurrencies) {
            cur.conversionTo = currencies.get(position);
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
