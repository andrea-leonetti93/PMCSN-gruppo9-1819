package Util;

import Request.*;
import Server.Cloud;
import Server.Cloudlet;
import Statistic.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Util.Configuration.S;

public class Controller {

    private static Controller controller = null;
    private RequestQueue requestQueue = RequestQueue.getInstance();
    private Cloudlet cloudlet = Cloudlet.getInstance();
    private Cloud cloud = Cloud.getInstance();
    private Clock clock = Clock.getInstance();
    private List<CompletedRequest> completedRequests;
    private ArrayList<Request> type2JobRequestInCloudlet;
    private List<Integer> typeTwoJobToMove;
    private int cloudCompletedRequests;
    private int cloudletCompletedRequests;
    private int N = Configuration.N;
    private static boolean batch_means = Configuration.BATCH_MEANS;
    private static boolean hyperexpo = Configuration.HYPEREXPO;
    private FileWriter fileWriter;
    private int nJobTotCloudClass1 = 0;
    private int nJobTotCloudClass2 = 0;
    private int nJobTotCletClass1 = 0;
    private int nJobTotCletClass2 = 0;
    private double serviceTimeC1Clet = 0;
    private double serviceTimeC2Clet = 0;
    private double serviceTimeC1Cloud = 0;
    private double serviceTimeC2Cloud = 0;
    private double arrivalTimeC1Clet = 0;
    private double arrivalTimeC2Clet = 0;
    private double arrivalTimeC1Cloud = 0;
    private double arrivalTimeC2Cloud = 0;
    private double completedTimeC1Clet = 0;
    private double completedTimeC2Clet = 0;
    private double completedTimeC1Cloud = 0;
    private double completedTimeC2Cloud = 0;
    private static Statistic s;

    private Controller(){
        if(batch_means){
            s = AdvancedStatistic.getInstance();
        }else{
            s = BaseStatistic.getInstance();
        }
        completedRequests = new ArrayList<CompletedRequest>();
        try{
            if(hyperexpo){
                fileWriter = new FileWriter("C:\\Users\\andre\\IdeaProjects\\PMCSN-gruppo9-1819\\src\\JobFlowStatsHyperexpo.csv");
            }else{
                fileWriter = new FileWriter("C:\\Users\\andre\\IdeaProjects\\PMCSN-gruppo9-1819\\src\\JobFlowStatsExpo.csv");
            }
            fileWriter.append("curtime;");
            fileWriter.append("j1cloudlet;");
            fileWriter.append("j2cloudlet;");
            fileWriter.append("j1cloud;");
            fileWriter.append("j2cloud;");
            fileWriter.append("curtime;");
            fileWriter.append("\n");
        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static Controller getInstance()
    {
        if (controller == null)
            controller = new Controller();

        return controller;
    }

    public void getRequest(){
        Request re = requestQueue.poll();
        if(re !=null){
            //TODO aggiornare qui clock globale
            clock.currentTime = re.getRequestTime();
            //System.out.println("To handle:" + re);
            System.out.println("Current time:" + clock.currentTime);
            if(re instanceof ArrivalRequest){
                //check cloudlet space
                //if it's lower than N, send the request to the cloudlet
                int totCletJobs = cloudlet.nJobsClass1+cloudlet.nJobsClass2;
                if(totCletJobs < N){
                    //cloudlet.incrNServerUsed();
                    cloudlet.handleRequest((ArrivalRequest) re);
                    //gestire l'add del server che gestisce la richiesta alla richeista stessa
                }
                //if the cloudlet is full, send the request to the cloud
                else{
                    cloud.handleRequest((ArrivalRequest) re);
                }
            }
            else{
                completedRequests.add((CompletedRequest) re);
                if(((CompletedRequest) re).getServer() instanceof Cloudlet){
                    //cloudlet.decrNServerUsed();
                    if(re.getJobType()==1){
                        cloudlet.nJobsClass1-=1;
                    }else{
                        cloudlet.nJobsClass2-=1;
                    }
                }else{
                    if(re.getJobType()==1){
                        cloud.nJobsClass1-=1;
                    }else{
                        cloud.nJobsClass2-=1;
                    }
                }
            }
            writeOnFile(clock, cloudlet, cloud);
            if(re instanceof ArrivalRequest){
                s.updateStatistic(clock, cloudlet, cloud);
            }else{
                s.updateStatistic(clock, cloudlet, cloud, (CompletedRequest) re);
            }
        }
        //clock.incrCurrentTime();
    }

    private void writeOnFile(Clock clock, Cloudlet cloudlet, Cloud cloud) {
        StringBuilder sb = new StringBuilder();
        sb.append(clock.currentTime);
        sb.append(";");
        sb.append(cloudlet.nJobsClass1);
        sb.append(";");
        sb.append(cloudlet.nJobsClass2);
        sb.append(";");
        sb.append(cloud.nJobsClass1);
        sb.append(";");
        sb.append(cloud.nJobsClass2);
        sb.append("\n");

        try {
            fileWriter.append(sb.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int numbCompletedRequest(){
        return completedRequests.size();
    }

    public void numbJobEachServer(){
        if(batch_means){
            AdvancedStatistic as = (AdvancedStatistic) s;
            //population
            System.out.println("\nNumb mean job Cloud: " + as.getCloudMeanPopulation().getwMean().getCurrent_mean() +
                    ", dev: " + as.getCloudMeanPopulation().getwMean().getStDeviation());
            System.out.println("\nNumb mean job Cloudlet: " + as.getCloudletMeanPopulation().getwMean().getCurrent_mean() +
                    ", dev: " + as.getCloudletMeanPopulation().getwMean().getStDeviation());
            System.out.println("\nNumb mean job Global: " + as.getGlobalMeanPopulation().getwMean().getCurrent_mean() +
                    ", dev: " + as.getGlobalMeanPopulation().getwMean().getStDeviation());
            //service time
            System.out.println("\nMean service time Cloud: " + as.getCloudServiceMeanTime().getwMean().getCurrent_mean() +
                    ", dev: " + as.getCloudServiceMeanTime().getwMean().getStDeviation());
            System.out.println("\nMean service time Cloudlet: " + as.getCloudletServiceMeanTime().getwMean().getCurrent_mean() +
                    ", dev: " + as.getCloudletServiceMeanTime().getwMean().getStDeviation());
            System.out.println("\nMean service time Global: " + as.getGlobalServiceMeanTime().getwMean().getCurrent_mean() +
                    ", dev: " + as.getGlobalServiceMeanTime().getwMean().getStDeviation());
            //throughput
            System.out.println("\nMean throughput Cloud: " + as.getCloudThroughputMean().getwMean().getCurrent_mean() +
                    ", dev: " + as.getCloudThroughputMean().getwMean().getStDeviation());
            System.out.println("\nMean throughput Cloudlet: " + as.getCloudletThroughputMean().getwMean().getCurrent_mean() +
                    ", dev: " + as.getCloudletThroughputMean().getwMean().getStDeviation());
            System.out.println("\nMean throughput Global: " + as.getGlobalThroughputMean().getwMean().getCurrent_mean() +
                    ", dev: " + as.getGlobalThroughputMean().getwMean().getStDeviation());
        }else{
            BaseStatistic bs = (BaseStatistic) s;
            //population
            System.out.println("\nNumb mean job Cloud: " + bs.getCloudMeanPopulation().getCurrent_mean() +
                    ", dev: " + bs.getCloudMeanPopulation().getStDeviation());
            System.out.println("\nNumb mean job Cloudlet: " + bs.getCloudletMeanPopulation().getCurrent_mean() +
                    ", dev: " + bs.getCloudletMeanPopulation().getStDeviation());
            System.out.println("\nNumb mean job Global: " + bs.getGlobalMeanPopulation().getCurrent_mean() +
                    ", dev: " + bs.getGlobalMeanPopulation().getStDeviation());
            //service time
            System.out.println("\nMean service time Cloud: " + bs.getCloudServiceMeanTime().getCurrent_mean() +
                    ", dev: " + bs.getCloudServiceMeanTime().getStDeviation());
            System.out.println("\nMean service time Cloudlet: " + bs.getCloudletServiceMeanTime().getCurrent_mean() +
                    ", dev: " + bs.getCloudletServiceMeanTime().getStDeviation());
            System.out.println("\nMean service time Global: " + bs.getGlobalServiceMeanTime().getCurrent_mean() +
                    ", dev: " + bs.getGlobalServiceMeanTime().getStDeviation());
            //throughput
            System.out.println("\nMean throughput Cloud: " + bs.getCloudThroughputMean().getCurrent_mean() +
                    ", dev: " + bs.getCloudThroughputMean().getStDeviation());
            System.out.println("\nMean throughput Cloudlet: " + bs.getCloudletThroughputMean().getCurrent_mean() +
                    ", dev: " + bs.getCloudletThroughputMean().getStDeviation());
            System.out.println("\nMean throughput Global: " + bs.getGlobalThroughputMean().getCurrent_mean() +
                    ", dev: " + bs.getGlobalThroughputMean().getStDeviation());
        }
        /*CompletedRequest cr;
        for (CompletedRequest completedRequest : completedRequests) {
            cr = completedRequest;
            if (cr.getServer() instanceof Cloudlet) {
                if (cr.getJobType() == 1) {
                    nJobTotCletClass1 += 1;
                    serviceTimeC1Clet+=cr.getJob().getServiceTime();
                    arrivalTimeC1Clet+=cr.getJob().getArrivalTime();
                    completedTimeC1Clet+=arrivalTimeC1Clet+serviceTimeC1Clet;
                } else {
                    nJobTotCletClass2 += 1;
                    serviceTimeC2Clet+=cr.getJob().getServiceTime();
                    arrivalTimeC2Clet+=cr.getJob().getArrivalTime();
                    completedTimeC2Clet+=arrivalTimeC2Clet+serviceTimeC2Clet;
                }
            } else {
                if (cr.getJobType() == 1) {
                    nJobTotCloudClass1 += 1;
                    serviceTimeC1Cloud+=cr.getJob().getServiceTime();
                    arrivalTimeC1Cloud+=cr.getJob().getArrivalTime();
                    completedTimeC1Cloud+=arrivalTimeC1Cloud+serviceTimeC1Cloud;
                } else {
                    nJobTotCloudClass2 += 1;
                    serviceTimeC2Cloud+=cr.getJob().getServiceTime();
                    arrivalTimeC2Cloud+=cr.getJob().getArrivalTime();
                    completedTimeC2Cloud+=arrivalTimeC2Cloud+serviceTimeC2Cloud;
                }
            }
        }
        System.out.println("\nNumb job C1 Cloudlet: " + nJobTotCletClass1);
        System.out.println("\nNumb job C2 Cloudlet: " + nJobTotCletClass2);
        System.out.println("\nNumb job C1 Cloud: " + nJobTotCloudClass1);
        System.out.println("\nNumb job C2 Cloud: " + nJobTotCloudClass2);
        //service time
        System.out.println("\nTempo servizio C1 Cloudlet: " + serviceTimeC1Clet);
        System.out.println("\nTempo servizio C2 Cloudlet: " + serviceTimeC2Clet);
        System.out.println("\nTempo servizio C1 Cloud: " + serviceTimeC1Cloud);
        System.out.println("\nTempo servizio C2 Cloud: " + serviceTimeC2Cloud);
        //completed time
        System.out.println("\nTasso completamenti C1 Cloudlet: " + completedTimeC1Clet);
        System.out.println("\nTasso completamenti C2 Cloudlet: " + completedTimeC2Clet);
        System.out.println("\nTasso completamenti C1 Cloud: " + completedTimeC1Cloud);
        System.out.println("\nTasso completamenti C2 Cloud: " + completedTimeC2Cloud);*/
    }

    public void getRequestAlgorithm2() {
        Request re = requestQueue.poll();
        if(re !=null){
            //TODO aggiornare qui clock globale
            clock.currentTime = re.getRequestTime();
            //System.out.println("To handle:" + re);
            System.out.println("Current time:" + clock.currentTime);
            if(!type2JobRequestInCloudlet.isEmpty() && re.getJobType() == 2 && re.getJob().getServiceTime() != 0.0){
                // handle two queues
                int jobId = re.getJob().getId();
                if(checkList(jobId)){
                    // the job is removed in checklist function
                    // removeFromList(jobId);
                    // update the job elaboration time and send it to the cloud
                    cloud.handleRequestFromCloudlet((CompletedRequest) re);

                }else{
                    // it executes the normal handlerequest function if it recived a new class 2 job
                    handleRequest(re);
                }
            }else{
                handleRequest(re);
            }


        }
        //clock.incrCurrentTime();
    }

    private void handleRequest(Request re){
        if(re instanceof ArrivalRequest){
            //check cloudlet space class 1 job
            if(re.getJobType() == 1) {
                //if it's higher than N, send the request to the cloud
                if (cloudlet.nJobsClass1 == N) {
                    cloud.handleRequest((ArrivalRequest) re);
                    //if it's lower than S, send the request to the cloudlet
                } else if (cloudlet.nJobsClass1 + cloudlet.nJobsClass2 < S) {
                    cloudlet.handleRequest((ArrivalRequest) re);
                } else if (cloudlet.nJobsClass2 > 0) {
                    //if there one or more class 2 job in the cloudlet,
                    //accept a class 1 job to the cloudlet and send a class 2 job from cloudlet to cloud
                    int jobId = chooseWhichClassTwoJobRemove();
                    typeTwoJobToMove.add(jobId);
                    cloudlet.nJobsClass2-=1;
                    cloudlet.completedRequests--;
                    //nJobClass1 and completedRequest are updating in the cloudlet class
                    cloudlet.handleRequest((ArrivalRequest) re);
                    //if there's space in the cloudlet
                } else {
                    cloudlet.handleRequest((ArrivalRequest) re);
                }
            //check cloudlet space class 2 job
            }else{
                int totCletJobs = cloudlet.nJobsClass1+cloudlet.nJobsClass2;
                if(totCletJobs < S){
                    //adding the job to the list of class 2 jobs accepted by cloudlet
                    type2JobRequestInCloudlet.add(re);
                    cloudlet.handleRequest((ArrivalRequest) re);
                    //gestire l'add del server che gestisce la richiesta alla richeista stessa
                }
                //if the cloudlet is full, send the request to the cloud
                else{
                    cloud.handleRequest((ArrivalRequest) re);
                }
            }
        }else{
            completedRequests.add((CompletedRequest) re);
            if(((CompletedRequest) re).getServer() instanceof Cloudlet){
                //cloudlet.decrNServerUsed();
                if(re.getJobType()==1){
                    cloudlet.nJobsClass1-=1;
                }else{
                    cloudlet.nJobsClass2-=1;
                }
            }else{
                if(re.getJobType()==1){
                    cloud.nJobsClass1-=1;
                }else{
                    cloud.nJobsClass2-=1;
                }
            }
        }
        writeOnFile(clock, cloudlet, cloud);
        if(re instanceof ArrivalRequest){
            s.updateStatistic(clock, cloudlet, cloud);
        }else{
            s.updateStatistic(clock, cloudlet, cloud, (CompletedRequest) re);
        }
    }

    private int chooseWhichClassTwoJobRemove(){
        Collections.sort(type2JobRequestInCloudlet, new SortByCompletionTime());
        int jobId = type2JobRequestInCloudlet.get(0).getJob().getId();
        type2JobRequestInCloudlet.remove(0);
        return jobId;
    }

    private boolean checkList(int jobTwoId){
        for(int reTypeTwo : typeTwoJobToMove){
            if(jobTwoId == reTypeTwo){
                typeTwoJobToMove.remove(reTypeTwo);
                return true;
            }
        }
        return false;
    }

    //rimuove job 2 dalla lista dopo che è stato re-inviato al cloud
    private void removeFromList(int jobTwoId){
        typeTwoJobToMove.remove(jobTwoId);
    }
}
