package com.example.pafi.weatherapp;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pafi.weatherapp.RestAPI.Result;
import com.example.pafi.weatherapp.RestAPI.ResultAPI;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<Result> {

    private LocationManager locationManager;
    private LocationListener locationListener;
    public RecyclerView recyclerView;
    private TextView emptyTextView;
    private Retrofit retrofit;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.summary_toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        emptyTextView = (TextView) findViewById(R.id.empty);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String searchString = preferences.getString(Constants.SEARCH_STRING, "");
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String searchString = preferences.getString(Constants.SEARCH_STRING, "");
                if (!isConnected()) showMessage();
                else {
                    hideMessage();
                    if (searchString.equals("")) makeLocationRequest();
                    else makeSearchRequest();
                }
                refresh.setRefreshing(false);
            }
        });

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (isLocationOn()) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                              }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            } else {
                if(searchString.equals("")) makeLocationRequest();
                else makeSearchRequest();
            }
        }

        if(!isConnected()) showMessage(); // Network connection turned off

    }

    private void showMessage() {
        recyclerView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
    }

    private void hideMessage() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private boolean isLocationOn() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void makeLocationRequest() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            List<String> providers = locationManager.getProviders(true);
            Location location = null;
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (location == null || l.getAccuracy() < location.getAccuracy()) {
                    // Found best last known location: %s", l);
                    location = l;
                }
            }
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ResultAPI weather = retrofit.create(ResultAPI.class);
                Call<Result> call = weather.getByLocation(latitude, longitude);
                call.enqueue(this);
            }
        }

    }

    private void makeSearchRequest() {
        retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        ResultAPI weather = retrofit.create(ResultAPI.class);
        String searchString = preferences.getString(Constants.SEARCH_STRING, "");
        weather.getByCity(searchString + ",us").enqueue(this);
    }

    @Override
    public void onResponse(Response<Result> response, Retrofit retrofit) {
        int size = response.body().getList().size();
        for(int i = 0; i < size; i++) {
            if(getSupportActionBar() != null) getSupportActionBar().setTitle(getString(R.string.app_name) + " - " + response.body().getCity().getName());
            recyclerView.setAdapter(new SummaryAdapter(response.body()));
        }
        System.out.println("Responses " + size);
    }

    @Override
    public void onFailure(Throwable t) {
        System.out.println("Error message: " + t.getLocalizedMessage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                System.out.println("Permission granted.");
                makeLocationRequest();

            } else {
                System.out.println("Permission not granted.");
                Toast.makeText(this, "Sorry, the app can't work without this permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final MenuItem locationItem = menu.findItem(R.id.action_location);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setQueryHint("Show forecast for city");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            final SearchView finalSearchView = searchView;
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    editor.putString(Constants.SEARCH_STRING, query);
                    editor.apply();
                    if(retrofit != null) {
                        ResultAPI weather = retrofit.create(ResultAPI.class);
                        Call<Result> call = weather.getByCity(query + ",us");
                        call.enqueue(MainActivity.this);
                    }
                    else System.out.println("retrofit == null");
                    finalSearchView.onActionViewCollapsed();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_location) {
            editor.putString(Constants.SEARCH_STRING, "");
            editor.apply();
            if(locationOn()) makeLocationRequest();
            else Toast.makeText(this, R.string.message_gps_turned_off, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean locationOn() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
