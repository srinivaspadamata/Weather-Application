package srinivas.Weather_App.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import srinivas.Weather_App.Constants.WeatherAPIConstants;
import srinivas.Weather_App.Model.WeatherAppModel;
import srinivas.Weather_App.Model.WeatherResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherAppService {
    private final RestTemplate restTemplate;
    @Value("${openWeatherApiKey}")
    private String weatherApiKey;
    //private Map<String, WeatherAppModel> cachedWeatherData = new HashMap<>(); // use this to cache manually
    @Autowired
    public WeatherAppService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Cacheable(value="weatherData", key = "#ip")
    public WeatherAppModel getWeatherData(String ip){
        /* if(cachedWeatherData.containsKey(ip)){
            return cachedWeatherData.get(ip);
        } */
        WeatherAppModel weatherAppModel = new WeatherAppModel();
        String locationUrl = WeatherAPIConstants.IP_API_URL + ip;
        weatherAppModel.setIp(ip);
        ResponseEntity<WeatherAppModel.Location> locationData = restTemplate.getForEntity(locationUrl, WeatherAppModel.Location.class);
        weatherAppModel.setLocation(locationData.getBody());

        String city = weatherAppModel.getLocation().getCity();
        String countryCode = weatherAppModel.getLocation().getCountryCode();
        WeatherAppModel.Weather weather = getWeatherData(city, countryCode);
        weatherAppModel.setWeather(weather);
        //cachedWeatherData.put(ip,weatherAppModel);

        return weatherAppModel;
    }

    public WeatherAppModel.Weather getWeatherData(String city, String countryCode){
        WeatherResponse weatherData = getWeatherResponse(city, countryCode);
        WeatherAppModel.Weather weather = new WeatherAppModel.Weather();
        double tempInCelsius = BigDecimal.valueOf(weatherData.getMain().getTemp() - 273.15).setScale(1, RoundingMode.HALF_UP).doubleValue();
        double humidity = Math.round(weatherData.getMain().getHumidity());
        weather.setTemp(tempInCelsius);
        weather.setHumidity(humidity);
        weather.setDescription(weatherData.getWeather().get(0).getDescription());
        return weather;
    }

    public WeatherResponse getWeatherResponse(String city, String countryCode){
        String weatherUrl = WeatherAPIConstants.WEATHER_API_URL + city + "," + countryCode + "&appid=" + weatherApiKey;
        ResponseEntity<WeatherResponse> weatherResponse = restTemplate.getForEntity(weatherUrl, WeatherResponse.class);
        return weatherResponse.getBody();
    }
}
