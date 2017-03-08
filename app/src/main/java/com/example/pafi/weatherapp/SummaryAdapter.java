package com.example.pafi.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pafi.weatherapp.RestAPI.Forecast;
import com.example.pafi.weatherapp.RestAPI.Result;
import com.example.pafi.weatherapp.databinding.SummaryItemBinding;

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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SummaryAdapter.ViewHolder holder, final int position) {
        Forecast forecast = data.getList().get(pos);
        holder.bind(forecast);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.context, ForecastDetailActivity.class);
                i.putExtra("position", position);
                i.putExtra("result", data);
                holder.context.startActivity(i);
            }
        });
        pos += Constants.FORECASTS_PER_DAY;
    }

    @Override
    public int getItemCount() {
        return Constants.NUM_OF_FORECASTED_DAYS;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SummaryItemBinding binding;
        private final Context context;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            context = itemView.getContext();
        }

        void bind(Forecast item) {
            binding.setForecast(item);
        }
    }
}
