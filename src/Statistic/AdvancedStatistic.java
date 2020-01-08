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
    private IoC ioC = IoC.getInstance();
    private BatchMeans cloudMeanPopulation;
    private BatchMeans cloudletMeanPopulation;
    private BatchMeans globalMeanPopulation;
    private BatchMeans cloudServiceMeanTime;
    private BatchMeans cloudletServiceMeanTime;
    private BatchMeans globalServiceMeanTime;
    private BatchMeans cloudThroughputMean;
    private BatchMeans cloudletThroughputMean;
    private BatchMeans globalThroughputMean;
    // class job statistics
    private BatchMeans meanPopulationJobClassOneClet;
    private BatchMeans meanPopulationJobClassTwoClet;
    private BatchMeans meanPopulationJobClassOneCloud;
    private BatchMeans meanPopulationJobClassTwoCloud;
    private BatchMeans meanThroughputJobClassOneClet;
    private BatchMeans meanThroughputJobClassTwoClet;
    private BatchMeans meanThroughputJobClassOneCloud;
    private BatchMeans meanThroughputJobClassTwoCloud;
    private BatchMeans meanServiceTimeJobClassOneClet;
    private BatchMeans meanServiceTimeJobClassTwoClet;
    private BatchMeans meanServiceTimeJobClassOneCloud;
    private BatchMeans meanServiceTimeJobClassTwoCloud;
    private int counter = 1;
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
        this.meanPopulationJobClassOneClet = new BatchMeans();
        this.meanPopulationJobClassTwoClet = new BatchMeans();
        this.meanPopulationJobClassOneCloud = new BatchMeans();
        this.meanPopulationJobClassTwoCloud = new BatchMeans();
        this.meanThroughputJobClassOneClet = new BatchMeans();
        this.meanThroughputJobClassTwoClet = new BatchMeans();
        this.meanThroughputJobClassOneCloud = new BatchMeans();
        this.meanThroughputJobClassTwoCloud = new BatchMeans();
        this.meanServiceTimeJobClassOneClet = new BatchMeans();
        this.meanServiceTimeJobClassTwoClet = new BatchMeans();
        this.meanServiceTimeJobClassOneCloud = new BatchMeans();
        this.meanServiceTimeJobClassTwoCloud = new BatchMeans();
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
        sb.append(meanPopulationJobClassOneClet.getBatchMean());
        sb.append(";");
        sb.append(meanPopulationJobClassTwoClet.getBatchMean());
        sb.append(";");
        sb.append(meanPopulationJobClassOneCloud.getBatchMean());
        sb.append(";");
        sb.append(meanPopulationJobClassTwoCloud.getBatchMean());
        sb.append(";");
        sb.append(meanThroughputJobClassOneClet.getBatchMean());
        sb.append(";");
        sb.append(meanThroughputJobClassTwoClet.getBatchMean());
        sb.append(";");
        sb.append(meanThroughputJobClassOneCloud.getBatchMean());
        sb.append(";");
        sb.append(meanThroughputJobClassTwoCloud.getBatchMean());
        sb.append(";");
        sb.append(meanServiceTimeJobClassOneClet.getBatchMean());
        sb.append(";");
        sb.append(meanServiceTimeJobClassTwoClet.getBatchMean());
        sb.append(";");
        sb.append(meanServiceTimeJobClassOneCloud.getBatchMean());
        sb.append(";");
        sb.append(meanServiceTimeJobClassTwoCloud.getBatchMean());
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
        cloudMeanPopulation.computeBatchMeans(cloud.getAllJobs());
        cloudletMeanPopulation.computeBatchMeans(cloudlet.getAllJobs());
        globalMeanPopulation.computeBatchMeans(cloud.getAllJobs()+cloudlet.getAllJobs());
        //requests handled by cloud
        cloudThroughputMean.computeBatchMeans(cloud.completedRequests/clock.currentTime);
        //requests handled by cloudlet
        cloudletThroughputMean.computeBatchMeans(cloudlet.completedRequests/clock.currentTime);
        //requests handled globally
        globalThroughputMean.computeBatchMeans((cloud.completedRequests+cloudlet.completedRequests)/clock.currentTime);
        if(request != null){
            if(request.getServer() instanceof Cloud){
                cloudServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());
                if(request.getJobType() == 1){
                    meanServiceTimeJobClassOneCloud.computeBatchMeans(request.getJob().getServiceTime());
                }else{
                    meanServiceTimeJobClassTwoCloud.computeBatchMeans(request.getJob().getServiceTime());
                }
            }else{
                cloudletServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());
                if(request.getJobType() == 1){
                    meanServiceTimeJobClassOneClet.computeBatchMeans(request.getJob().getServiceTime());
                }else{
                    meanServiceTimeJobClassTwoClet.computeBatchMeans(request.getJob().getServiceTime());
                }
            }
            globalServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());
        }
        // statistics for each job class
        // mean population
        meanPopulationJobClassOneClet.computeBatchMeans(cloudlet.nJobsClass1);
        meanPopulationJobClassTwoClet.computeBatchMeans(cloudlet.nJobsClass2);
        meanPopulationJobClassOneCloud.computeBatchMeans(cloud.nJobsClass1);
        meanPopulationJobClassTwoCloud.computeBatchMeans(cloud.nJobsClass2);
        // mean throughput
        meanThroughputJobClassOneClet.computeBatchMeans(cloudlet.completedReqJobsClass1/clock.currentTime);
        meanThroughputJobClassTwoClet.computeBatchMeans(cloudlet.completedReqJobsClass2/clock.currentTime);
        meanThroughputJobClassOneCloud.computeBatchMeans(cloud.completedReqJobsClass1/clock.currentTime);
        meanThroughputJobClassTwoCloud.computeBatchMeans(cloud.completedReqJobsClass2/clock.currentTime);
        if(counter == 256){
            //TODO write del valore batchmean on file
            counter = 1;
            ioC.setIocCloudMeanPopulation(cloudMeanPopulation.getBatchMean());
            ioC.setIocCloudletMeanPopulation(cloudletMeanPopulation.getBatchMean());
            ioC.setIocGlobalMeanPopulation(globalMeanPopulation.getBatchMean());
            ioC.setIocCloudThroughputMean(cloudThroughputMean.getBatchMean());
            ioC.setIocCloudletThroughputMean(cloudletThroughputMean.getBatchMean());
            ioC.setIocGlobalThroughputMean(globalThroughputMean.getBatchMean());
            ioC.setIocCloudServiceMeanTime(cloudServiceMeanTime.getBatchMean());
            ioC.setIocCloudletServiceMeanTime(cloudletServiceMeanTime.getBatchMean());
            ioC.setIocGlobalServiceMeanTime(globalServiceMeanTime.getBatchMean());
            ioC.addIocMeanPopulationJobClassOneClet(meanPopulationJobClassOneClet.getBatchMean());
            ioC.addIocMeanPopulationJobClassTwoClet(meanPopulationJobClassTwoClet.getBatchMean());
            ioC.addIocMeanPopulationJobClassOneCloud(meanPopulationJobClassOneCloud.getBatchMean());
            ioC.addIocMeanPopulationJobClassTwoCloud(meanPopulationJobClassTwoCloud.getBatchMean());
            ioC.addIocMeanThroughputJobClassOneClet(meanThroughputJobClassOneClet.getBatchMean());
            ioC.addIocMeanThroughputJobClassTwoClet(meanThroughputJobClassTwoClet.getBatchMean());
            ioC.addIocMeanThroughputJobClassOneCloud(meanThroughputJobClassOneCloud.getBatchMean());
            ioC.addIocMeanThroughputJobClassTwoCloud(meanThroughputJobClassTwoCloud.getBatchMean());
            ioC.addIocMeanServiceTimeJobClassOneClet(meanServiceTimeJobClassOneClet.getBatchMean());
            ioC.addIocMeanServiceTimeJobClassTwoClet(meanServiceTimeJobClassTwoClet.getBatchMean());
            ioC.addIocMeanServiceTimeJobClassOneCloud(meanServiceTimeJobClassOneCloud.getBatchMean());
            ioC.addIocMeanServiceTimeJobClassTwoCloud(meanServiceTimeJobClassTwoCloud.getBatchMean());
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
