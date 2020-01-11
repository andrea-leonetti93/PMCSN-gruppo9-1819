package Statistic;

import Request.CompletedRequest;
import Server.Cloud;
import Server.Cloudlet;
import Util.Clock;

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
            updateStatisticCompletedReq(request);
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
            ioC.addIocMeanPopulationJobClassOneClet(meanPopulationJobClassOneClet.getBatchMean());
            ioC.addIocMeanPopulationJobClassTwoClet(meanPopulationJobClassTwoClet.getBatchMean());
            ioC.addIocMeanPopulationJobClassOneCloud(meanPopulationJobClassOneCloud.getBatchMean());
            ioC.addIocMeanPopulationJobClassTwoCloud(meanPopulationJobClassTwoCloud.getBatchMean());
            ioC.addIocMeanThroughputJobClassOneClet(meanThroughputJobClassOneClet.getBatchMean());
            ioC.addIocMeanThroughputJobClassTwoClet(meanThroughputJobClassTwoClet.getBatchMean());
            ioC.addIocMeanThroughputJobClassOneCloud(meanThroughputJobClassOneCloud.getBatchMean());
            ioC.addIocMeanThroughputJobClassTwoCloud(meanThroughputJobClassTwoCloud.getBatchMean());
        }else{
            counter++;
        }
    }

    private void updateStatisticCompletedReq(CompletedRequest request){
        if(request.getServer() instanceof Cloud){
            cloudServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());
            if(cloudServiceMeanTime.getwMean().getN()%256==0){
                ioC.setIocCloudServiceMeanTime(cloudServiceMeanTime.getBatchMeanForIoC());
            }
            if(request.getJobType() == 1){
                meanServiceTimeJobClassOneCloud.computeBatchMeans(request.getJob().getServiceTime());
                if(meanServiceTimeJobClassOneCloud.getwMean().getN()%256==0){
                    ioC.addIocMeanServiceTimeJobClassOneCloud(meanServiceTimeJobClassOneCloud.getBatchMeanForIoC());
                }
            }else{
                meanServiceTimeJobClassTwoCloud.computeBatchMeans(request.getJob().getServiceTime());
                if(meanServiceTimeJobClassTwoCloud.getwMean().getN()%256==0){
                    ioC.addIocMeanServiceTimeJobClassTwoCloud(meanServiceTimeJobClassTwoCloud.getBatchMeanForIoC());
                }
            }
        }else{
            cloudletServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());
            if(cloudletServiceMeanTime.getwMean().getN()%256==0){
                ioC.setIocCloudletServiceMeanTime(cloudletServiceMeanTime.getBatchMeanForIoC());
            }
            if(request.getJobType() == 1){
                meanServiceTimeJobClassOneClet.computeBatchMeans(request.getJob().getServiceTime());
                if(meanServiceTimeJobClassOneClet.getwMean().getN()%256==0){
                    ioC.addIocMeanServiceTimeJobClassOneClet(meanServiceTimeJobClassOneClet.getBatchMeanForIoC());
                }
            }else{
                meanServiceTimeJobClassTwoClet.computeBatchMeans(request.getJob().getServiceTime());
                if(meanServiceTimeJobClassTwoClet.getwMean().getN()%256==0){
                    ioC.addIocMeanServiceTimeJobClassTwoClet(meanServiceTimeJobClassTwoClet.getBatchMeanForIoC());
                }
            }
        }
        globalServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());
        if(globalServiceMeanTime.getwMean().getN()%256==0){
            ioC.setIocGlobalServiceMeanTime(globalServiceMeanTime.getBatchMeanForIoC());
        }
    }

    public BatchMeans getCloudMeanPopulation(){
        return cloudMeanPopulation;
    }

    public BatchMeans getCloudletMeanPopulation(){
        return cloudletMeanPopulation;
    }

    public BatchMeans getGlobalMeanPopulation(){
        return globalMeanPopulation;
    }

    public BatchMeans getCloudServiceMeanTime(){
        return cloudServiceMeanTime;
    }

    public BatchMeans getCloudletServiceMeanTime(){
        return cloudletServiceMeanTime;
    }

    public BatchMeans getGlobalServiceMeanTime(){
        return globalServiceMeanTime;
    }

    public BatchMeans getCloudThroughputMean(){
        return cloudThroughputMean;
    }

    public BatchMeans getCloudletThroughputMean(){
        return cloudletThroughputMean;
    }

    public BatchMeans getGlobalThroughputMean(){
        return globalThroughputMean;
    }

    public BatchMeans getMeanPopulationJobClassOneClet() {
        return meanPopulationJobClassOneClet;
    }

    public BatchMeans getMeanPopulationJobClassTwoClet(){
        return meanPopulationJobClassTwoClet;
    }

    public BatchMeans getMeanPopulationJobClassOneCloud(){
        return meanPopulationJobClassOneCloud;
    }

    public BatchMeans getMeanPopulationJobClassTwoCloud(){
        return meanPopulationJobClassTwoCloud;
    }

    public BatchMeans getMeanThroughputJobClassOneClet() {
        return meanThroughputJobClassOneClet;
    }

    public BatchMeans getMeanThroughputJobClassTwoClet() {
        return meanThroughputJobClassTwoClet;
    }

    public BatchMeans getMeanThroughputJobClassOneCloud() {
        return meanThroughputJobClassOneCloud;
    }

    public BatchMeans getMeanThroughputJobClassTwoCloud() {
        return meanThroughputJobClassTwoCloud;
    }

    public BatchMeans getMeanServiceTimeJobClassOneClet() {
        return meanServiceTimeJobClassOneClet;
    }

    public BatchMeans getMeanServiceTimeJobClassTwoClet() {
        return meanServiceTimeJobClassTwoClet;
    }

    public BatchMeans getMeanServiceTimeJobClassOneCloud() {
        return meanServiceTimeJobClassOneCloud;
    }

    public BatchMeans getMeanServiceTimeJobClassTwoCloud() {
        return meanServiceTimeJobClassTwoCloud;
    }
}
