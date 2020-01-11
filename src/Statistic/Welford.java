package Statistic;


public class Welford {

    private double LOC = 0.95;
    private double current_mean = 0.0;
    private double current_var;
    private double n = 0.0;


    public void updateWelfordMean(double elem){
        n++;
        double prev_mean = current_mean;
        current_mean += (1/n)*(elem - current_mean);
        current_var += (n-1)/(n) * (elem - prev_mean) * (elem - prev_mean);
    }

    public double getCurrent_mean() {
        return current_mean;
    }

    public double getVariance() {
        return current_var/n;
    }

    public int getN() {
        return (int) n;
    }
}
