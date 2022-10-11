package com.jopek.taupngamoni;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
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

        currencies.add(new Currency("USD", "United States Dollar", null));
        currencies.add(new Currency("GBP", "British Pound Sterling", null));
        currencies.add(new Currency("CHF", "Swiss Franc", null));
        currencies.add(new Currency("PLN", "Polish Zloty", null));
        currencies.add(new Currency("EUR", "Euro", null));
        currencies.add(new Currency("JPY", "Japanese Yen", null));
        currencies.add(new Currency("CNY", "Chinese Yuan", null));
        currencies.add(new Currency("UAH", "Ukrainian Hryvnia", null));
        currencies.add(new Currency("CZK", "Czech Republic Koruna", null));
        currencies.add(new Currency("TWD", "New Taiwan Dollar", null));

        ArrayList<Currency> currenciesCopy = (ArrayList<Currency>) currencies.clone();
        for (int i = 0; i < currencies.size(); i++) {
            Currency cur = currencies.get(i);
            RequestImage requestImage = new RequestImage();
            int finalI = i;
            requestImage.getBitmap("https://wise.com/public-resources/assets/flags/rectangle/" + cur.code.toLowerCase() + ".png", bitmap -> {
                cur.img = bitmap;
                if (finalI + 1 == currencies.size()) {
                    spinner.setAdapter(spinnerAdapter);
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

        spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
//        spinner.setAdapter(spinnerAdapter);

        //Creating the ArrayAdapter instance having the country list
        // ArrayAdapter aa = new ArrayAdapter(requireContext(), R.layout.spinner_item, currenciesCodes.toArray(new String[0]));
        // aa.setDropDownViewResource(R.layout.spinner_item);
        // Setting the ArrayAdapter data on the Spinner
        // spin.setAdapter(new MySpinnerAdapter(requireContext(), currenciesCodes));
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new HomeRecyclerAdapter(PlaceholderContent.ITEMS));
        }
        return view;
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
        Toast.makeText(requireContext(), currencies.get(position).code, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}

class Currency {
    public String code;
    public String description;
    public Bitmap img;

    public Currency(String code, String description, Bitmap img) {
        this.code = code;
        this.description = description;
        this.img = img;
    }
}