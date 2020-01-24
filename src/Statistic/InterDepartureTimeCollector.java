package Statistic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InterDepartureTimeCollector {

    private static List<Double> metrics1 = new ArrayList<>();
    private static List<Double> metrics2 = new ArrayList<>();
    private static FileWriter fw1;
    private static FileWriter fw2;

    static {
        try {
            fw1 = new FileWriter(System.getProperty("user.dir") + File.separator + "stat2" + File.separator + "Batch_InterDepartureTimeJ1Expocomplete.csv");
            fw2 = new FileWriter(System.getProperty("user.dir") + File.separator + "stat2" + File.separator + "Batch_InterDepartureTimeJ2Expocomplete.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addElement1(double value){
        metrics1.add(value);
    }

    public static void addElement2(double value){
        metrics2.add(value);
    }

    public static void printMetrics(){
        for(Double d : metrics1){
            try {
                fw1.append(d.toString());
                fw1.append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fw1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Double d : metrics2){
            try {
                fw2.append(d.toString());
                fw2.append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fw2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
