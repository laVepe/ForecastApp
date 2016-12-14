package com.example.pafi.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pafi.weatherapp.RestAPI.Forecast;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private ArrayList<Forecast> data;
    private static final String IMAGE_URL = "http://openweathermap.org/img/w/";
    private ViewGroup group;

    public DetailAdapter(ArrayList<Forecast> list) {
        this.data = list;
    }

    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        group = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item, parent, false);
        DetailAdapter.ViewHolder vh = new DetailAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DetailAdapter.ViewHolder holder, int position) {
        Forecast item = data.get(position);
        String time = item.getDt_txt().substring(11);
        holder.time.setText(time);
        holder.temp.setText(convertToCelsius(item.getMain().getTemp()));
        holder.max.setText(convertToCelsius(item.getMain().getTemp_max()));
        holder.min.setText(convertToCelsius(item.getMain().getTemp_min()));
        holder.press.setText(String.valueOf(item.getMain().getPressure())+" hPa");
        holder.humidity.setText(String.valueOf(item.getMain().getHumidity())+"%");
        holder.windSpeed.setText(String.valueOf(item.getWind().getSpeed())+" m/s");
        float f = item.getWind().getDeg();
        DecimalFormat df = new DecimalFormat("#.##");
        holder.windDir.setText(String.valueOf(df.format(f))+"°");
        holder.clouds.setText("Cloudiness: " + String.valueOf(item.getClouds().getAll())+"%");
        holder.desc.setText("Weather: " + item.getWeather().get(0).getMain() + " - " + item.getWeather().get(0).getDescription());
        if(item.getRain() != null) holder.rain.setText("Rain: " + String.valueOf(item.getRain().getH()) + " mm");
        else holder.rain.setVisibility(View.GONE);
        if(item.getSnow() != null) holder.snow.setText("Snow: " + String.valueOf(item.getSnow().getH()));
        else holder.snow.setVisibility(View.GONE);
        Glide
                .with(group.getContext())
                .load(IMAGE_URL + item.getWeather().get(0).getIcon() + ".png")
                .centerCrop()
                .crossFade()
                .into(holder.image);
    }

    private String convertToCelsius(double kelvin) {
        DecimalFormat df = new DecimalFormat("#.#");
        double celsius = kelvin - 273.15;
        return df.format(celsius).replace(".", ",") + "°C";
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView time, temp, max, min, humidity, windSpeed, windDir;
        TextView press, desc, clouds, rain, snow;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            temp = (TextView) itemView.findViewById(R.id.temp);
            max = (TextView) itemView.findViewById(R.id.tempMax);
            min = (TextView) itemView.findViewById(R.id.tempMin);
            press = (TextView) itemView.findViewById(R.id.pressure);
            humidity = (TextView) itemView.findViewById(R.id.humidity);
            windSpeed = (TextView) itemView.findViewById(R.id.wind_speed);
            windDir = (TextView) itemView.findViewById(R.id.wind_dir);
            desc = (TextView) itemView.findViewById(R.id.desc);
            clouds = (TextView) itemView.findViewById(R.id.clouds);
            rain = (TextView) itemView.findViewById(R.id.rain);
            snow = (TextView) itemView.findViewById(R.id.snow);
            image = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }
}
