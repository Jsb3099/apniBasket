package com.example.apnibasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class checkoutUser extends AppCompatActivity {

    //ArrayList<String> ordereditems;
    String ordereditems;
    TextView total, userAddress;
    ArrayList<checkOutCustom> data;
    ListView checkoutlist;
    Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_user);
        total = findViewById(R.id.textView13);
        ordereditems = "";
        userAddress = findViewById(R.id.textView12);
        checkoutlist = findViewById(R.id.checkoutlist);
        placeOrder = findViewById(R.id.button6);
        data = new ArrayList<>();
        int totalamount = 0;
        for (Map.Entry<String, String> entry : itemAdapt.checkoutvalue.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            ordereditems+=k+" : ";
            int val = Integer.parseInt(v);
            totalamount += val;
            data.add(new checkOutCustom(k, v));
            checkOutAdapt adaptc = new checkOutAdapt(getApplicationContext(), 0, data);
            checkoutlist.setAdapter(adaptc);
        }
        total.setText("Total amount to be paid : " + totalamount);
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(checkoutUser.this, Locale.getDefault());
                    try {
                        List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        userAddress.setText(address.get(0).getAddressLine(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemAdapt.checkoutvalue.isEmpty()){
                    placeOrder.setError("add some items to cart");
                    return;
                }
                placeord();
                itemAdapt.checkoutvalue.clear();
            }
        });
    }
    private void placeord(){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("order").push();
        ref.child(shopItemShowUser.shopname).child("items").setValue(ordereditems);
        ref.child(shopItemShowUser.shopname).child("totalAmount").setValue(total.getText().toString());
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(checkoutUser.this, Locale.getDefault());
                    try {
                        List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        ref.child(shopItemShowUser.shopname).child("address").setValue(address.get(0).getAddressLine(0));
                        ref.child(shopItemShowUser.shopname).child("latitude").setValue(address.get(0).getLatitude());
                        ref.child(shopItemShowUser.shopname).child("longitude").setValue(address.get(0).getLongitude());
                        ref.child(shopItemShowUser.shopname).child("orderingPerson").setValue(userHomeAct.number);
                        Toast.makeText(checkoutUser.this, "Your Order is placed successfully", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
