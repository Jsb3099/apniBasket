package com.example.apnibasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signUp extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference mref;
    EditText fname , snumber , spassword;
    CheckBox deliveryPartner , user , shopOwner;
    Button signup;
    abData sdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fname = findViewById(R.id.editText4);
        snumber = findViewById(R.id.editText);
        spassword = findViewById(R.id.editText5);
        deliveryPartner = findViewById(R.id.checkBox6);
        shopOwner = findViewById(R.id.checkBox5);
        user = findViewById(R.id.checkBox4);
        signup = findViewById(R.id.button2);
        database = FirebaseDatabase.getInstance();
        mref = database.getReference();
        sdata = new abData();


        deliveryPartner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    user.setEnabled(false);
                    shopOwner.setEnabled(false);
                }else {
                    user.setEnabled(true);
                    shopOwner.setEnabled(true);

                }
            }
        });
        user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    deliveryPartner.setEnabled(false);
                    shopOwner.setEnabled(false);
                }else {
                    deliveryPartner.setEnabled(true);
                    shopOwner.setEnabled(true);
                }
            }
        });
        shopOwner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    deliveryPartner.setEnabled(false);
                    user.setEnabled(false);
                }else {
                    deliveryPartner.setEnabled(true);
                    user.setEnabled(true);
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fname.getText().toString().length()==0){
                    fname.setError("full name is required");
                    return;
                }else if (snumber.getText().toString().length() == 0){
                    snumber.setError("number is required");
                    return;
                }else if(snumber.getText().toString().length() != 10){
                    snumber.setError("enter a valid number");
                    return;
                }else if(spassword.getText().toString().length() == 0){
                    spassword.setError("password is required");
                    return;
                }else if (deliveryPartner.isChecked() || user.isChecked() || shopOwner.isChecked()){
                   fsignup();
                }else {
                    Toast.makeText(signUp.this, "tell who you are", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void fsignup(){

        sdata.setFullName(fname.getText().toString());
        sdata.setNumber(snumber.getText().toString());
        sdata.setPassword(spassword.getText().toString());
        if (deliveryPartner.isChecked()){
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if (dataSnapshot.child("deliveryPartner").hasChild(sdata.getNumber())){
                      snumber.setError("number already registered");
                      return;
                  }else {
                      mref.child("deliveryPartner").child(sdata.getNumber()).setValue(sdata);
                      Toast.makeText(getApplicationContext(), "sucessfully registered", Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(signUp.this,login.class);
                      startActivity(intent);
                      finish();
                  }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(signUp.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }else if(user.isChecked()){

            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("user").hasChild(sdata.getNumber())){
                        snumber.setError("number already registered");
                        return;
                    }else {
                        mref.child("user").child(sdata.getNumber()).setValue(sdata);
                        Toast.makeText(getApplicationContext(), "sucessfully registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(signUp.this,login.class);
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(signUp.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }else if(shopOwner.isChecked()){

            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("shopOwner").hasChild(sdata.getNumber())){
                        snumber.setError("number already registered");
                        return;
                    }else {
                        mref.child("shopOwner").child(sdata.getNumber()).setValue(sdata);
                        Toast.makeText(getApplicationContext(), "sucessfully registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(signUp.this,login.class);
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(signUp.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
