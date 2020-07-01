package com.example.apnibasket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class itemAdapt extends ArrayAdapter<custom> {

    Button addcart;
    static HashMap<String,String> checkoutvalue = new HashMap<>();

    public itemAdapt(@NonNull Context context, int resource, @NonNull ArrayList<custom> customs) {
        super(context, resource, customs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemview = convertView;
        if (itemview == null) {
            itemview = LayoutInflater.from(getContext()).inflate(R.layout.customshopitem, parent, false);
        }
        custom currentItem = getItem(position);
        ImageView imageView = itemview.findViewById(R.id.img);
        imageView.setImageBitmap(currentItem.getImgid());
        TextView textView1 = itemview.findViewById(R.id.name);
        textView1.setText(currentItem.getMovien());
        TextView textView2 = itemview.findViewById(R.id.detail);
        textView2.setText(currentItem.getMovier());
        addcart = itemview.findViewById(R.id.addbutton);
        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout parentlist = (LinearLayout) view.getParent();
                TextView textView = (TextView) ((View) parentlist).findViewById(R.id.name);
                TextView textView2 = (TextView) ((View) parentlist).findViewById(R.id.detail);
                String drink = textView.getText().toString();
                String price = textView2.getText().toString();
                checkoutvalue.put(drink,price);
                Toast.makeText(getContext(), "item added to cart", Toast.LENGTH_SHORT).show();
            }
        });
        return itemview;
    }
}
