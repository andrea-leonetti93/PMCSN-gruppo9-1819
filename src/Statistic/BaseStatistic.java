package Statistic;

import Request.CompletedRequest;
import Server.Cloud;
import Server.Cloudlet;
import Util.Clock;
import Util.Configuration;

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
    private FileWriter fileWriter;
    private static boolean hyperexpo = Configuration.HYPEREXPO;
    private static BaseStatistic baseStatistic = null;

    public static BaseStatistic getInstance(){
        if(baseStatistic == null){
            baseStatistic = new BaseStatistic();
        }
        return baseStatistic;
    }

    private BaseStatistic() {
        this.cloudletMeanPopulation = new Welford();
        this.cloudMeanPopulation = new Welford();
        this.globalMeanPopulation = new Welford();
        this.cloudletServiceMeanTime = new Welford();
        this.cloudServiceMeanTime = new Welford();
        this.globalServiceMeanTime = new Welford();
        this.cloudThroughputMean = new Welford();
        this.cloudletThroughputMean = new Welford();
        this.globalThroughputMean = new Welford();
        try{
            if(hyperexpo){
                fileWriter = new FileWriter("C:\\Users\\andre\\IdeaProjects\\PMCSN-gruppo9-1819\\src\\StatisticsHyperexpo.csv");
            }else{
                fileWriter = new FileWriter("C:\\Users\\andre\\IdeaProjects\\PMCSN-gruppo9-1819\\src\\StatisticsExpo.csv");
            }
            fileWriter.append("CloudMeanPopulation;");
            fileWriter.append("CloudletMeanPopulation;");
            fileWriter.append("GlobalMeanPopulation;");
            fileWriter.append("CloudMeanThroughput;");
            fileWriter.append("CloudletMeanThroughput;");
            fileWriter.append("GlobalMeanThroughput;");
            fileWriter.append("CloudMeanServiceTime;");
            fileWriter.append("CloudletMeanServiceTime;");
            fileWriter.append("GlobalMeanServiceTime;");
            fileWriter.append("\n");
        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
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
        sb.append("\n");
        try {
            fileWriter.append(sb.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatistic(Clock clock, Cloudlet cloudlet, Cloud cloud) {
        cloudMeanPopulation.updateWelfordMean(cloud.getAllJobs());
        cloudletMeanPopulation.updateWelfordMean(cloudlet.getAllJobs());
        globalMeanPopulation.updateWelfordMean(cloud.getAllJobs()+cloudlet.getAllJobs());
        //requests handled by cloud
        cloudThroughputMean.updateWelfordMean(cloud.completedRequests/clock.currentTime);
        //requests handled by cloudlet
        cloudletThroughputMean.updateWelfordMean(cloudlet.completedRequests/clock.currentTime);
        //requests handled globally
        globalThroughputMean.updateWelfordMean((cloud.completedRequests+cloudlet.completedRequests)/clock.currentTime);
        writeOnFile(clock);
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
        if(request.getServer() instanceof Cloud){
            cloudServiceMeanTime.updateWelfordMean(request.getJob().getServiceTime());
        }else{
            cloudletServiceMeanTime.updateWelfordMean(request.getJob().getServiceTime());
        }
        globalServiceMeanTime.updateWelfordMean(request.getJob().getServiceTime());
        writeOnFile(clock);
    }

    public Welford getCloudMeanPopulation() {
        return cloudMeanPopulation;
    }

    public Welford getCloudletMeanPopulation() {
        return cloudletMeanPopulation;
    }

    public Welford getGlobalMeanPopulation() {
        return globalMeanPopulation;
    }

    public Welford getCloudServiceMeanTime() {
        return cloudServiceMeanTime;
    }

    public Welford getCloudletServiceMeanTime() {
        return cloudletServiceMeanTime;
    }

    public Welford getGlobalServiceMeanTime() {
        return globalServiceMeanTime;
    }

    public Welford getCloudThroughputMean() {
        return cloudThroughputMean;
    }

    public Welford getCloudletThroughputMean() {
        return cloudletThroughputMean;
    }

    public Welford getGlobalThroughputMean() {
        return globalThroughputMean;
    }

}
