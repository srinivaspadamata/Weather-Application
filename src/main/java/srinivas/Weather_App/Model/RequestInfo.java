package srinivas.Weather_App.Model;

import java.time.Instant;

public class RequestInfo {
    int count;
    Instant timestamp;

    public RequestInfo(int count, Instant timestamp) {
        this.count = count;
        this.timestamp = timestamp;
    }
    public int getCount(){
        return count;
    }
    public Instant getTimestamp(){
        return timestamp;
    }
    public void setCount(int count){
        this.count = count;
    }
    public void setTimestamp(Instant timestamp){
        this.timestamp = timestamp;
    }
}
