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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    TextView signup;
    Button login;
    EditText lnumber,lpassword;
    CheckBox deliveryPartner , user , shopOwner;
    String tablename;
    private FirebaseDatabase database;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup = findViewById(R.id.signup);
        lnumber = findViewById(R.id.editText2);
        lpassword = findViewById(R.id.editText3);
        deliveryPartner = findViewById(R.id.checkBox);
        user = findViewById(R.id.checkBox3);
        shopOwner = findViewById(R.id.checkBox2);
        login = findViewById(R.id.button);
        database = FirebaseDatabase.getInstance();
        mref = database.getReference();
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



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (lnumber.getText().toString().length()==0){
                 lnumber.setError("number is required");
                 return;
             }else if (lpassword.getText().toString().length()==0){
                 lpassword.setError("password is required");
                 return;
             }else if (deliveryPartner.isChecked() || user.isChecked() || shopOwner.isChecked() ){
                 mlogin();
             }else {
                 Toast.makeText(login.this, "select who you are", Toast.LENGTH_SHORT).show();
             }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sign();
            }
        });
    }
    private void sign(){
        Intent intent = new Intent(login.this,signUp.class);
        startActivity(intent);
    }
    private void mlogin(){
        if (deliveryPartner.isChecked()){tablename = "deliveryPartner";}
        else if (user.isChecked()){tablename = "user";}
        else if (shopOwner.isChecked()){tablename = "shopOwner";}
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(tablename).hasChild(lnumber.getText().toString())){
                    if (dataSnapshot.child(tablename).child(lnumber.getText().toString()).child("password").getValue().equals(lpassword.getText().toString())){
                        Toast.makeText(login.this, "login Sucessfully", Toast.LENGTH_SHORT).show();
                        if (shopOwner.isChecked()) {
                            Intent sown = new Intent(login.this, shopOwnerAct.class);
                            sown.putExtra("number",lnumber.getText().toString());
                            startActivity(sown);
                            finish();
                        }else if (user.isChecked()) {
                            Intent use = new Intent(login.this,userHomeAct.class);
                            use.putExtra("number",lnumber.getText().toString());
                            startActivity(use);
                            finish();
                        }
                    }else {
                        lpassword.setError("Wrong Password");
                        Toast.makeText(login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    lnumber.setError("enter Valid number");
                    Toast.makeText(login.this, "enter valid number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(login.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
