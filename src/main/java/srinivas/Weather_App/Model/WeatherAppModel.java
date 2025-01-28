package srinivas.Weather_App.Model;

import java.io.Serializable;

public class WeatherAppModel implements Serializable {
    private String ip;
    private Location location;
    private Weather weather;

    public String getIp(){
        return ip;
    }
    public void setIp(String ip){
        this.ip = ip;
    }
    public Location getLocation(){
        return location;
    }
    public void setLocation(Location location){
        this.location = location;
    }
    public Weather getWeather(){
        return weather;
    }
    public void setWeather(Weather weather){
        this.weather = weather;
    }

    public static class Location{
        private String city;
        private String countryCode;
        private String country;

        public String getCity(){
            return city;
        }
        public void setCity(String city){
            this.city = city;
        }
        public String getCountryCode() {
            return countryCode;
        }
        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }
        public String getCountry(){
            return country;
        }
        public void setCountry(String country){
            this.country = country;
        }
    }
    public static class Weather{
        private double temp;
        private double humidity;
        private String description;

        public double getTemp(){
            return temp;
        }
        public void setTemp(double temp){
            this.temp = temp;
        }
        public double getHumidity(){
            return humidity;
        }
        public void setHumidity(double humidity){
            this.humidity = humidity;
        }
        public String getDescription(){
            return description;
        }
        public void setDescription(String description){
            this.description = description;
        }
    }
}


