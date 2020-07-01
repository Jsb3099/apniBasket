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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class userHomeAct extends AppCompatActivity {

    Button logOut;
    static String number;
    TextView nameText,numberText,addressText;
    ListView shoplist;
    ArrayList<String> shopname;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        ImageSlider imageSlider=findViewById(R.id.slider);
        List<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1527281400683-1aae777175f8?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1561150169-371f366b828a?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1516535794938-6063878f08cc?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1566666152520-27af5f022a0f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"));
        imageSlider.setImageList(slideModels,true);

        number = getIntent().getExtras().getString("number");
        shopname = new ArrayList<>();
        shoplist = findViewById(R.id.listShop);

        nameText = findViewById(R.id.textView);
        numberText = findViewById(R.id.textView6);
        addressText = findViewById(R.id.textView9);
        logOut = findViewById(R.id.button7);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logouth();
            }
        });
        numberText.setText(number);
        mref = FirebaseDatabase.getInstance().getReference();
        mref.child("user").child(number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("fullName").getValue().toString();
                nameText.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null){
                    Geocoder geocoder = new Geocoder(userHomeAct.this, Locale.getDefault());
                    try {
                        final List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        final String ad = address.get(0).getAddressLine(0);
                        addressText.setText(ad);
                        final String city = address.get(0).getLocality();
                        mref.child("product").child(city).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dd : dataSnapshot.getChildren()){
                                    String nameshop = dd.getKey();
                                    shopname.add(nameshop);
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,shopname);
                                shoplist.setAdapter(adapter);
                                shoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String text = ((TextView)view).getText().toString();
                                        Intent next = new Intent(userHomeAct.this,shopItemShowUser.class);
                                        next.putExtra("shopname",text);
                                        next.putExtra("cityname",city);
                                        startActivity(next);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }
        });

    }
    private void logouth(){
        Intent logoutbh = new Intent(userHomeAct.this,login.class);
        startActivity(logoutbh);
        finish();
    }
}
