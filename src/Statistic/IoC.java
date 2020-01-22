package Statistic;

import Distribution.Rvms;
import Util.Configuration;
import java.util.ArrayList;

public class IoC {

    private static IoC instance = null;
    private static double LOC = 0.95;
    private Rvms rvms = new Rvms();
    private static int algorithm = Configuration.ALGORITHM;
    // global statistics
    private ArrayList<Double> iocCloudMeanPopulation = new ArrayList<>();
    private ArrayList<Double> iocCloudletMeanPopulation = new ArrayList<>();
    private ArrayList<Double> iocGlobalMeanPopulation = new ArrayList<>();
    private ArrayList<Double> iocCloudThroughputMean = new ArrayList<>();
    private ArrayList<Double> iocCloudletThroughputMean = new ArrayList<>();
    private ArrayList<Double> iocGlobalThroughputMean = new ArrayList<>();
    private ArrayList<Double> iocCloudServiceMeanTime = new ArrayList<>();
    private ArrayList<Double> iocCloudletServiceMeanTime = new ArrayList<>();
    private ArrayList<Double> iocGlobalServiceMeanTime = new ArrayList<>();
    // statistics for each job class
    private ArrayList<Double> iocMeanPopulationJobClassOneClet = new ArrayList<>();
    private ArrayList<Double> iocMeanPopulationJobClassTwoClet = new ArrayList<>();
    private ArrayList<Double> iocMeanPopulationJobClassOneCloud = new ArrayList<>();
    private ArrayList<Double> iocMeanPopulationJobClassTwoCloud = new ArrayList<>();
    private ArrayList<Double> iocMeanThroughputJobClassOneClet = new ArrayList<>();
    private ArrayList<Double> iocMeanThroughputJobClassTwoClet = new ArrayList<>();
    private ArrayList<Double> iocMeanThroughputJobClassOneCloud = new ArrayList<>();
    private ArrayList<Double> iocMeanThroughputJobClassTwoCloud = new ArrayList<>();
    private ArrayList<Double> iocMeanServiceTimeJobClassOneClet = new ArrayList<>();
    private ArrayList<Double> iocMeanServiceTimeJobClassTwoClet = new ArrayList<>();
    private ArrayList<Double> iocMeanServiceTimeJobClassOneCloud = new ArrayList<>();
    private ArrayList<Double> iocMeanServiceTimeJobClassTwoCloud = new ArrayList<>();
    private ArrayList<Double> iocMeanServiceTimeJobClassTwoPreempted = new ArrayList<>();
    private ArrayList<Double> iocMeanInterDeparturesJobClassOne = new ArrayList<>();
    private ArrayList<Double> iocMeanInterDeparturesJobClassTwo = new ArrayList<>();
    private ArrayList<Double> iocMeanServiceTimeJobClassOne = new ArrayList<>();
    private ArrayList<Double> iocMeanServiceTimeJobClassTwo = new ArrayList<>();

    private IoC(){};

    public static IoC getInstance(){
        if(instance==null){
            instance = new IoC();
        }
        return instance;
    }

    public double[] computeIoC(ArrayList<Double> givenNumbers){
        double sampleMean = 0.0;
        double variance = 0.0;
        double standardDeviation;
        double t_student;
        double width = 0.0;
        long n = 0;

        for(Double actualValue: givenNumbers){
            n++;
            double diff = actualValue - sampleMean;
            variance += (diff * diff * (n-1)/n);
            sampleMean += (diff/n);
        }
        standardDeviation = Math.sqrt(variance);

        if(n>1){
            double u = 1.0 - 0.5*(1.0 - LOC);                                            /* interval parameter  */
            t_student = rvms.idfStudent(n - 1, u);                                    /* critical value of t */
            width = t_student * standardDeviation / Math.sqrt(n - 1);                    /* interval half width */
        }
        double[] confidenceInterval = {sampleMean, width};
        return confidenceInterval;
    }

    public void computeIoCForEveryMetric(){
        double[] confIntCMP = computeIoC(iocCloudMeanPopulation);
        double[] confIntCLMP = computeIoC(iocCloudletMeanPopulation);
        double[] confIntGMP = computeIoC(iocGlobalMeanPopulation);
        double[] confIntCTM = computeIoC(iocCloudThroughputMean);
        double[] confIntCLTM = computeIoC(iocCloudletThroughputMean);
        double[] confIntGTM = computeIoC(iocGlobalThroughputMean);
        // service time
        double[] confIntCSM = computeIoC(iocCloudServiceMeanTime);
        double[] confIntCLSM = computeIoC(iocCloudletServiceMeanTime);
        double[] confIntGSM = computeIoC(iocGlobalServiceMeanTime);

        double[] confIntPJ1CL = computeIoC(iocMeanPopulationJobClassOneClet);
        double[] confIntPJ2CL = computeIoC(iocMeanPopulationJobClassTwoClet);
        double[] confIntPJ1C = computeIoC(iocMeanPopulationJobClassOneCloud);
        double[] confIntPJ2C = computeIoC(iocMeanPopulationJobClassTwoCloud);
        double[] confIntTJ1CL = computeIoC(iocMeanThroughputJobClassOneClet);
        double[] confIntTJ2CL = computeIoC(iocMeanThroughputJobClassTwoClet);
        double[] confIntTJ1C = computeIoC(iocMeanThroughputJobClassOneCloud);
        double[] confIntTJ2C = computeIoC(iocMeanThroughputJobClassTwoCloud);
        //mean service time for each class
        double[] confIntSJ1CL = computeIoC(iocMeanServiceTimeJobClassOneClet);
        double[] confIntSJ2CL = computeIoC(iocMeanServiceTimeJobClassTwoClet);
        double[] confIntSJ1C = computeIoC(iocMeanServiceTimeJobClassOneCloud);
        double[] confIntSJ2C = computeIoC(iocMeanServiceTimeJobClassTwoCloud);
        double[] confIntSJ2P = computeIoC(iocMeanServiceTimeJobClassTwoPreempted);

        System.out.println("\n INTERVALLI DI CONFIDENZA ");

        System.out.println("\nNumb mean job Cloud: " + confIntCMP[0] + ", width: " + confIntCMP[1]);
        System.out.println("\nNumb mean job Cloudlet: " + confIntCLMP[0] + ", width: " + confIntCLMP[1]);
        System.out.println("\nNumb mean job Global: " + confIntGMP[0] + ", width: " + confIntGMP[1]);

        System.out.println("\nThroughput mean job Cloud: " + confIntCTM[0] + ", width: " + confIntCTM[1]);
        System.out.println("\nThroughput mean job Cloudlet: " + confIntCLTM[0] + ", width: " + confIntCLTM[1]);
        System.out.println("\nThroughput mean job Global: " + confIntGTM[0] + ", width: " + confIntGTM[1]);

        System.out.println("\nService mean time job Cloud: " + confIntCSM[0] + ", width: " + confIntCSM[1]);
        System.out.println("\nService mean time job Cloudlet: " + confIntCLSM[0] + ", width: " + confIntCLSM[1]);
        System.out.println("\nService mean time job Global: " + confIntGSM[0] + ", width: " + confIntGSM[1]);

        System.out.println("\nNumb mean job class 1 Cloudlet: " + confIntPJ1CL[0] + ", width: " + confIntPJ1CL[1]);
        System.out.println("\nNumb mean job class 2 Cloudlet: " + confIntPJ2CL[0] + ", width: " + confIntPJ2CL[1]);
        System.out.println("\nNumb mean job class 1 Cloud: " + confIntPJ1C[0] + ", width: " + confIntPJ1C[1]);
        System.out.println("\nNumb mean job class 2 Cloud: " + confIntPJ2C[0] + ", width: " + confIntPJ2C[1]);

        System.out.println("\nThroughput mean job class 1 Cloudlet: " + confIntTJ1CL[0] + ", width: " + confIntTJ1CL[1]);
        System.out.println("\nThroughput mean job class 2 Cloudlet: " + confIntTJ2CL[0] + ", width: " + confIntTJ2CL[1]);
        System.out.println("\nThroughput mean job class 1 Cloud: " + confIntTJ1C[0] + ", width: " + confIntTJ1C[1]);
        System.out.println("\nThroughput mean job class 2 Cloud: " + confIntTJ2C[0] + ", width: " + confIntTJ2C[1]);

        System.out.println("\nService mean time job class 1 Cloudlet: " + confIntSJ1CL[0] + ", width: " + confIntSJ1CL[1]);
        System.out.println("\nService mean time job class 2 Cloudlet: " + confIntSJ2CL[0] + ", width: " + confIntSJ2CL[1]);
        System.out.println("\nService mean time job class 1 Cloud: " + confIntSJ1C[0] + ", width: " + confIntSJ1C[1]);
        System.out.println("\nService mean time job class 2 Cloud: " + confIntSJ2C[0] + ", width: " + confIntSJ2C[1]);
        if(algorithm == 2){
            System.out.println("\nService mean time job class 2 Preempted: " + confIntSJ2P[0] + ", width: " + confIntSJ2P[1]);
        }
    }

    public void setIocCloudMeanPopulation(double value) {
        this.iocCloudMeanPopulation.add(value);
    }

    public void setIocCloudletMeanPopulation(double value) {
        this.iocCloudletMeanPopulation.add(value);
    }

    public void setIocGlobalMeanPopulation(double value) {
        this.iocGlobalMeanPopulation.add(value);
    }

    public void setIocCloudThroughputMean(double value) {
        this.iocCloudThroughputMean.add(value);
    }

    public void setIocCloudletThroughputMean(double value) {
        this.iocCloudletThroughputMean.add(value);
    }

    public void setIocGlobalThroughputMean(double value) {
        this.iocGlobalThroughputMean.add(value);
    }

    public void setIocCloudServiceMeanTime(double value) {
        this.iocCloudServiceMeanTime.add(value);
    }

    public void setIocCloudletServiceMeanTime(double value) {
        this.iocCloudletServiceMeanTime.add(value);
    }

    public void setIocGlobalServiceMeanTime(double value) {
        this.iocGlobalServiceMeanTime.add(value);
    }

    public void addIocMeanPopulationJobClassOneClet(double value) {
        this.iocMeanPopulationJobClassOneClet.add(value);
    }

    public void addIocMeanPopulationJobClassTwoClet(double value) {
        this.iocMeanPopulationJobClassTwoClet.add(value);
    }

    public void addIocMeanPopulationJobClassOneCloud(double value) {
        this.iocMeanPopulationJobClassOneCloud.add(value);
    }

    public void addIocMeanPopulationJobClassTwoCloud(double value) {
        this.iocMeanPopulationJobClassTwoCloud.add(value);
    }

    public void addIocMeanThroughputJobClassOneClet(double value) {
        this.iocMeanThroughputJobClassOneClet.add(value);
    }

    public void addIocMeanThroughputJobClassTwoClet(double value) {
        this.iocMeanThroughputJobClassTwoClet.add(value);
    }

    public void addIocMeanThroughputJobClassOneCloud(double value) {
        this.iocMeanThroughputJobClassOneCloud.add(value);
    }

    public void addIocMeanThroughputJobClassTwoCloud(double value) {
        this.iocMeanThroughputJobClassTwoCloud.add(value);
    }

    public void addIocMeanServiceTimeJobClassOneClet(double value) {
        this.iocMeanServiceTimeJobClassOneClet.add(value);
    }

    public void addIocMeanServiceTimeJobClassTwoClet(double value) {
        this.iocMeanServiceTimeJobClassTwoClet.add(value);
    }

    public void addIocMeanServiceTimeJobClassOneCloud(double value) {
        this.iocMeanServiceTimeJobClassOneCloud.add(value);
    }

    public void addIocMeanServiceTimeJobClassTwoCloud(double value) {
        this.iocMeanServiceTimeJobClassTwoCloud.add(value);
    }

    public void addIocMeanServiceTimeJobClassTwoPreempted(double value) {
        this.iocMeanServiceTimeJobClassTwoPreempted.add(value);
    }

    public void addIocMeanInterDeparturesJobClassOne(double value){
        this.iocMeanInterDeparturesJobClassOne.add(value);
    }

    public void addIocMeanInterDeparturesJobClassTwo(double value){
        this.iocMeanInterDeparturesJobClassTwo.add(value);
    }

    public void addIocMeanServiceTimeJobClassOne(double value){
        this.iocMeanServiceTimeJobClassOne.add(value);
    }

    public void addIocMeanServiceTimeJobClassTwo(double value){
        this.iocMeanServiceTimeJobClassTwo.add(value);
    }

    public ArrayList<Double> getIocCloudMeanPopulation(){
        return iocCloudMeanPopulation;
    }

    public ArrayList<Double> getIocCloudletMeanPopulation(){
        return iocCloudletMeanPopulation;
    }

    public ArrayList<Double> getIocGlobalMeanPopulation(){
        return iocGlobalMeanPopulation;
    }

    public ArrayList<Double> getIocCloudThroughputMean(){
        return iocCloudThroughputMean;
    }

    public ArrayList<Double> getIocCloudletThroughputMean(){
        return iocCloudletThroughputMean;
    }

    public ArrayList<Double> getIocGlobalThroughputMean() {
        return iocGlobalThroughputMean;
    }

    public ArrayList<Double> getIocCloudServiceMeanTime() {
        return iocCloudServiceMeanTime;
    }

    public ArrayList<Double> getIocCloudletServiceMeanTime() {
        return iocCloudletServiceMeanTime;
    }

    public ArrayList<Double> getIocGlobalServiceMeanTime() {
        return iocGlobalServiceMeanTime;
    }

    public ArrayList<Double> getIocMeanPopulationJobClassOneClet() {
        return iocMeanPopulationJobClassOneClet;
    }

    public ArrayList<Double> getIocMeanPopulationJobClassTwoClet() {
        return iocMeanPopulationJobClassTwoClet;
    }

    public ArrayList<Double> getIocMeanPopulationJobClassOneCloud() {
        return iocMeanPopulationJobClassOneCloud;
    }

    public ArrayList<Double> getIocMeanPopulationJobClassTwoCloud() {
        return iocMeanPopulationJobClassTwoCloud;
    }

    public ArrayList<Double> getIocMeanThroughputJobClassOneClet() {
        return iocMeanThroughputJobClassOneClet;
    }

    public ArrayList<Double> getIocMeanThroughputJobClassTwoClet(){
        return iocMeanThroughputJobClassTwoClet;
    }

    public ArrayList<Double> getIocMeanThroughputJobClassOneCloud(){
        return iocMeanThroughputJobClassOneCloud;
    }

    public ArrayList<Double> getIocMeanThroughputJobClassTwoCloud(){
        return iocMeanThroughputJobClassTwoCloud;
    }

    public ArrayList<Double> getIocMeanServiceTimeJobClassOneClet(){
        return iocMeanServiceTimeJobClassOneClet;
    }

    public ArrayList<Double> getIocMeanServiceTimeJobClassTwoClet(){
        return iocMeanServiceTimeJobClassTwoClet;
    }

    public ArrayList<Double> getIocMeanServiceTimeJobClassOneCloud(){
        return iocMeanServiceTimeJobClassOneCloud;
    }

    public ArrayList<Double> getIocMeanServiceTimeJobClassTwoCloud(){
        return iocMeanServiceTimeJobClassTwoCloud;
    }

    public ArrayList<Double> getIocMeanServiceTimeJobClassTwoPreempted() {
        return iocMeanServiceTimeJobClassTwoPreempted;
    }

    public ArrayList<Double> getIocMeanInterDeparturesJobClassOne(){
        return iocMeanInterDeparturesJobClassOne;
    }

    public ArrayList<Double> getIocMeanInterDeparturesJobClassTwo(){
        return iocMeanInterDeparturesJobClassTwo;
    }

    public ArrayList<Double> getIocMeanServiceTimeJobClassOne() {
        return iocMeanServiceTimeJobClassOne;
    }

    public ArrayList<Double> getIocMeanServiceTimeJobClassTwo() {
        return iocMeanServiceTimeJobClassTwo;
    }
}

