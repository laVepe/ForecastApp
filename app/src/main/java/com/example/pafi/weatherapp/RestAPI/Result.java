package com.example.pafi.weatherapp.RestAPI;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class Result  implements Parcelable {

    private City city;
    private String cod;
    private float message;
    private int cnt;
    private ArrayList<Forecast> list = new ArrayList<>();

    public  Result() {

    }

    private Result(Parcel in) {
        city = in.readParcelable(City.class.getClassLoader());
        cod = in.readString();
        message = in.readFloat();
        cnt = in.readInt();
        in.readTypedList(list, Forecast.CREATOR);
    }

    public ArrayList<Forecast> getList() {
        return list;
    }

    public void setList(ArrayList<Forecast> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public float getMessage() {
        return message;
    }

    public void setMessage(float message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(city, flags);
        dest.writeString(cod);
        dest.writeFloat(message);
        dest.writeInt(cnt);
        dest.writeTypedList(list);
    }

    public static final Parcelable.Creator<Result> CREATOR
            = new Parcelable.Creator<Result>() {

        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public static class City implements Parcelable {

        private int id;
        private String name;
        private Coord coord;
        private String country;
        private long population;
        private Sys sys;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Coord getCoord() {
            return coord;
        }

        public void setCoord(Coord coord) {
            this.coord = coord;
        }

        public long getPopulation() {
            return population;
        }

        public void setPopulation(long population) {
            this.population = population;
        }

        public City() {

        }

        private City(Parcel in) {
            id = in.readInt();
            name = in.readString();
            coord = in.readParcelable(Coord.class.getClassLoader());
            country = in.readString();
            population = in.readLong();
            sys = in.readParcelable(Sys.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeParcelable(coord, flags);
            dest.writeString(country);
            dest.writeLong(population);
            dest.writeParcelable(sys, flags);
        }

        public static final Parcelable.Creator<City> CREATOR
                = new Parcelable.Creator<City>() {

            @Override
            public City createFromParcel(Parcel source) {
                return new City(source);
            }

            @Override
            public City[] newArray(int size) {
                return new City[size];
            }
        };

        public static class Coord implements Parcelable {

            private float lon;
            private float lat;

            public float getLon() {
                return lon;
            }

            public void setLon(float lon) {
                this.lon = lon;
            }

            public float getLat() {
                return lat;
            }

            public void setLat(float lat) {
                this.lat = lat;
            }

            public static final Parcelable.Creator<Coord> CREATOR
                    = new Parcelable.Creator<Coord>() {

                @Override
                public Coord createFromParcel(Parcel source) {
                    return new Coord(source);
                }

                @Override
                public Coord[] newArray(int size) {
                    return new Coord[size];
                }
            };

            public Coord() {

            }

            private Coord(Parcel in) {
                lon = in.readFloat();
                lat = in.readFloat();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeFloat(lon);
                dest.writeFloat(lat);
            }
        }
    }

    public static class Sys implements Parcelable {

        private long population;

        public long getPopulation() {
            return population;
        }

        public void setPopulation(long population) {
            this.population = population;
        }

        public Sys() {

        }

        private Sys(Parcel in) {
            population = in.readLong();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(population);
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
