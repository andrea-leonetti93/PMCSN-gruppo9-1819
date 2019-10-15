package Statistic;

import Request.CompletedRequest;
import Server.Cloud;
import Server.Cloudlet;
import Util.Clock;

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
