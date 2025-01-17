package Statistic;

import Request.CompletedRequest;
import Server.Cloud;
import Server.Cloudlet;
import Util.Clock;
import Util.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BaseStatistic extends Statistic {

    private Welford cloudMeanPopulation;
    private Welford cloudletMeanPopulation;
    private Welford globalMeanPopulation;
    private Welford cloudServiceMeanTime;
    private Welford cloudletServiceMeanTime;
    private Welford globalServiceMeanTime;
    private Welford cloudThroughputMean;
    private Welford cloudletThroughputMean;
    private Welford globalThroughputMean;
    // class job statistics
    private Welford meanPopulationJobClassOneClet;
    private Welford meanPopulationJobClassTwoClet;
    private Welford meanPopulationJobClassOneCloud;
    private Welford meanPopulationJobClassTwoCloud;
    private Welford meanThroughputJobClassOneClet;
    private Welford meanThroughputJobClassTwoClet;
    private Welford meanThroughputJobClassOneCloud;
    private Welford meanThroughputJobClassTwoCloud;
    private Welford meanServiceTimeJobClassOneClet;
    private Welford meanServiceTimeJobClassTwoClet;
    private Welford meanServiceTimeJobClassOneCloud;
    private Welford meanServiceTimeJobClassTwoCloud;
    private Welford meanServiceTimeJobClassTwoPreempted;
    private FileWriter fileWriter;
    private static boolean hyperexpo = Configuration.HYPEREXPO;
    private static int algorithm = Configuration.ALGORITHM;
    private static BaseStatistic baseStatistic = null;

    public static BaseStatistic getInstance(){
        if(baseStatistic == null){
            baseStatistic = new BaseStatistic();
        }
        return baseStatistic;
    }

    private BaseStatistic() {
        String path = System.getProperty("user.dir") + File.separator + "stat1";
        File dir1 = new File(path);
        if (!dir1.exists()) {
            if (dir1.mkdir()) {
                System.out.println("Directory stat1 is created!");
                createSubDir(path);
            } else {
                System.out.println("Failed to create directory stat1!");
            }
        }else{
            createSubDir(path);
        }
        path = System.getProperty("user.dir") + File.separator + "stat2";
        File dir2 = new File(path);
        if(!dir2.exists()){
            if (dir2.mkdir()) {
                System.out.println("Directory stat2 is created!");
                createSubDir(path);
            } else {
                System.out.println("Failed to create directory stat2!");
            }
        }else{
            createSubDir(path);
        }
        if(algorithm == 1){
            path = System.getProperty("user.dir") + File.separator + "stat1" + File.separator + "BaseStatistics";
        }else{
            path = System.getProperty("user.dir") + File.separator + "stat2" + File.separator + "BaseStatistics";
        }
        this.cloudletMeanPopulation = new Welford();
        this.cloudMeanPopulation = new Welford();
        this.globalMeanPopulation = new Welford();
        this.cloudletServiceMeanTime = new Welford();
        this.cloudServiceMeanTime = new Welford();
        this.globalServiceMeanTime = new Welford();
        this.cloudThroughputMean = new Welford();
        this.cloudletThroughputMean = new Welford();
        this.globalThroughputMean = new Welford();
        this.meanPopulationJobClassOneClet = new Welford();
        this.meanPopulationJobClassTwoClet = new Welford();
        this.meanPopulationJobClassOneCloud = new Welford();
        this.meanPopulationJobClassTwoCloud = new Welford();
        this.meanThroughputJobClassOneClet = new Welford();
        this.meanThroughputJobClassTwoClet = new Welford();
        this.meanThroughputJobClassOneCloud = new Welford();
        this.meanThroughputJobClassTwoCloud = new Welford();
        this.meanServiceTimeJobClassOneClet = new Welford();
        this.meanServiceTimeJobClassTwoClet = new Welford();
        this.meanServiceTimeJobClassOneCloud = new Welford();
        this.meanServiceTimeJobClassTwoCloud = new Welford();
        this.meanServiceTimeJobClassTwoPreempted = new Welford();
        try{
            if(hyperexpo){
                fileWriter = new FileWriter(path + File.separator + "StatisticsHyperexpo.csv");
            }else{
                fileWriter = new FileWriter(path + File.separator + "StatisticsExpo.csv");
            }
            fileWriter.append("curtime;");
            fileWriter.append("CloudMeanPopulation;");
            fileWriter.append("CloudletMeanPopulation;");
            fileWriter.append("GlobalMeanPopulation;");
            fileWriter.append("CloudMeanThroughput;");
            fileWriter.append("CloudletMeanThroughput;");
            fileWriter.append("GlobalMeanThroughput;");
            fileWriter.append("CloudMeanServiceTime;");
            fileWriter.append("CloudletMeanServiceTime;");
            fileWriter.append("GlobalMeanServiceTime;");
            fileWriter.append("MeanPopJobClassOneCloudlet;");
            fileWriter.append("MeanPopJobClassTwoCloudlet;");
            fileWriter.append("MeanPopJobClassOneCloud;");
            fileWriter.append("MeanPopJobClassTwoCloud;");
            fileWriter.append("MeanThrJobClassOneClet;");
            fileWriter.append("MeanThrJobClassTwoClet;");
            fileWriter.append("MeanThrJobClassOneCloud;");
            fileWriter.append("MeanThrJobClassTwoCloud;");
            fileWriter.append("MeanSTimeJobClassOneClet;");
            fileWriter.append("MeanSTimeJobClassTwoClet;");
            fileWriter.append("MeanSTimeJobClassOneCloud;");
            fileWriter.append("MeanSTimeJobClassTwoCloud;");
            fileWriter.append("MeanSTimeJobClassTwoPreempted;");
            fileWriter.append("\n");
        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void createSubDir(String path){
        File baseDir = new File(path + File.separator + "BaseStatistics");
        if(baseDir.mkdir()){
            System.out.println("Directory BaseStatistics in " + path +"created");
        }else{
            System.out.println("Failed to create directory BaseStatistics!");
        }
    }

    private void writeOnFile(Clock clock) {
        StringBuilder sb = new StringBuilder();
        sb.append(clock.currentTime);
        sb.append(";");
        sb.append(cloudMeanPopulation.getCurrent_mean());
        sb.append(";");
        sb.append(cloudletMeanPopulation.getCurrent_mean());
        sb.append(";");
        sb.append(globalMeanPopulation.getCurrent_mean());
        sb.append(";");
        sb.append(cloudThroughputMean.getCurrent_mean());
        sb.append(";");
        sb.append(cloudletThroughputMean.getCurrent_mean());
        sb.append(";");
        sb.append(globalThroughputMean.getCurrent_mean());
        sb.append(";");
        sb.append(cloudServiceMeanTime.getCurrent_mean());
        sb.append(";");
        sb.append(cloudletServiceMeanTime.getCurrent_mean());
        sb.append(";");
        sb.append(globalServiceMeanTime.getCurrent_mean());
        sb.append(";");
        sb.append(meanPopulationJobClassOneClet.getCurrent_mean());
        sb.append(";");
        sb.append(meanPopulationJobClassTwoClet.getCurrent_mean());
        sb.append(";");
        sb.append(meanPopulationJobClassOneCloud.getCurrent_mean());
        sb.append(";");
        sb.append(meanPopulationJobClassTwoCloud.getCurrent_mean());
        sb.append(";");
        sb.append(meanThroughputJobClassOneClet.getCurrent_mean());
        sb.append(";");
        sb.append(meanThroughputJobClassTwoClet.getCurrent_mean());
        sb.append(";");
        sb.append(meanThroughputJobClassOneCloud.getCurrent_mean());
        sb.append(";");
        sb.append(meanThroughputJobClassTwoCloud.getCurrent_mean());
        sb.append(";");
        sb.append(meanServiceTimeJobClassOneClet.getCurrent_mean());
        sb.append(";");
        sb.append(meanServiceTimeJobClassTwoClet.getCurrent_mean());
        sb.append(";");
        sb.append(meanServiceTimeJobClassOneCloud.getCurrent_mean());
        sb.append(";");
        sb.append(meanServiceTimeJobClassTwoCloud.getCurrent_mean());
        sb.append(";");
        sb.append(meanServiceTimeJobClassTwoPreempted.getCurrent_mean());
        sb.append(";");
        sb.append("\n");
        try {
            fileWriter.append(sb.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatistic(Clock clock, Cloudlet cloudlet, Cloud cloud, CompletedRequest request) {
        cloudMeanPopulation.updateWelfordMean(cloud.getAllJobs());
        cloudletMeanPopulation.updateWelfordMean(cloudlet.getAllJobs());
        globalMeanPopulation.updateWelfordMean(cloud.getAllJobs()+cloudlet.getAllJobs());
        //requests handled by cloud
        cloudThroughputMean.updateWelfordMean(cloud.completedRequests/clock.currentTime);
        //requests handled by cloudlet
        cloudletThroughputMean.updateWelfordMean(cloudlet.completedRequests/clock.currentTime);
        //requests handled globally
        globalThroughputMean.updateWelfordMean((cloud.completedRequests+cloudlet.completedRequests)/clock.currentTime);
        if(request != null) {
            if (request.getServer() instanceof Cloud) {
                cloudServiceMeanTime.updateWelfordMean(request.getJob().getServiceTime());
                if (request.getJobType() == 1) {
                    meanServiceTimeJobClassOneCloud.updateWelfordMean(request.getJob().getServiceTime());
                } else {
                    meanServiceTimeJobClassTwoCloud.updateWelfordMean(request.getJob().getServiceTime());
                    // if the request is preempted then update the mean value of the service time in the cloud
                    if(request.isPreempted()){
                        meanServiceTimeJobClassTwoPreempted.updateWelfordMean(request.getJob().getServiceTime());
                    }
                }
            } else {
                cloudletServiceMeanTime.updateWelfordMean(request.getJob().getServiceTime());
                if (request.getJobType() == 1) {
                    meanServiceTimeJobClassOneClet.updateWelfordMean(request.getJob().getServiceTime());
                } else {
                    meanServiceTimeJobClassTwoClet.updateWelfordMean(request.getJob().getServiceTime());
                }
            }
            globalServiceMeanTime.updateWelfordMean(request.getJob().getServiceTime());
        }
        // statistics for each job class
        // mean population
        meanPopulationJobClassOneClet.updateWelfordMean(cloudlet.nJobsClass1);
        meanPopulationJobClassTwoClet.updateWelfordMean(cloudlet.nJobsClass2);
        meanPopulationJobClassOneCloud.updateWelfordMean(cloud.nJobsClass1);
        meanPopulationJobClassTwoCloud.updateWelfordMean(cloud.nJobsClass2);
        // mean throughput
        meanThroughputJobClassOneClet.updateWelfordMean(cloudlet.completedReqJobsClass1/clock.currentTime);
        meanThroughputJobClassTwoClet.updateWelfordMean(cloudlet.completedReqJobsClass2/clock.currentTime);
        meanThroughputJobClassOneCloud.updateWelfordMean(cloud.completedReqJobsClass1/clock.currentTime);
        meanThroughputJobClassTwoCloud.updateWelfordMean(cloud.completedReqJobsClass2/clock.currentTime);
        writeOnFile(clock);
    }

    public Welford getCloudMeanPopulation(){
        return cloudMeanPopulation;
    }

    public Welford getCloudletMeanPopulation(){
        return cloudletMeanPopulation;
    }

    public Welford getGlobalMeanPopulation(){
        return globalMeanPopulation;
    }

    public Welford getCloudServiceMeanTime(){
        return cloudServiceMeanTime;
    }

    public Welford getCloudletServiceMeanTime(){
        return cloudletServiceMeanTime;
    }

    public Welford getGlobalServiceMeanTime(){
        return globalServiceMeanTime;
    }

    public Welford getCloudThroughputMean(){
        return cloudThroughputMean;
    }

    public Welford getCloudletThroughputMean(){
        return cloudletThroughputMean;
    }

    public Welford getGlobalThroughputMean(){
        return globalThroughputMean;
    }

    public Welford getMeanPopulationJobClassOneClet(){
        return meanPopulationJobClassOneClet;
    }

    public Welford getMeanPopulationJobClassTwoClet(){
        return meanPopulationJobClassTwoClet;
    }

    public Welford getMeanPopulationJobClassOneCloud(){
        return meanPopulationJobClassOneCloud;
    }

    public Welford getMeanPopulationJobClassTwoCloud(){
        return meanPopulationJobClassTwoCloud;
    }

    public Welford getMeanThroughputJobClassOneClet(){
        return meanThroughputJobClassOneClet;
    }

    public Welford getMeanThroughputJobClassTwoClet(){
        return meanThroughputJobClassTwoClet;
    }

    public Welford getMeanThroughputJobClassOneCloud(){
        return meanThroughputJobClassOneCloud;
    }

    public Welford getMeanThroughputJobClassTwoCloud(){
        return meanThroughputJobClassTwoCloud;
    }

    public Welford getMeanServiceTimeJobClassOneClet(){
        return meanServiceTimeJobClassOneClet;
    }

    public Welford getMeanServiceTimeJobClassTwoClet(){
        return meanServiceTimeJobClassTwoClet;
    }

    public Welford getMeanServiceTimeJobClassOneCloud(){
        return meanServiceTimeJobClassOneCloud;
    }

    public Welford getMeanServiceTimeJobClassTwoCloud(){
        return meanServiceTimeJobClassTwoCloud;
    }

    public Welford getMeanServiceTimeJobClassTwoPreempted() {
        return meanServiceTimeJobClassTwoPreempted;
    }
}
