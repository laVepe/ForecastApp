package com.example.pafi.weatherapp.RestAPI;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Forecast implements Parcelable {

    private long dt;
    private Main main;
    private ArrayList<Weather> weather = new ArrayList<>();
    private Clouds clouds;
    private Wind wind;
    private Rain rain;
    private Snow snow;
    private Sys sys;
    private String dt_txt;

    public Forecast() {

    }

    private Forecast(Parcel in) {
        dt = in.readLong();
        main = in.readParcelable(Main.class.getClassLoader());
        in.readTypedList(weather, Weather.CREATOR);
        clouds = in.readParcelable(Clouds.class.getClassLoader());
        wind = in.readParcelable(Wind.class.getClassLoader());
        rain = in.readParcelable(Rain.class.getClassLoader());
        snow = in.readParcelable(Snow.class.getClassLoader());
        sys = in.readParcelable(Sys.class.getClassLoader());
        dt_txt = in.readString();
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dt);
        dest.writeParcelable(main, flags);
        dest.writeTypedList(weather);
        dest.writeParcelable(clouds, flags);
        dest.writeParcelable(wind, flags);
        dest.writeParcelable(rain, flags);
        dest.writeParcelable(snow, flags);
        dest.writeParcelable(sys, flags);
        dest.writeString(dt_txt);
    }

    public static final Parcelable.Creator<Forecast> CREATOR
            = new Parcelable.Creator<Forecast>() {

        @Override
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    public String convertToCelsius(double kelvin) {
        DecimalFormat df = new DecimalFormat("#.#");
        double celsius = kelvin - 273.15;
        return df.format(celsius).replace(".", ",") + "°C";
    }

    public String formatDt(long timeStamp) {
        DateFormat dateFormat = new SimpleDateFormat("EEE dd.MM.yyyy", Locale.getDefault());
        Date time = new java.util.Date(timeStamp * 1000);
        return dateFormat.format(time);
    }

    public static class Main implements Parcelable {

        private float temp;
        private float temp_min;
        private float temp_max;
        private float pressure;
        private float sea_level;
        private float grnd_level;
        private float temp_kf;
        private int humidity;

        public Main() {

        }

        private Main(Parcel in) {
            temp = in.readFloat();
            temp_min = in.readFloat();
            temp_max = in.readFloat();
            pressure = in.readFloat();
            sea_level = in.readFloat();
            grnd_level = in.readFloat();
            temp_kf = in.readFloat();
            humidity = in.readInt();
        }

        public float getTemp() {
            return temp;
        }

        public void setTemp(float temp) {
            this.temp = temp;
        }

        public float getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(float temp_min) {
            this.temp_min = temp_min;
        }

        public float getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(float temp_max) {
            this.temp_max = temp_max;
        }

        public float getPressure() {
            return pressure;
        }

        public void setPressure(float pressure) {
            this.pressure = pressure;
        }

        public float getSea_level() {
            return sea_level;
        }

        public void setSea_level(float sea_level) {
            this.sea_level = sea_level;
        }

        public float getGrnd_level() {
            return grnd_level;
        }

        public void setGrnd_level(float grnd_level) {
            this.grnd_level = grnd_level;
        }

        public float getTemp_kf() {
            return temp_kf;
        }

        public void setTemp_kf(float temp_kf) {
            this.temp_kf = temp_kf;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(temp);
            dest.writeFloat(temp_min);
            dest.writeFloat(temp_max);
            dest.writeFloat(pressure);
            dest.writeFloat(sea_level);
            dest.writeFloat(grnd_level);
            dest.writeFloat(temp_kf);
            dest.writeInt(humidity);
        }

        public static final Parcelable.Creator<Main> CREATOR
                = new Parcelable.Creator<Main>() {

            @Override
            public Main createFromParcel(Parcel source) {
                return new Main(source);
            }

            @Override
            public Main[] newArray(int size) {
                return new Main[size];
            }
        };
    }

    public static class Clouds implements Parcelable {

        private int all;

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(all);
        }

        public Clouds() {

        }

        private Clouds(Parcel in) {
            all = in.readInt();
        }

        public static final Parcelable.Creator<Clouds> CREATOR
                = new Parcelable.Creator<Clouds>() {

            @Override
            public Clouds createFromParcel(Parcel source) {
                return new Clouds(source);
            }

            @Override
            public Clouds[] newArray(int size) {
                return new Clouds[size];
            }
        };
    }

    public static class Wind implements Parcelable {

        private float speed;
        private float deg;

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public float getDeg() {
            return deg;
        }

        public void setDeg(float deg) {
            this.deg = deg;
        }

        public Wind() {

        }

        private Wind(Parcel in) {
            speed = in.readFloat();
            deg = in.readFloat();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(speed);
            dest.writeFloat(deg);
        }

        public static final Parcelable.Creator<Wind> CREATOR
                = new Parcelable.Creator<Wind>() {

            @Override
            public Wind createFromParcel(Parcel source) {
                return new Wind(source);
            }

            @Override
            public Wind[] newArray(int size) {
                return new Wind[size];
            }
        };
    }

    public static class Rain implements Parcelable {

        @SerializedName("3h")
        private float h;

        public float getH() {
            return h;
        }

        public void setH(float h) {
            this.h = h;
        }

        private Rain(Parcel in) {
            h = in.readFloat();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(h);
        }

        public static final Parcelable.Creator<Rain> CREATOR
                = new Parcelable.Creator<Rain>() {

            @Override
            public Rain createFromParcel(Parcel source) {
                return new Rain(source);
            }

            @Override
            public Rain[] newArray(int size) {
                return new Rain[size];
            }
        };
    }

    public static class Snow implements Parcelable {

        @SerializedName("3h")
        private float h;

        public float getH() {
            return h;
        }

        public void setH(float h) {
            this.h = h;
        }

        private Snow(Parcel in) {
            h = in.readFloat();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(h);
        }

        public static final Parcelable.Creator<Snow> CREATOR
                = new Parcelable.Creator<Snow>() {

            @Override
            public Snow createFromParcel(Parcel source) {
                return new Snow(source);
            }

            @Override
            public Snow[] newArray(int size) {
                return new Snow[size];
            }
        };
    }

    public static class Sys implements Parcelable {

        private String pod;

        public String getPod() {
            return pod;
        }

        public void setPod(String pod) {
            this.pod = pod;
        }

        private Sys(Parcel in) {
            pod = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(pod);
        }

        public static final Parcelable.Creator<Sys> CREATOR
                = new Parcelable.Creator<Sys>() {

            @Override
            public Sys createFromParcel(Parcel source) {
                return new Sys(source);
            }

            @Override
            public Sys[] newArray(int size) {
                return new Sys[size];
            }
        };
    }
}
