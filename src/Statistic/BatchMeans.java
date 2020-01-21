package Statistic;

import Util.Configuration;

public class BatchMeans {

    private Double batchMean = 0.0;
    private Double current_batchMean = 0.0;
    private Welford wMean;
    private static final double batchMeanDim = Configuration.BATCH_DIM;

    public BatchMeans() {
        wMean = new Welford();
        reset();
    }

    public void computeBatchMeans(double elem){
        wMean.updateWelfordMean(elem);
        current_batchMean += wMean.getCurrent_mean();
    }

    public void reset(){
        current_batchMean = 0.0;
    }

    public Welford getwMean() {
        return wMean;
    }

    public Double getBatchMean() {
        batchMean = current_batchMean / batchMeanDim;
        reset();
        return batchMean;
    }

    public Double getCurrent_batchMean() {
        return current_batchMean;
    }
}
