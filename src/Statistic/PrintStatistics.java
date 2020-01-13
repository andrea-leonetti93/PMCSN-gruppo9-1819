package Statistic;

import Util.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PrintStatistics {

    private static PrintStatistics printStatistics = null;
    private IoC ioC = IoC.getInstance();
    // global
    private FileWriter populationFile;
    // global
    private FileWriter throughputFile;
    // single
    private FileWriter populationClass1CletFile;
    private FileWriter populationClass2CletFile;
    private FileWriter populationClass1CloudFile;
    private FileWriter populationClass2CloudFile;
    // single
    private FileWriter throughputClass1CletFile;
    private FileWriter throughputClass2CletFile;
    private FileWriter throughputClass1CloudFile;
    private FileWriter throughputClass2CloudFile;
    // single
    private FileWriter serviceTimeCloudFile;
    private FileWriter serviceTimeCloudletFile;
    private FileWriter serviceTimeGlobalFile;
    private FileWriter serviceTimeClass1CletFile;
    private FileWriter serviceTimeClass2CletFile;
    private FileWriter serviceTimeClass1CloudFile;
    private FileWriter serviceTimeClass2CloudFile;
    private FileWriter serviceTimeClass2PreemptedFile;
    private static int algorithm = Configuration.ALGORITHM;
    private static boolean hyperexpo = Configuration.HYPEREXPO;

    private PrintStatistics(){
        String path1;
        String path2;
        String path3;
        CreateDirectory();
        if(algorithm == 1){
            path1 = System.getProperty("user.dir") + File.separator + "stat1" + File.separator + "Population";
            path2 = System.getProperty("user.dir") + File.separator + "stat1" + File.separator + "ServiceTime";
            path3 = System.getProperty("user.dir") + File.separator + "stat1" + File.separator + "Throughput";
        }else{
            path1 = System.getProperty("user.dir") + File.separator + "stat2" + File.separator + "Population";
            path2 = System.getProperty("user.dir") + File.separator + "stat2" + File.separator + "ServiceTime";
            path3 = System.getProperty("user.dir") + File.separator + "stat2" + File.separator + "Throughput";
        }
        try{
            if(hyperexpo){
                populationFile = new FileWriter(path1 + File.separator + "Batch_PopulationHyper.csv");
                throughputFile = new FileWriter(path3 + File.separator + "Batch_ThrpughputHyper.csv");
                populationClass1CletFile = new FileWriter(path1 + File.separator + "Batch_PopClass1CletHyper.csv");
                populationClass2CletFile = new FileWriter(path1 + File.separator + "Batch_PopClass2CletHyper.csv");
                populationClass1CloudFile = new FileWriter(path1 + File.separator + "Batch_PopClass1CloudHyper.csv");
                populationClass2CloudFile = new FileWriter(path1 + File.separator + "Batch_PopClass2CloudHyper.csv");
                throughputClass1CletFile = new FileWriter(path3 + File.separator + "Batch_ThrClass1CletHyper.csv");
                throughputClass2CletFile = new FileWriter(path3 + File.separator + "Batch_ThrClass2CletHyper.csv");
                throughputClass1CloudFile = new FileWriter(path3 + File.separator + "Batch_ThrClass1CloudHyper.csv");
                throughputClass2CloudFile = new FileWriter(path3 + File.separator + "Batch_ThrClass2CloudHyper.csv");
                serviceTimeCloudFile = new FileWriter(path2 + File.separator + "Batch_STimeCloudHyper.csv");
                serviceTimeCloudletFile = new FileWriter(path2 + File.separator + "Batch_STimeCletHyper.csv");
                serviceTimeGlobalFile = new FileWriter(path2 + File.separator + "Batch_STimeGlobalHyper.csv");
                serviceTimeClass1CletFile = new FileWriter(path2 + File.separator + "Batch_STimeClass1CletHyper.csv");
                serviceTimeClass2CletFile = new FileWriter(path2 + File.separator + "Batch_STimeClass2CletHyper.csv");
                serviceTimeClass1CloudFile = new FileWriter(path2 + File.separator + "Batch_STimeClass1CloudHyper.csv");
                serviceTimeClass2CloudFile = new FileWriter(path2 + File.separator + "Batch_STimeClass2CloudHyper.csv");
                serviceTimeClass2PreemptedFile = new FileWriter(path2 + File.separator + "Batch_STimeClass2PreemptedHyper.csv");
            }else{
                populationFile = new FileWriter(path1 + File.separator + "Batch_MeansExpo.csv");
                throughputFile = new FileWriter(path3 + File.separator + "Batch_ThrpughputExpo.csv");
                populationClass1CletFile = new FileWriter(path1 + File.separator + "Batch_PopClass1CletExpo.csv");
                populationClass2CletFile = new FileWriter(path1 + File.separator + "Batch_PopClass2CletExpo.csv");
                populationClass1CloudFile = new FileWriter(path1 + File.separator + "Batch_PopClass1CloudExpo.csv");
                populationClass2CloudFile = new FileWriter(path1 + File.separator + "Batch_PopClass2CloudExpo.csv");
                throughputClass1CletFile = new FileWriter(path3 + File.separator + "Batch_ThrClass1CletExpo.csv");
                throughputClass2CletFile = new FileWriter(path3 + File.separator + "Batch_ThrClass2CletExpo.csv");
                throughputClass1CloudFile = new FileWriter(path3 + File.separator + "Batch_ThrClass1CloudExpo.csv");
                throughputClass2CloudFile = new FileWriter(path3 + File.separator + "Batch_ThrClass2CloudExpo.csv");
                serviceTimeCloudFile = new FileWriter(path2 + File.separator + "Batch_STimeCloudExpo.csv");
                serviceTimeCloudletFile = new FileWriter(path2 + File.separator + "Batch_STimeCletExpo.csv");
                serviceTimeGlobalFile = new FileWriter(path2 + File.separator + "Batch_STimeGlobalExpo.csv");
                serviceTimeClass1CletFile = new FileWriter(path2 + File.separator + "Batch_STimeClass1CletExpo.csv");
                serviceTimeClass2CletFile = new FileWriter(path2 + File.separator + "Batch_STimeClass2CletExpo.csv");
                serviceTimeClass1CloudFile = new FileWriter(path2 + File.separator + "Batch_STimeClass1CloudExpo.csv");
                serviceTimeClass2CloudFile = new FileWriter(path2 + File.separator + "Batch_STimeClass2CloudExpo.csv");
                serviceTimeClass2PreemptedFile = new FileWriter(path2 + File.separator + "Batch_STimeClass2PreemptedExpo.csv");
            }
            //TODO check if it's necessary adding the curtime to each file with the statistics, ex: value1CloudMeanPop : curtime
            //fileWriter.append("curtime;");
            populationFile.append("CloudMeanPopulation;");
            populationFile.append("CloudletMeanPopulation;");
            populationFile.append("GlobalMeanPopulation;");
            populationFile.append("\n");
            throughputFile.append("CloudMeanThroughput;");
            throughputFile.append("CloudletMeanThroughput;");
            throughputFile.append("GlobalMeanThroughput;");
            throughputFile.append("\n");
            serviceTimeCloudFile.append("CloudMeanServiceTime;");
            serviceTimeCloudFile.append("\n");
            serviceTimeCloudletFile.append("CloudletMeanServiceTime;");
            serviceTimeCloudletFile.append("\n");
            serviceTimeGlobalFile.append("GlobalMeanServiceTime;");
            serviceTimeGlobalFile.append("\n");
            populationClass1CletFile.append("MeanPopJobClassOneCloudlet;");
            populationClass1CletFile.append("\n");
            populationClass2CletFile.append("MeanPopJobClassTwoCloudlet;");
            populationClass2CletFile.append("\n");
            populationClass1CloudFile.append("MeanPopJobClassOneCloud;");
            populationClass1CloudFile.append("\n");
            populationClass2CloudFile.append("MeanPopJobClassTwoCloud;");
            populationClass2CloudFile.append("\n");
            throughputClass1CletFile.append("MeanThrJobClassOneClet;");
            throughputClass1CletFile.append("\n");
            throughputClass2CletFile.append("MeanThrJobClassTwoClet;");
            throughputClass2CletFile.append("\n");
            throughputClass1CloudFile.append("MeanThrJobClassOneCloud;");
            throughputClass1CloudFile.append("\n");
            throughputClass2CloudFile.append("MeanThrJobClassTwoCloud;");
            throughputClass2CloudFile.append("\n");
            serviceTimeClass1CletFile.append("MeanSTimeJobClassOneClet;");
            serviceTimeClass1CletFile.append("\n");
            serviceTimeClass2CletFile.append("MeanSTimeJobClassTwoClet;");
            serviceTimeClass2CletFile.append("\n");
            serviceTimeClass1CloudFile.append("MeanSTimeJobClassOneCloud;");
            serviceTimeClass1CloudFile.append("\n");
            serviceTimeClass2CloudFile.append("MeanSTimeJobClassTwoCloud;");
            serviceTimeClass2CloudFile.append("\n");
            serviceTimeClass2CloudFile.append("MeanSTimeJobClassTwoPreempted;");
            serviceTimeClass2CloudFile.append("\n");
        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static PrintStatistics getInstance()
    {
        if (printStatistics == null)
            printStatistics = new PrintStatistics();

        return printStatistics;
    }

    private void CreateDirectory(){
        File dir1 = new File(System.getProperty("user.dir") + File.separator + "stat1");
        if (!dir1.exists()) {
            if (dir1.mkdir()) {
                System.out.println("Directory stat1 is created!");
                File popDir = new File(System.getProperty("user.dir") + File.separator + "stat1" + File.separator + "Population");
                if(popDir.mkdir()){
                    System.out.println("Directory Population in stat1 created");
                }else{
                    System.out.println("Failed to create directory Population!");
                }
                File thrDir = new File(System.getProperty("user.dir") + File.separator + "stat1" + File.separator + "Throughput");
                if(thrDir.mkdir()){
                    System.out.println("Directory Throughput in stat1 created");
                }else{
                    System.out.println("Failed to create directory Throughput!");
                }
                File serDir = new File(System.getProperty("user.dir") + File.separator + "stat1" + File.separator + "ServiceTime");
                if(serDir.mkdir()){
                    System.out.println("Directory ServiceTime in stat1 created");
                }else{
                    System.out.println("Failed to create directory ServiceTime!");
                }
            } else {
                System.out.println("Failed to create directory stat1!");
            }
        }
        File dir2 = new File(System.getProperty("user.dir") + File.separator + "stat2");
        if(!dir2.exists()){
            if(dir2.mkdir()){
                System.out.println("Directory stat2 is created!");
                File popDir = new File(System.getProperty("user.dir") + File.separator + "stat2" + File.separator + "Population");
                if(popDir.mkdir()){
                    System.out.println("Directory Population in stat2 created");
                }else{
                    System.out.println("Failed to create directory Population!");
                }
                File thrDir = new File(System.getProperty("user.dir") + File.separator + "stat2" + File.separator + "Throughput");
                if(thrDir.mkdir()){
                    System.out.println("Directory Throughput in stat2 created");
                }else{
                    System.out.println("Failed to create directory Throughput!");
                }
                File serDir = new File(System.getProperty("user.dir") + File.separator + "stat2" + File.separator + "ServiceTime");
                if(serDir.mkdir()){
                    System.out.println("Directory ServiceTime in stat2 created");
                }else{
                    System.out.println("Failed to create directory ServiceTime!");
                }
            }else{
                System.out.println("Failed to create directory stat2!");
            }
        }
    }

    private void writePopulationStatistics() {
        for(int i=0; i<ioC.getIocGlobalMeanPopulation().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocCloudMeanPopulation().get(i));
            sb.append(";");
            sb.append(ioC.getIocCloudletMeanPopulation().get(i));
            sb.append(";");
            sb.append(ioC.getIocGlobalMeanPopulation().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                populationFile.append(sb.toString());
                populationFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeThroughputStatistics() {
        for(int i=0; i<ioC.getIocGlobalThroughputMean().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocCloudThroughputMean().get(i));
            sb.append(";");
            sb.append(ioC.getIocCloudletThroughputMean().get(i));
            sb.append(";");
            sb.append(ioC.getIocGlobalThroughputMean().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                throughputFile.append(sb.toString());
                throughputFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeServiceTimeCLoudStatistics() {
        for(int i=0; i<ioC.getIocCloudServiceMeanTime().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocCloudServiceMeanTime().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                serviceTimeCloudFile.append(sb.toString());
                serviceTimeCloudFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeServiceTimeCLoudletStatistics() {
        for(int i=0; i<ioC.getIocCloudletServiceMeanTime().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocCloudletServiceMeanTime().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                serviceTimeCloudletFile.append(sb.toString());
                serviceTimeCloudletFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeServiceTimeGlobalStatistics() {
        for(int i=0; i<ioC.getIocGlobalServiceMeanTime().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocGlobalServiceMeanTime().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                serviceTimeGlobalFile.append(sb.toString());
                serviceTimeGlobalFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writePopulationClass1CletStatistics() {
        for(int i=0; i<ioC.getIocMeanPopulationJobClassOneClet().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanPopulationJobClassOneClet().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                populationClass1CletFile.append(sb.toString());
                populationClass1CletFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writePopulationClass2CletStatistics() {
        for(int i=0; i<ioC.getIocMeanPopulationJobClassTwoClet().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanPopulationJobClassTwoClet().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                populationClass2CletFile.append(sb.toString());
                populationClass2CletFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writePopulationClass1CloudStatistics() {
        for(int i=0; i<ioC.getIocMeanPopulationJobClassOneCloud().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanPopulationJobClassOneCloud().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                populationClass1CloudFile.append(sb.toString());
                populationClass1CloudFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writePopulationClass2CloudStatistics() {
        for(int i=0; i<ioC.getIocMeanPopulationJobClassTwoCloud().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanPopulationJobClassTwoCloud().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                populationClass2CloudFile.append(sb.toString());
                populationClass2CloudFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeThroughputClass1CletStatistics() {
        for(int i=0; i<ioC.getIocMeanThroughputJobClassOneClet().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanThroughputJobClassOneClet().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                throughputClass1CletFile.append(sb.toString());
                throughputClass1CletFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeThroughputClass2CletStatistics() {
        for(int i=0; i<ioC.getIocMeanThroughputJobClassTwoClet().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanThroughputJobClassTwoClet().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                throughputClass2CletFile.append(sb.toString());
                throughputClass2CletFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeThroughputClass1CloudStatistics() {
        for(int i=0; i<ioC.getIocMeanThroughputJobClassOneCloud().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanThroughputJobClassOneCloud().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                throughputClass1CloudFile.append(sb.toString());
                throughputClass1CloudFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeThroughputClass2CloudStatistics() {
        for(int i=0; i<ioC.getIocMeanThroughputJobClassTwoCloud().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanThroughputJobClassTwoCloud().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                throughputClass2CloudFile.append(sb.toString());
                throughputClass2CloudFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeServiceTimeClass1CletStatistics() {
        for(int i=0; i<ioC.getIocMeanServiceTimeJobClassOneClet().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanServiceTimeJobClassOneClet().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                serviceTimeClass1CletFile.append(sb.toString());
                serviceTimeClass1CletFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeServiceTimeClass2CletStatistics() {
        for(int i=0; i<ioC.getIocMeanServiceTimeJobClassTwoClet().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanServiceTimeJobClassTwoClet().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                serviceTimeClass2CletFile.append(sb.toString());
                serviceTimeClass2CletFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeServiceTimeClass1CloudStatistics() {
        for(int i=0; i<ioC.getIocMeanServiceTimeJobClassOneCloud().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanServiceTimeJobClassOneCloud().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                serviceTimeClass1CloudFile.append(sb.toString());
                serviceTimeClass1CloudFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeServiceTimeClass2CloudStatistics() {
        for(int i=0; i<ioC.getIocMeanServiceTimeJobClassTwoCloud().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanServiceTimeJobClassTwoCloud().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                serviceTimeClass2CloudFile.append(sb.toString());
                serviceTimeClass2CloudFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeServiceTimeClass2PreemptedStatistics() {
        for(int i=0; i<ioC.getIocMeanServiceTimeJobClassTwoPreempted().size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(ioC.getIocMeanServiceTimeJobClassTwoPreempted().get(i));
            sb.append(";");
            sb.append("\n");
            try {
                serviceTimeClass2PreemptedFile.append(sb.toString());
                serviceTimeClass2PreemptedFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeStatistics(){
        writePopulationStatistics();
        writeThroughputStatistics();
        writeServiceTimeCLoudStatistics();
        writeServiceTimeCLoudletStatistics();
        writeServiceTimeGlobalStatistics();
        writePopulationClass1CletStatistics();
        writePopulationClass2CletStatistics();
        writePopulationClass1CloudStatistics();
        writePopulationClass2CloudStatistics();
        writeThroughputClass1CletStatistics();
        writeThroughputClass2CletStatistics();
        writeThroughputClass1CloudStatistics();
        writeThroughputClass2CloudStatistics();
        writeServiceTimeClass1CletStatistics();
        writeServiceTimeClass2CletStatistics();
        writeServiceTimeClass1CloudStatistics();
        writeServiceTimeClass2CloudStatistics();
        if(algorithm == 2){
            writeServiceTimeClass2PreemptedStatistics();
        }
    }

}
