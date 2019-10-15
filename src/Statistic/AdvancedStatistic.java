package Statistic;

import Request.CompletedRequest;
import Server.Cloud;
import Server.Cloudlet;
import Util.Clock;

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
        }else{
            cloudletServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());
        }
        globalServiceMeanTime.computeBatchMeans(request.getJob().getServiceTime());
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
