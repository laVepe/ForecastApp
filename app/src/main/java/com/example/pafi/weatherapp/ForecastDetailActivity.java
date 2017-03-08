package com.example.pafi.weatherapp;

import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.pafi.weatherapp.RestAPI.Forecast;
import com.example.pafi.weatherapp.RestAPI.Result;
import com.example.pafi.weatherapp.databinding.ActivityDetailBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForecastDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String cityName;
    private ImageView barLayout;
    private Result data;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        data = getIntent().getParcelableExtra("result");
        binding.included.setDetail(data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        barLayout = (ImageView) findViewById(R.id.detail_image);

        int pos = getIntent().getIntExtra("position", 0);
        pos++;
        cityName = data.getCity().getName();
        time = data.getList().get((pos - 1) * 8).getDt_txt();
        time = time.substring(0, 10);
        prepareCollapsingToolbarLayout();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.detail_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new DetailAdapter(getDayForecasts()));

        new Task().execute();
    }

    private void prepareCollapsingToolbarLayout() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date d = formatter.parse(time);
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.", Locale.getDefault());
            collapsingToolbarLayout.setTitle(dateFormat.format(d)+", "+cityName);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Forecast> getDayForecasts() {
        ArrayList<Forecast> day = new ArrayList<>();
        for(Forecast f : data.getList()) {
            String temp = f.getDt_txt();
            if(temp.contains(time)) day.add(f);
        }
        return day;
    }

    private class Task extends AsyncTask<String, String, String> {
        private String string = "";
        String title = "";
        String real_url;

        protected String doInBackground(String... urls) {
            cityName = cityName.replace(" ","%");
            System.out.println("image name: " +cityName);
            try {
                InputStream input = new URL(Constants.REQUEST.replace("REPLACEME",cityName)).openStream();

                Scanner scanner = new Scanner(input);
                while (scanner.hasNextLine()) {
                    string += scanner.nextLine() + " ";
                }

                Pattern patter = Pattern.compile(Constants.PATTERN);
                Matcher m = patter.matcher(string);
                if (m.find( )) title=m.group(3);

            } catch (IOException e) {
                e.printStackTrace();
            }

            title = title.replace(" ","_");
            real_url = Constants.FROM_TITLE.replace("REPLACEME",title);

            try {
                InputStream input = new URL(real_url).openStream();
                string = "";
                Scanner scanner = new Scanner(input);
                while (scanner.hasNextLine()) {
                    string += scanner.nextLine() + " ";
                }

                Pattern patter = Pattern.compile(Constants.URL_PATTERN);
                Matcher m = patter.matcher(string);
                if (m.find()) real_url = m.group(2);
                else real_url = null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(real_url != null && !real_url.contains(".svg")) return real_url;
            else return Constants.DEFAUKT_IMG;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("image url: " + s);
            Glide.with(ForecastDetailActivity.this)
                    .load(s)
                    .centerCrop()
                    .crossFade()
                    .into(barLayout);
        }
    }
}
