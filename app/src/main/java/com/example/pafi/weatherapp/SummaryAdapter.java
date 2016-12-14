package com.example.pafi.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pafi.weatherapp.RestAPI.Forecast;
import com.example.pafi.weatherapp.RestAPI.Result;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {

    private Result data;
    private int pos = 0;

    public SummaryAdapter(Result data) {
        this.data = data;
    }

    @Override
    public SummaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SummaryAdapter.ViewHolder holder, final int position) {
        Forecast forecast = data.getList().get(pos);
        long timeStamp = forecast.getDt();
        DateFormat dateFormat = new SimpleDateFormat("EEE dd.MM.yyyy");
        Date time = new java.util.Date(timeStamp * 1000);
        holder.date.setText(dateFormat.format(time));
        holder.max.setText(convertToCelsius(forecast.getMain().getTemp_max()));
        holder.min.setText(convertToCelsius(forecast.getMain().getTemp_min()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(position);
                Intent i = new Intent(holder.context, ForecastDetailActivity.class);
                i.putExtra("position", position);
                i.putExtra("result", data);
                holder.context.startActivity(i);
            }
        });
        pos += 8;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private String convertToCelsius(double kelvin) {
        DecimalFormat df = new DecimalFormat("#.##");
        double celsius = kelvin - 273.15;
        return df.format(celsius).replace(".", ",") + " °C";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView max, min, date;
        private final Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            max = (TextView) itemView.findViewById(R.id.summary_max);
            min = (TextView) itemView.findViewById(R.id.summary_min);
            date = (TextView) itemView.findViewById(R.id.summary_date);
            context = itemView.getContext();

        }
    }
}
