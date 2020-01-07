package Statistic;

import Distribution.Rvms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class IoC {
    private static IoC instance = null;
    private static double LOC = 0.95;
    private Rvms rvms = new Rvms();
    public ArrayList<Double> iocCloudMeanPopulation = new ArrayList<>();
    public ArrayList<Double> iocCloudletMeanPopulation = new ArrayList<>();
    public ArrayList<Double> iocGlobalMeanPopulation = new ArrayList<>();
    public ArrayList<Double> iocCloudThroughputMean = new ArrayList<>();
    public ArrayList<Double> iocCloudletThroughputMean = new ArrayList<>();
    public ArrayList<Double> iocGlobalThroughputMean = new ArrayList<>();
    public ArrayList<Double> iocCloudServiceMeanTime = new ArrayList<>();
    public ArrayList<Double> iocCloudletServiceMeanTime = new ArrayList<>();
    public ArrayList<Double> iocGlobalServiceMeanTime = new ArrayList<>();

    private IoC(){};

    public static IoC getInstance(){
        if(instance==null){
            instance = new IoC();
        }
        return instance;
    }

    public double[] computeIoC(ArrayList<Double> givenNumbers){
        double sampleMean = 0.0;
        double variance = 0.0;
        double standardDeviation;
        double t_student;
        double width = 0.0;
        long n = 0;

        for(Double actualValue: givenNumbers){
            n++;
            double diff = actualValue - sampleMean;
            variance += (diff * diff * (n-1)/n);
            sampleMean += (diff/n);
        }
        standardDeviation = Math.sqrt(variance);

        if(n>1){
            double u = 1.0 - 0.5*(1.0 - LOC);                                            /* interval parameter  */
            t_student = rvms.idfStudent(n - 1, u);                                    /* critical value of t */
            width = t_student * standardDeviation / Math.sqrt(n - 1);                    /* interval half width */
        }
        double[] confidenceInterval = {sampleMean, width};
        return confidenceInterval;
    }

    public void setIocCloudMeanPopulation(double value) {
        this.iocCloudMeanPopulation.add(value);
    }

    public void setIocCloudletMeanPopulation(double value) {
        this.iocCloudletMeanPopulation.add(value);
    }

    public void setIocGlobalMeanPopulation(double value) {
        this.iocGlobalMeanPopulation.add(value);
    }

    public void setIocCloudThroughputMean(double value) {
        this.iocCloudThroughputMean.add(value);
    }

    public void setIocCloudletThroughputMean(double value) {
        this.iocCloudletThroughputMean.add(value);
    }

    public void setIocGlobalThroughputMean(double value) {
        this.iocGlobalThroughputMean.add(value);
    }

    public void setIocCloudServiceMeanTime(double value) {
        this.iocCloudServiceMeanTime.add(value);
    }

    public void setIocCloudletServiceMeanTime(double value) {
        this.iocCloudletServiceMeanTime.add(value);
    }

    public void setIocGlobalServiceMeanTime(double value) {
        this.iocGlobalServiceMeanTime.add(value);
    }

    public static void main(String[] args) {
        long   n    = 0;                     /* counts data points */
        double sum  = 0.0;
        double mean = 0.0;
        double data;
        double stdev;
        double u, t, w;
        double diff;

        String line ="";
        Rvms rvms = new Rvms();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            line = br.readLine();

            while (line!=null) {         /* use Welford's one-pass method */
                StringTokenizer tokenizer = new StringTokenizer(line);
                if(tokenizer.hasMoreTokens()){
                    data = Double.parseDouble(tokenizer.nextToken());

                    n++;                 /* and standard deviation        */
                    diff  = data - mean;
                    sum  += diff * diff * (n - 1.0) / n;
                    mean += diff / n;
                }

                line = br.readLine();

            }
        }catch(IOException e){
            System.err.println(e);
            System.exit(1);
        }

        stdev  = Math.sqrt(sum / n);

        DecimalFormat df = new DecimalFormat("###0.00");

        if (n > 1) {
            u = 1.0 - 0.5 * (1.0 - LOC);              /* interval parameter  */
            t = rvms.idfStudent(n - 1, u);            /* critical value of t */
            w = t * stdev / Math.sqrt(n - 1);         /* interval half width */

            System.out.print("\nbased upon " + n + " data points");
            System.out.print(" and with " + (int) (100.0 * LOC + 0.5) +
                    "% confidence\n");
            System.out.print("the expected value is in the interval ");
            System.out.print( df.format(mean) + " +/- " + df.format(w) + "\n");
        }
        else{
            System.out.print("ERROR - insufficient data\n");
        }
    }
}

