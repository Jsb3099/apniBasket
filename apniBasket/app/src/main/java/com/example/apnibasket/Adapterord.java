package com.example.apnibasket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Adapterord extends ArrayAdapter<ordCustom> {
    public Adapterord(@NonNull Context context, int resource, @NonNull ArrayList<ordCustom> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemview = convertView;
        if (itemview == null) {
            itemview = LayoutInflater.from(getContext()).inflate(R.layout.orderlistcustom, parent, false);
        }
        ordCustom currentItem = getItem(position);
        TextView items = itemview.findViewById(R.id.itemsord);
        items.setText(currentItem.getItems());
        TextView price = itemview.findViewById(R.id.priceord);
        price.setText(currentItem.getPrice());
        TextView number = itemview.findViewById(R.id.numord);
        number.setText(currentItem.getNumberid());
        return itemview;
    }
}
