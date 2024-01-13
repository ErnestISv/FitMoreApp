package com.example.universityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private RequestQueue requestQueue;
    private TextView vadbe;
    private String url = "https://fitmore.azurewebsites.net/api/v1/Vadba";
    private String url1 = "https://fitmore.azurewebsites.net/api/v1/Del";

    public static final String EXTRA_MESSAGE = "com.example.universityapp.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        vadbe = (TextView) findViewById(R.id.vadbe);
    }

    public  void prikaziVadbe(View view){
        if (view != null){
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response){
            Log.d("API Response", "Received response: " + response.toString());

            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++){
                try {
                    JSONObject object = response.getJSONObject(i);
                    String del = object.getString("del");
                    String cas = object.getString("dan");
                    String kalorije = object.getString("porabaKalorij");


                    String[] datumInCasArray = cas.split("T");
                    String datum = datumInCasArray[0];
                    String[] datumArray = datum.split("-");

                    String leto = datumArray[0];
                    String mesec = datumArray[1];
                    String dan = datumArray[2];

                    String[] casArray = datumInCasArray[1].split("[:.]");

                    String ura = casArray[0];
                    String minuta = casArray[1];

                    String skupniCas = dan + "." + mesec + "." + leto + " " + ura + ":" + minuta;

                    data.add("\n" + "Porabljene kalorije na dan: " + skupniCas + ":\n " + kalorije);

                } catch (JSONException e){
                    e.printStackTrace();
                    return;

                }
            }

            vadbe.setText("");


            for (String row: data){
                String currentText = vadbe.getText().toString();
                vadbe.setText(currentText + "\n\n" + row);
            }

        }

    };


    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("API Error", "Error in API request: " + error.getMessage());
            Log.d("REST error", error.getMessage());
        }
    };
    public void addVadbaActivity (View view) {
        Intent intent = new Intent(this,AddVadbaActivity.class);
        String message = "Dodaj vadbo v seznam.";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}