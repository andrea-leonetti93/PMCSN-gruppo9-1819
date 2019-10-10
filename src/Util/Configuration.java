package Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    public static long SEED;
    public static double LAMBDA1;
    public static double LAMBDA2;
    public static double MU1CLET;
    public static double MU2CLET;
    public static double MU1CLOUD;
    public static double MU2CLOUD;
    public static int N;
    public static int S;
    public static int STOP_TIME;
    public static double PROB_LAMBDA1;
    public static double PROB_LAMBDA2;
    private static Configuration configuration = null;

    public static Configuration getInstance(){
        if(configuration == null){
            configuration = new Configuration();
        }
        return configuration;
    }

    public static void startConfiguration() {
        try (
                InputStream input = Configuration.class.getClassLoader().getResourceAsStream("Parameters.properties")) {

            Properties prop = new Properties();

            if(input == null){
                System.out.println("Didn't find configuration file, check the name.");
                System.exit(1);
            }
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            SEED = Long.parseLong(prop.getProperty("seed"));
            LAMBDA1 = Double.parseDouble(prop.getProperty("lambda1"));
            LAMBDA2 = Double.parseDouble(prop.getProperty("lambda2"));
            MU1CLET = Double.parseDouble(prop.getProperty("mu1Clet"));
            MU2CLET = Double.parseDouble(prop.getProperty("mu2Clet"));
            MU1CLOUD = Double.parseDouble(prop.getProperty("mu1Cloud"));
            MU2CLOUD = Double.parseDouble(prop.getProperty("mu2Cloud"));
            N = Integer.parseInt(prop.getProperty("N"));
            S = Integer.parseInt(prop.getProperty("S"));
            STOP_TIME = Integer.parseInt(prop.getProperty("STOP_TIME"));
            PROB_LAMBDA1 = Double.parseDouble(prop.getProperty("prob_lambda1"));
            PROB_LAMBDA2 = Double.parseDouble(prop.getProperty("prob_lambda2"));
            System.out.println("\n SEED: " + SEED);
            System.out.println("\n LAMBDA1: " + LAMBDA1);
            System.out.println("\n LAMBDA2: " + LAMBDA2);
            System.out.println("\n MU1CLET: " + MU1CLET);
            System.out.println("\n MU2CLET: " + MU2CLET);
            System.out.println("\n MU1CLOUD: " + MU1CLOUD);
            System.out.println("\n MU2CLOUD: " + MU2CLOUD);
            System.out.println("\n N: " + N);
            System.out.println("\n S: " + S);
            System.out.println("\n STOP_TIME: " + STOP_TIME);
            System.out.println("\n PROB_LAMBDA1: " + PROB_LAMBDA1);
            System.out.println("\n PROB_LAMBDA2: " + PROB_LAMBDA2);
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
    }
}
