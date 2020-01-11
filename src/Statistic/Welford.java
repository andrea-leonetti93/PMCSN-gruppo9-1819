package Statistic;


public class Welford {

    private double LOC = 0.95;
    private double current_mean = 0.0;
    private double stDeviation;
    private double n = 0.0;


    public void updateWelfordMean(double elem){
        n++;
        double prev_mean = current_mean;
        current_mean += (1/n)*(elem - current_mean);
        stDeviation += (n-1)/(n) * (elem - prev_mean) * (elem - prev_mean);

    }

    public double getCurrent_mean() {
        return current_mean;
    }

    public double getStDeviation() {
        return stDeviation;
    }

    public int getN() {
        return (int) n;
    }
}
