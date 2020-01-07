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

