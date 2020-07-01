package com.example.apnibasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

public class shopItemShowUser extends AppCompatActivity {

    static String shopname , cityname;
    ArrayList<String> shopItems;
    private RequestQueue mdque;
    ArrayList<custom> midata;
    DatabaseReference ref ;
    itemAdapt ad;
    ListView listofitem;
    Button cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_show_user);
        final ProgressDialog pd = new ProgressDialog(shopItemShowUser.this);
        pd.setMessage("loading shop menu");
        pd.show();
        cart = findViewById(R.id.button8);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carth();
            }
        });
        mdque = Volley.newRequestQueue(this);
        listofitem = findViewById(R.id.shopItems);
        shopname = getIntent().getExtras().getString("shopname");
        cityname = getIntent().getExtras().getString("cityname");
        shopItems = new ArrayList<>();
        midata = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("product").child(cityname).child(shopname).child("drinkname").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String key = ds.getKey();
                    shopItems.add(dataSnapshot.child(key).getValue().toString());
                }
                jParse();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void jParse(){
        String murl = "https://extendsclass.com/api/json-storage/bin/effcbad";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, murl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray alcohol = response.getJSONArray("alchol");
                            for (int i = 0; i < alcohol.length(); i++) {
                                JSONObject redata = alcohol.getJSONObject(i);
                                String name = redata.getString("name");
                                if (shopItems.contains(name)){
                                    String imgurl = redata.getString("imgurl");
                                    String price = redata.getString("price");
                                    downloadImageuser img = new downloadImageuser();
                                    Bitmap imgs;
                                    try {
                                        imgs = img.execute(imgurl).get();
                                        midata.add(new custom(imgs, name, price));
                                        //Log.i("data",midata.toString());
                                        ad = new itemAdapt(shopItemShowUser.this, 0, midata);
                                        listofitem.setAdapter(ad);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Connect To Internet and restart app", Toast.LENGTH_SHORT).show();
                                }
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

        mdque.add(request);
        return;

    }
    public static class downloadImageuser extends AsyncTask<String,Void, Bitmap> {
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
    private void carth(){
        Intent cartb = new Intent(shopItemShowUser.this,checkoutUser.class);
        startActivity(cartb);
    }
}
