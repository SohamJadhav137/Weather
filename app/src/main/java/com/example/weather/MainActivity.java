package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.util.NetworkKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText et;
    TextView tvcity,tvtemp,tvmintemp,tvmaxtemp,tvsr,tvss,tvwind,tvprs,tvhum,tvvis;
    ProgressBar pb;

//    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.editText);
        tvcity = findViewById(R.id.textViewCity);
        tvtemp = findViewById(R.id.textViewTemp);
        tvmintemp = findViewById(R.id.textViewMinTemp);
        tvmaxtemp = findViewById(R.id.textViewMaxTemp);
        tvsr = findViewById(R.id.textViewSunrise);
        tvss = findViewById(R.id.textViewSunset);
        tvwind = findViewById(R.id.textViewWind);
        tvprs = findViewById(R.id.textViewPressure);
        tvhum = findViewById(R.id.textViewPressure);
        tvvis = findViewById(R.id.textViewVisibility);
        pb = findViewById(R.id.progressBar);

//        btn = findViewById(R.id.button);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String city = et.getText().toString().trim();
//                    if(city.equals(""))
//                    {
//                        Toast.makeText(MainActivity.this, "Enter city name", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        getAllDetails(city);
//                    }
//            }
//        });

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String city = et.getText().toString().trim();
                    if(city.equals(""))
                    {
                        Toast.makeText(MainActivity.this, "Enter city name", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    else
                    {
                        getAllDetails(city);
                        return true;
                    }
                }
                return false;
            }
        });


    }

    private void getAllDetails(String city) {
        pb.setVisibility(View.VISIBLE);

        String URL = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid=d80902d57b939f6cb43c1d044d4413b3";

        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pb.setVisibility(View.GONE);
                try {
                    JSONObject j = new JSONObject(response);
                    String c = j.getString("name");
                    String temp = String.format("%.2f",j.getJSONObject("main").getDouble("temp"));
                    String tempmin = String.format("%.2f",j.getJSONObject("main").getDouble("temp_min"));
                    String tempmax = String.format("%.2f",j.getJSONObject("main").getDouble("temp_max"));


                    long sr = j.getJSONObject("sys").getLong("sunrise");
                    long javaTimestamp1 = sr * 1000L;
                    Date date1 = new Date(javaTimestamp1);
                    String sunrise = new SimpleDateFormat("hh:mm").format(date1);

                    long ss = j.getJSONObject("sys").getLong("sunset");
                    long javaTimestamp2 = ss * 1000L;
                    Date date2 = new Date(javaTimestamp2);
                    String sunset = new SimpleDateFormat("hh:mm").format(date2);

                    String wind = String.format("%.2f",j.getJSONObject("wind").getDouble("speed"));
                    String prs = String.format("%d",j.getJSONObject("main").getInt("pressure"));
                    String hum = String.format("%d",j.getJSONObject("main").getInt("humidity"));
                    String vis = String.format("%d",j.getInt("visibility"));

                    tvcity.setText(c);
                    tvtemp.setText(temp+" °C");
                    tvmintemp.setText("Min Temp: "+tempmin+" °C");
                    tvmaxtemp.setText("Max Temp: "+tempmax+" °C");
                    tvsr.setText("Sunrise\n"+sunrise+" AM");
                    tvss.setText("Sunset\n"+sunset+" PM");
                    tvwind.setText("Wind\n"+wind);
                    tvprs.setText("Pressure\n"+prs);
                    tvhum.setText("Humidity\n"+hum);
                    tvvis.setText("Visible\n"+vis);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb.setVisibility(View.GONE);
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


}