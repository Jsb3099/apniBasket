package com.example.apnibasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class shopViewOrder extends AppCompatActivity {

    ListView orderlist;
    DatabaseReference reference;
    String numberhh;
    ArrayList<ordCustom> dataord;
    Adapterord adapterord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_view_order);
        numberhh = getIntent().getExtras().getString("numberh");
        dataord = new ArrayList<>();
        Log.i("number",numberhh);
        orderlist = findViewById(R.id.orderList);
        reference = FirebaseDatabase.getInstance().getReference("order");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    String key = d.getKey();
                    reference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot t : dataSnapshot.getChildren()){
                                String numkey = t.getKey();
                                if (numkey.equals(numberhh)) {
                                    String item = dataSnapshot.child(numkey).child("items").getValue().toString();
                                    String price = dataSnapshot.child(numkey).child("totalAmount").getValue().toString();
                                    String number = dataSnapshot.child(numkey).child("orderingPerson").getValue().toString();
                                    dataord.add(new ordCustom(item, price, number));
                                    adapterord = new Adapterord(getApplicationContext(), 0, dataord);
                                    orderlist.setAdapter(adapterord);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
