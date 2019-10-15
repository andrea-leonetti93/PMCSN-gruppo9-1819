package Statistic;

public class BatchMeans {

    private Double batchMean;
    private Welford wMean;
    private static final int batchMeanDim = 256;
    private int count = 0;

    public BatchMeans() {
        reset();
    }

    public void computeBatchMeans(double elem){
        wMean.updateWelfordMean(elem);
        count++;
        if(count % batchMeanDim == 0){
            batchMean = wMean.getCurrent_mean()/(double)batchMeanDim;
            //write del valore batchmean on file
            reset();
        }
    }

    public void reset(){
        batchMean = 0.0;
        wMean = new Welford();
        count = 0;
    }

}
