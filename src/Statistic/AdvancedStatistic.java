package Statistic;

import Request.CompletedRequest;
import Server.Cloud;
import Server.Cloudlet;
import Util.Clock;
import Util.Configuration;

import java.io.FileWriter;
import java.io.IOException;

public class AdvancedStatistic extends Statistic {

    private static AdvancedStatistic advancedStatistic = null;
    private BatchMeans cloudMeanPopulation;
    private BatchMeans cloudletMeanPopulation;
    private BatchMeans globalMeanPopulation;
    private BatchMeans cloudServiceMeanTime;
    private BatchMeans cloudletServiceMeanTime;
    private BatchMeans globalServiceMeanTime;
    private BatchMeans cloudThroughputMean;
    private BatchMeans cloudletThroughputMean;
    private BatchMeans globalThroughputMean;
    private int counter = 0;
    private FileWriter fileWriter;
    private static boolean hyperexpo = Configuration.HYPEREXPO;

    public static AdvancedStatistic getInstance(){
        if(advancedStatistic == null){
            advancedStatistic = new AdvancedStatistic();
        }
        return advancedStatistic;
    }

    private AdvancedStatistic() {
        this.cloudMeanPopulation = new BatchMeans();
        this.cloudletMeanPopulation = new BatchMeans();
        this.globalMeanPopulation = new BatchMeans();
        this.cloudServiceMeanTime = new BatchMeans();
        this.cloudletServiceMeanTime = new BatchMeans();
        this.globalServiceMeanTime = new BatchMeans();
        this.cloudThroughputMean = new BatchMeans();
        this.cloudletThroughputMean = new BatchMeans();
        this.globalThroughputMean = new BatchMeans();
        try{
            if(hyperexpo){
                fileWriter = new FileWriter("C:\\Users\\andre\\IdeaProjects\\PMCSN-gruppo9-1819\\src\\Batch_MeansHyperexpo.csv");
            }else{
                fileWriter = new FileWriter("C:\\Users\\andre\\IdeaProjects\\PMCSN-gruppo9-1819\\src\\Batch_MeansExpo.csv");
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
            fileWriter.append("\n");
        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void writeOnFile(Clock clock) {
        StringBuilder sb = new StringBuilder();
        sb.append(clock.currentTime);
        sb.append(";");
        sb.append(cloudMeanPopulation.getBatchMean());
        sb.append(";");
        sb.append(cloudletMeanPopulation.getBatchMean());
        sb.append(";");
        sb.append(globalMeanPopulation.getBatchMean());
        sb.append(";");
        sb.append(cloudThroughputMean.getBatchMean());
        sb.append(";");
        sb.append(cloudletThroughputMean.getBatchMean());
        sb.append(";");
        sb.append(globalThroughputMean.getBatchMean());
        sb.append(";");
        sb.append(cloudServiceMeanTime.getBatchMean());
        sb.append(";");
        sb.append(cloudletServiceMeanTime.getBatchMean());
        sb.append(";");
        sb.append(globalServiceMeanTime.getBatchMean());
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
        cloudMeanPopulation.computeBatchMeans(cloud.getAllJobs());
        cloudletMeanPopulation.computeBatchMeans(cloudlet.getAllJobs());
        globalMeanPopulation.computeBatchMeans(cloud.getAllJobs()+cloudlet.getAllJobs());
        //requests handled by cloud
        cloudThroughputMean.computeBatchMeans(cloud.completedRequests/clock.currentTime);
        //requests handled by cloudlet
        cloudletThroughputMean.computeBatchMeans(cloudlet.completedRequests/clock.currentTime);
        //requests handled globally
        globalThroughputMean.computeBatchMeans((cloud.completedRequests+cloudlet.completedRequests)/clock.currentTime);
        cloudServiceMeanTime.setCount();
        cloudletServiceMeanTime.setCount();
        globalServiceMeanTime.setCount();
        if(counter == 256){
            //TODO write del valore batchmean on file
            counter = 0;
            writeOnFile(clock);
        }else{
            counter++;
        }

    }

    @Override
    public void updateStatistic(Clock clock, Cloudlet cloudlet, Cloud cloud, CompletedRequest request) {
        cloudMeanPopulation.computeBatchMeans(cloud.getAllJobs());
        cloudletMeanPopulation.computeBatchMeans(cloudlet.getAllJobs());
        globalMeanPopulation.computeBatchMeans(cloud.getAllJobs()+cloudlet.getAllJobs());
        //requests handled by cloud
        cloudThroughputMean.computeBatchMeans(cloud.completedRequests/clock.currentTime);
        //requests handled by cloudlet
        cloudletThroughputMean.computeBatchMeans(cloudlet.completedRequests/clock.currentTime);
        //requests handled globally
        globalThroughputMean.computeBatchMeans((cloud.completedRequests+cloudlet.completedRequests)/clock.currentTime);
        if(request.getServer() instanceof Cloud){
            cloudServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());
            cloudletServiceMeanTime.setCount();
        }else{
            cloudletServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());
            cloudServiceMeanTime.setCount();
        }
        globalServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());

        if(counter == 256){
            //TODO write del valore batchmean on file
            counter = 0;
            writeOnFile(clock);
        }else{
            counter++;
        }
    }

    public BatchMeans getCloudMeanPopulation() {
        return cloudMeanPopulation;
    }

    public BatchMeans getCloudletMeanPopulation() {
        return cloudletMeanPopulation;
    }

    public BatchMeans getGlobalMeanPopulation() {
        return globalMeanPopulation;
    }

    public BatchMeans getCloudServiceMeanTime() {
        return cloudServiceMeanTime;
    }

    public BatchMeans getCloudletServiceMeanTime() {
        return cloudletServiceMeanTime;
    }

    public BatchMeans getGlobalServiceMeanTime() {
        return globalServiceMeanTime;
    }

    public BatchMeans getCloudThroughputMean() {
        return cloudThroughputMean;
    }

    public BatchMeans getCloudletThroughputMean() {
        return cloudletThroughputMean;
    }

    public BatchMeans getGlobalThroughputMean() {
        return globalThroughputMean;
    }
}
