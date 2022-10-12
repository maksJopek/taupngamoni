package com.jopek.taupngamoni;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static Currency conversionTo;
    public static double conversionAmount = 1.0;

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<Currency> currencies = new ArrayList<>();
    private ArrayList<Currency> selectedCurrencies = new ArrayList<>();
    private HomeRecyclerAdapter recyclerAdapter = new HomeRecyclerAdapter(selectedCurrencies, code -> selectedCurrencies.removeIf(cur -> cur.code.equals(code)));
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
                HomeFragment.conversionTo = Currency.USD;
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

        EditText amountInput = view.findViewById(R.id.amount_input);
        amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String val = s.toString();
                if (!val.equals("")) {
                    HomeFragment.conversionAmount = Double.parseDouble(val);
                } else {
                    HomeFragment.conversionAmount = 0.0;
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        });
        TextView btnAdd = view.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(v -> {
            if(selectedCurrencies.stream().noneMatch(cur -> cur.code.equals(HomeFragment.conversionTo.code))) {
                selectedCurrencies.add(HomeFragment.conversionTo);
                updateList();
            }
        });

        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
        Currency selCur = currencies.get(position);
        HomeFragment.conversionTo = selCur;
        updateList();
    }
     public void updateList() {
         String url = "https://api.exchangerate.host/latest?base=" + HomeFragment.conversionTo.code + "&symbols=";
         for (Currency cur : selectedCurrencies) {
             url += cur.code + ",";
         }
         Requests request = new Requests();
         request.getJson(url, json -> {
             try {
                 JSONObject rates = json.getJSONObject("rates");
                 for (Currency cur : selectedCurrencies) {
                     cur.conversionFactor = rates.getDouble(cur.code);
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
             recyclerAdapter.notifyDataSetChanged();
         });
     }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
