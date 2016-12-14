package com.example.pafi.weatherapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pafi.weatherapp.RestAPI.Forecast;
import com.example.pafi.weatherapp.RestAPI.Result;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForecastDetailActivity extends AppCompatActivity {


    private static final String REQUEST = "https://en.wikipedia.org/w/api.php?action=query&titles=REPLACEME&prop=images&format=json";
    private static final String FROM_TITLE="https://en.wikipedia.org/w/api.php?action=query&format=json&prop=imageinfo&titles=REPLACEME&iiprop=url";
    private static final String DEFAUKT_IMG = "https://static.vecteezy.com/system/resources/previews/000/118/897/original/free-flat-city-landscape-vector-illustration.jpg";
    private static final String pattern = "(\\\"title\\\"\\:\\\".*?images.*?)(\\\"title\\\"\\:\\\")(.*?)(\\\"\\})";
    private static final String urlPattern="(\\{\\\"url\\\"\\:\\\")(.*?)(\",)";
    private ImageView barLayout;
    private String received, cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        barLayout = (ImageView) findViewById(R.id.detail_image);
        TextView country = (TextView) findViewById(R.id.detail_country);
        TextView lon = (TextView) findViewById(R.id.detail_long);
        TextView lat = (TextView) findViewById(R.id.detail_lat);


        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled},
        };

        int[] colors = new int[] {
                Color.WHITE,
        };

        int pos = getIntent().getIntExtra("position", 0);
        pos++;
        Result data = getIntent().getParcelableExtra("result");
        cityName = data.getCity().getName();
        System.out.println("city name: "+cityName);
        String time = data.getList().get((pos - 1) * 8).getDt_txt();
        time = time.substring(0, 10);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = formatter.parse(time);
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.");
            collapsingToolbarLayout.setTitle(dateFormat.format(d)+", "+cityName);
            ColorStateList myList = new ColorStateList(states, colors);
            collapsingToolbarLayout.setExpandedTitleTextColor(myList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.detail_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Forecast> day = new ArrayList<>();
        for(Forecast f : data.getList()) {
            String temp = f.getDt_txt();
            if(temp.contains(time)) day.add(f);
        }
        recyclerView.setAdapter(new DetailAdapter(day));
        country.setText(data.getCity().getCountry());
        DecimalFormat df = new DecimalFormat("#.##");
        float l = data.getCity().getCoord().getLon();
        String temp = String.valueOf(df.format(l)) + ", ";
        System.out.println(temp);
        lon.setText(temp);
        lat.setText(String.valueOf(data.getCity().getCoord().getLat()));

        try {
            received = new ForecastDetailActivity.Task().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(received);

        Glide
                .with(ForecastDetailActivity.this)
                .load(received)
                .centerCrop()
                .crossFade()
                .into(barLayout);
    }

    private class Task extends AsyncTask<String, String, String> {
        private String string = "";
        String title = "";
        String real_url;

        protected String doInBackground(String... urls) {
            try {
                InputStream input = new URL(REQUEST.replace("REPLACEME",cityName)).openStream();

                Scanner scanner = new Scanner(input);
                while (scanner.hasNextLine()) {
                    string += scanner.nextLine() + " ";
                }

                Pattern patter = Pattern.compile(pattern);
                Matcher m = patter.matcher(string);
                m.matches();
                if (m.find( )) title=m.group(3);

            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(ForecastDetailActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();

            }

            title= title.replace(" ","_");

            real_url= FROM_TITLE.replace("REPLACEME",title);

            try {
                InputStream input = new URL(real_url).openStream();
                string="";
                Scanner scanner = new Scanner(input);
                while (scanner.hasNextLine()) {
                    string += scanner.nextLine() + " ";

                }

                Pattern patter = Pattern.compile(urlPattern);
                Matcher m = patter.matcher(string);
                m.matches();
                if (m.find( )) real_url=m.group(2);
else{
                    real_url=null;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            if(real_url!=null){
                return real_url;
            }else
           return DEFAUKT_IMG;
        }
    }
}
