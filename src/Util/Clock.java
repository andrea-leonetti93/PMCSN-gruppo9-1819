package Util;


public class Clock {

    public double start_time = 0.0;
    public double currentTime;
    public double lastClass1Arrival;

    private static Clock clockInstance = Clock.getInstance();

    public static Clock getInstance(){
        if(clockInstance == null){
            clockInstance = new Clock();
        }
        return clockInstance;
    }

    private Clock() {}

    public void incrCurrentTime(int time){
        currentTime += time;
    }
}
