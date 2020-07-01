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

public class checkOutAdapt extends ArrayAdapter<checkOutCustom> {
    public checkOutAdapt(@NonNull Context context, int resource, ArrayList<checkOutCustom> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemview = convertView;
        if (itemview == null){
            itemview = LayoutInflater.from(getContext()).inflate(R.layout.customcart,parent,false);
        }
        checkOutCustom current = getItem(position);
        TextView nametext = itemview.findViewById(R.id.drinkname);
        nametext.setText(current.getName());
        TextView priceText = itemview.findViewById(R.id.drinkprice);
        priceText.setText(current.getPrice());
        return itemview;
    }
}
