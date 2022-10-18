package com.jopek.taupngamoni;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MySpinnerAdapter extends BaseAdapter {
    Context context;
    List<Currency> currencies;
    LayoutInflater inflater;

    public MySpinnerAdapter(Context applicationContext, List<Currency> currencies) {
        this.context = applicationContext;
        this.currencies = currencies;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_item, null);
        ImageView icon = view.findViewById(R.id.img);
        TextView name = view.findViewById(R.id.txt);
        icon.setImageBitmap(currencies.get(i).img);
        name.setText(currencies.get(i).code);
        return view;
    }
}
