package Util;


public class Clock {

    public double start_time = 0.0;
    public double currentTime;
    public double lastClass1Arrival1;
    public double lastClass1Arrival2;
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

    public void incrLastClass1Arrival(int time){
        lastClass1Arrival1 += time;
    }

    public void incrLastClass2Arrival(int time){
        lastClass1Arrival2 += time;
    }
}
