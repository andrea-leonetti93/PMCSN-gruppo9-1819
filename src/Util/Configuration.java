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
    public static boolean BATCH_MEANS;
    public static double P_HYPEREXPO;
    public static boolean HYPEREXPO;
    public static int ALGORITHM;
    public static double SETUP_TIME;
    public static boolean SERVICE_TIME_PREEMPTION;
    public static double BATCH_DIM;
    private static Configuration configuration = null;

    public static Configuration getInstance(){
        if(configuration == null){
            configuration = new Configuration();
            startConfiguration();
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
            BATCH_MEANS = Boolean.parseBoolean(prop.getProperty("batch_means"));
            P_HYPEREXPO = Double.parseDouble(prop.getProperty("p_hyperexpo"));
            HYPEREXPO = Boolean.parseBoolean(prop.getProperty("hyperexpo"));
            ALGORITHM = Integer.parseInt(prop.getProperty("algorithm"));
            SETUP_TIME = Double.parseDouble(prop.getProperty("setup_time"));
            SERVICE_TIME_PREEMPTION = Boolean.parseBoolean(prop.getProperty("service_time_preemption"));
            BATCH_DIM = Double.parseDouble(prop.getProperty("batch_dim"));
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
    }
}
