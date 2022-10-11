package com.jopek.taupngamoni;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jopek.taupngamoni.databinding.FragmentScreenHomeBinding;
import com.jopek.taupngamoni.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Currency}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    private final List<Currency> currencies;

    public HomeRecyclerAdapter(List<Currency> items) {
        currencies = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("maks", "onCreateViewHolder: " + parent);
        return new ViewHolder(FragmentScreenHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Currency item = currencies.get(position);
        holder.mItem = item;
        holder.img.setImageBitmap(item.img);
        holder.code.setText(item.code);
        holder.description.setText(item.description);
        String valueToForeign;
        String oneToForeign;
        try {
            valueToForeign = item.conversionTo.symbol + MainActivity.round(1 / item.conversionFactor, 4);
            oneToForeign = "1 " + item.code + " = " + item.conversionFactor + " " + item.conversionTo.code;
        } catch (NullPointerException exception) {
            valueToForeign = "Error";
            oneToForeign = "Error";
        }
        holder.valueToForeign.setText(valueToForeign);
        holder.oneToForeign.setText(oneToForeign);
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView img;
        public final TextView code;
        public final TextView description;
        public final TextView valueToForeign;
        public final TextView oneToForeign;
        public Currency mItem;

        public ViewHolder(FragmentScreenHomeBinding binding) {
            super(binding.getRoot());
            img = binding.img;
            code = binding.code;
            description = binding.description;
            valueToForeign = binding.valueToForeign;
            oneToForeign = binding.oneToForeign;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + code.getText() + "'";
        }
    }
}