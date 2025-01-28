package srinivas.Weather_App.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import srinivas.Weather_App.Model.RequestInfo;
import srinivas.Weather_App.Service.WeatherAppService;
import srinivas.Weather_App.Model.WeatherAppModel;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class WeatherAppController {
    @Autowired
    private WeatherAppService service;

    @Value("${Max_Requests}")
    private int Max_Requests;
    private final Map<String, RequestInfo> requestCounts = new HashMap<>();

    @GetMapping("/weather")
    public WeatherAppModel getWeatherData(@RequestParam(value = "ipAddress", required = false) String ipAddress){
        if(isRateLimited(ipAddress)){
            throw new RuntimeException("Rate limit Exceeded, Please try again later");
        }

        try {
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = getClientIp();
            }

            return service.getWeatherData(ipAddress);
        }
        catch(Exception Ex){
            throw new RuntimeException(Ex.getMessage());
        }
    }

    private String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    public boolean isRateLimited(String ipAddress){
        RequestInfo requestInfo = requestCounts.getOrDefault(ipAddress, new RequestInfo(0, Instant.now()));

        if(Instant.now().isAfter(requestInfo.getTimestamp().plusSeconds(60))){
            requestInfo = new RequestInfo(0, Instant.now());
        }

        if(requestInfo.getCount() < Max_Requests){
            requestInfo.setCount(requestInfo.getCount() + 1);
            requestCounts.put(ipAddress, requestInfo);
            return false;
        }

        return true;
    }
}
