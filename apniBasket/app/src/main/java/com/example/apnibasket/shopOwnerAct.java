package com.example.apnibasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class shopOwnerAct extends AppCompatActivity {

    static ArrayList<String> daa;
    Button logout,shop,order;
    TextView name , numb , tcity;
    static String numbid;
    private FirebaseDatabase database;
    private DatabaseReference mref;
    private RequestQueue mque;
    ArrayList<custom> mizedata;
    ListView mainlist;
    Adapt adapt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_owner2);
        mque = Volley.newRequestQueue(this);
        mizedata = new ArrayList<custom>();
        mainlist = findViewById(R.id.mlist);
        name = findViewById(R.id.textView3);
        numb = findViewById(R.id.textView4);
        order = findViewById(R.id.button5);
        logout = findViewById(R.id.button3);
        tcity = findViewById(R.id.textView5);
        shop = findViewById(R.id.button4);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
        Intent intg = getIntent();
        numbid = intg.getExtras().getString("number");
        numb.setText(numbid);
        database = FirebaseDatabase.getInstance();
        mref = database.getReference();
        mref.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dd : dataSnapshot.getChildren()){
                    String key = dd.getKey();
                    if (dataSnapshot.child(key).hasChild(numbid)){
                        Notification n = new Notification.Builder(getApplicationContext())
                                .setContentTitle("newOrder")
                                .setContentText("You Have Recieved a new Order")
                                .setSmallIcon(R.drawable.splash_img).setAutoCancel(true)
                                .setStyle(new Notification.BigTextStyle())
                                .build();

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(20,n);
                        Log.i("data","show");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        jsonParse();
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrd();
            }
        });
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nam = dataSnapshot.child("shopOwner").child(numbid).child("fullName").getValue().toString();
                name.setText(nam);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DailogBox().show(getSupportFragmentManager(),"shop");
            }
        });
        daa = new ArrayList<>();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null){
                    Geocoder geocoder = new Geocoder(shopOwnerAct.this, Locale.getDefault());
                    try {
                        List<Address> address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        String city = address.get(0).getLocality();
                        mref.child("product").child(city).child(numbid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                daa.clear();
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    HashMap<String,Object> data = new HashMap<>();
                                    data = (HashMap<String, Object>) ds.getValue();
                                    daa.add(data.values().toString());
                                    //Log.i("data",data.values().toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        daa.add("no items in your shop");
                    }
                }
            }
        });

    }

    private void jsonParse(){
        String murl = "https://extendsclass.com/api/json-storage/bin/effcbad";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, murl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray alcohol = response.getJSONArray("alchol");
                            for (int i=0;i<alcohol.length();i++){
                                JSONObject redata = alcohol.getJSONObject(i);
                                String name = redata.getString("name");
                                String imgurl = redata.getString("imgurl");
                                String price = redata.getString("price");
                                downloadImage img = new downloadImage();
                                Bitmap imgs;
                                try {
                                    imgs = img.execute(imgurl).get();
                                    mizedata.add(new custom(imgs,name,price));
                                    adapt = new Adapt(getApplicationContext(),0,mizedata);
                                    mainlist.setAdapter(adapt);
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(), "Connect To Internet and restart app", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mque.add(request);
        return;

    }
    public static class downloadImage extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private void logOut(){
        Intent intent = new Intent(shopOwnerAct.this,login.class);
        startActivity(intent);
        finish();
    }
    private void showOrd(){
        Intent intent = new Intent(shopOwnerAct.this,shopViewOrder.class);
        intent.putExtra("numberh",numbid);
        startActivity(intent);
    }
}
