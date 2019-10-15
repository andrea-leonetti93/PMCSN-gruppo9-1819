package Statistic;

public class BatchMeans {

    private Double batchMean = 0.0;
    private Double current_batchMean = 0.0;
    private Welford wMean;
    private static final int batchMeanDim = 256;
    private int count = 0;

    public BatchMeans() {
        reset();
    }

    public void computeBatchMeans(double elem){
        wMean.updateWelfordMean(elem);
        current_batchMean+=wMean.getCurrent_mean();
        if(count == batchMeanDim){
            batchMean = current_batchMean/(double)batchMeanDim;
            reset();
        }else{
            count++;
        }
    }

    public void reset(){
        current_batchMean = 0.0;
        wMean = new Welford();
        count = 0;
    }

    public Welford getwMean() {
        return wMean;
    }

    public Double getBatchMean() {
        return batchMean;
    }

}
