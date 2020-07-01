package com.example.apnibasket;

import android.content.Context;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Adapt extends ArrayAdapter<custom> {
    String drink;
    static String city;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference("product");


    public Adapt(@NonNull Context context, int resource, @NonNull ArrayList<custom> custom) {
        super(context, resource, custom);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemview = convertView;
        if (itemview == null){
            itemview = LayoutInflater.from(getContext()).inflate(R.layout.customlist,parent,false);
        }
        custom currentItem = getItem(position);
        ImageView imageView = itemview.findViewById(R.id.img);
        imageView.setImageBitmap(currentItem.getImgid());
        TextView textView1 = itemview.findViewById(R.id.name);
        textView1.setText(currentItem.getMovien());
        TextView textView2 = itemview.findViewById(R.id.detail);
        textView2.setText(currentItem.getMovier());
        Button add = itemview.findViewById(R.id.addbutton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                       Location location = task.getResult();
                       if (location != null){
                           Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                           try {
                               List<Address> address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                               city = address.get(0).getLocality();
                               LinearLayout parent = (LinearLayout) view.getParent();
                               TextView textView = (TextView) ((View) parent).findViewById(R.id.name);
                               drink = textView.getText().toString();
                               mref.child(address.get(0).getLocality()).child(shopOwnerAct.numbid).child("drinkname").push().setValue(drink);
                               Toast.makeText(getContext(), "product added to your shop", Toast.LENGTH_SHORT).show();
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }
                    }
                });
            }
        });

        return itemview;
    }
}

