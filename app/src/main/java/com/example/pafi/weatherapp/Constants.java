package com.example.pafi.weatherapp;

public class Constants {
    public static final String BASE_URL = "http://api.openweathermap.org/data/";
    public static final String SEARCH_STRING = "searchString";
    public static final String API_KEY = "69dd880b968b6f8f0a2440f2e93beadb";
    public static final String REQUEST = "https://en.wikipedia.org/w/api.php?action=query&titles=REPLACEME&prop=images&format=json";
    public static final String FROM_TITLE="https://en.wikipedia.org/w/api.php?action=query&format=json&prop=imageinfo&titles=REPLACEME&iiprop=url";
    public static final String DEFAUKT_IMG = "https://static.vecteezy.com/system/resources/previews/000/118/897/original/free-flat-city-landscape-vector-illustration.jpg";
    public static final String PATTERN = "(\\\"title\\\"\\:\\\".*?images.*?)(\\\"title\\\"\\:\\\")(.*?)(\\\"\\})";
    public static final String URL_PATTERN ="(\\{\\\"url\\\"\\:\\\")(.*?)(\",)";
    public static final int FORECASTS_PER_DAY = 8;
    public static final int NUM_OF_FORECASTED_DAYS = 5;
}
