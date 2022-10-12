package com.jopek.taupngamoni;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jopek.taupngamoni.databinding.FragmentScreenHomeBinding;

import java.util.List;
import java.util.function.Consumer;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Currency}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    private final List<Currency> currencies;
    public Consumer<String> cb;

    public HomeRecyclerAdapter(List<Currency> currencies, Consumer<String> cb) {
        this.currencies = currencies;
        this.cb = cb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentScreenHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Currency item = currencies.get(position);
        holder.mItem = item;
        holder.img.setImageBitmap(item.img);
        holder.code.setText(item.code);
        holder.description.setText(item.description);

        holder.itemView.setOnLongClickListener(v -> {
            cb.accept(item.code);
            return false;
        });

        String valueToForeign;
        String oneToForeign;
        try {
            valueToForeign = item.symbol + " " + MainActivity.round(HomeFragment.conversionAmount * item.conversionFactor, 6);
            oneToForeign = "1 " + item.code + " = " + item.conversionFactor + " " + HomeFragment.conversionTo.code;
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