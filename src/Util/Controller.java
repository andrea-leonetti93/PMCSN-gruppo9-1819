package Util;

import Request.*;
import Server.Cloud;
import Server.Cloudlet;
import Statistic.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Controller {

    private static Controller controller = null;
    private RequestQueue requestQueue = RequestQueue.getInstance();
    private Cloudlet cloudlet = Cloudlet.getInstance();
    private Cloud cloud = Cloud.getInstance();
    private Clock clock = Clock.getInstance();
    private List<CompletedRequest> completedRequests;
    private List<Integer> preemptedRequests;
    public ArrayList<Request> type2JobRequestInCloudlet;
    public List<Integer> typeTwoJobToMove;
    private double numberAllJobClass1 = 0.0;
    private double numberAllJobClass2 = 0.0;
    private static int N = Configuration.N;
    private static int S = Configuration.S;
    private static boolean batch_means = Configuration.BATCH_MEANS;
    private static int algorithm = Configuration.ALGORITHM;
    private static Statistic s;

    private Controller(){
        if(batch_means){
            s = AdvancedStatistic.getInstance();
        }else{
            s = BaseStatistic.getInstance();
        }
        completedRequests = new ArrayList<>();
        preemptedRequests = new ArrayList<>();
        type2JobRequestInCloudlet = new ArrayList<>();
        typeTwoJobToMove = new ArrayList<>();
        /*try{
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
        }*/
    }

    public static Controller getInstance() {
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
            //System.out.println("Current time:" + clock.currentTime);
            if(re instanceof ArrivalRequest){
                //check cloudlet space
                //if it's lower than N, send the request to the cloudlet
                if(re.getJobType() == 1){
                    numberAllJobClass1+=1;
                }else{
                    numberAllJobClass2+=1;
                }
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
            //writeOnFile(clock, cloudlet, cloud);
            if(re instanceof ArrivalRequest){
                s.updateStatistic(clock, cloudlet, cloud, null);
            }else{
                s.updateStatistic(clock, cloudlet, cloud, (CompletedRequest) re);
            }
        }
    }

    public int numbCompletedRequest(){
        return completedRequests.size();
    }

    public int numbPreemptedRequest(){
        return preemptedRequests.size();
    }

    public void printStatistics(){
        if(batch_means){
            AdvancedStatistic as = (AdvancedStatistic) s;
            //population
            System.out.println("\nNumb mean job Cloud: " + as.getCloudMeanPopulation().getwMean().getCurrent_mean() +
                    ", var: " + as.getCloudMeanPopulation().getwMean().getVariance());
            System.out.println("\nNumb mean job Cloudlet: " + as.getCloudletMeanPopulation().getwMean().getCurrent_mean() +
                    ", var: " + as.getCloudletMeanPopulation().getwMean().getVariance());
            System.out.println("\nNumb mean job Global: " + as.getGlobalMeanPopulation().getwMean().getCurrent_mean() +
                    ", var: " + as.getGlobalMeanPopulation().getwMean().getVariance());
            //service time
            System.out.println("\nMean service time Cloud: " + as.getCloudServiceMeanTime().getwMean().getCurrent_mean() +
                    ", var: " + as.getCloudServiceMeanTime().getwMean().getVariance());
            System.out.println("\nMean service time Cloudlet: " + as.getCloudletServiceMeanTime().getwMean().getCurrent_mean() +
                    ", var: " + as.getCloudletServiceMeanTime().getwMean().getVariance());
            System.out.println("\nMean service time Global: " + as.getGlobalServiceMeanTime().getwMean().getCurrent_mean() +
                    ", var: " + as.getGlobalServiceMeanTime().getwMean().getVariance());
            //throughput
            System.out.println("\nMean throughput Cloud: " + as.getCloudThroughputMean().getwMean().getCurrent_mean() +
                    ", var: " + as.getCloudThroughputMean().getwMean().getVariance());
            System.out.println("\nMean throughput Cloudlet: " + as.getCloudletThroughputMean().getwMean().getCurrent_mean() +
                    ", var: " + as.getCloudletThroughputMean().getwMean().getVariance());
            System.out.println("\nMean throughput Global: " + as.getGlobalThroughputMean().getwMean().getCurrent_mean() +
                    ", var: " + as.getGlobalThroughputMean().getwMean().getVariance());
            // statistics on each class job
            // mean population
            System.out.println("\nMean population job C1 Cloudlet: " + as.getMeanPopulationJobClassOneClet().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanPopulationJobClassOneClet().getwMean().getVariance());
            System.out.println("\nMean population job C2 Cloudlet: " + as.getMeanPopulationJobClassTwoClet().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanPopulationJobClassTwoClet().getwMean().getVariance());
            System.out.println("\nMean population job C1 Cloud: " + as.getMeanPopulationJobClassOneCloud().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanPopulationJobClassOneCloud().getwMean().getVariance());
            System.out.println("\nMean population job C2 Cloud: " + as.getMeanPopulationJobClassTwoCloud().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanPopulationJobClassTwoCloud().getwMean().getVariance());
            // mean throughput
            System.out.println("\nMean throughput job C1 Cloudlet: " + as.getMeanThroughputJobClassOneClet().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanThroughputJobClassOneClet().getwMean().getVariance());
            System.out.println("\nMean throughput job C2 Cloudlet: " + as.getMeanThroughputJobClassTwoClet().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanThroughputJobClassTwoClet().getwMean().getVariance());
            System.out.println("\nMean throughput job C1 Cloud: " + as.getMeanThroughputJobClassOneCloud().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanThroughputJobClassOneCloud().getwMean().getVariance());
            System.out.println("\nMean throughput job C2 Cloud: " + as.getMeanThroughputJobClassTwoCloud().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanThroughputJobClassTwoCloud().getwMean().getVariance());
            // mean service time
            System.out.println("\nMean Service time job C1 Cloudlet: " + as.getMeanServiceTimeJobClassOneClet().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanServiceTimeJobClassOneClet().getwMean().getVariance());
            System.out.println("\nMean Service time job C2 Cloudlet: " + as.getMeanServiceTimeJobClassTwoClet().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanServiceTimeJobClassTwoClet().getwMean().getVariance());
            System.out.println("\nMean Service time job C1 Cloud: " + as.getMeanServiceTimeJobClassOneCloud().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanServiceTimeJobClassOneCloud().getwMean().getVariance());
            System.out.println("\nMean Service time job C2 Cloud: " + as.getMeanServiceTimeJobClassTwoCloud().getwMean().getCurrent_mean() +
                    ", var: " + as.getMeanServiceTimeJobClassTwoCloud().getwMean().getVariance());
            if(algorithm == 2){
                System.out.println("\nMean Service time job C2 Preempted: " + as.getMeanServiceTimeJobClassTwoPreempted().getwMean().getCurrent_mean() +
                        ", var: " + as.getMeanServiceTimeJobClassTwoPreempted().getwMean().getVariance());
            }
        }else{
            BaseStatistic bs = (BaseStatistic) s;
            //population
            System.out.println("\nNumb mean job Cloud: " + bs.getCloudMeanPopulation().getCurrent_mean() +
                    ", var: " + bs.getCloudMeanPopulation().getVariance());
            System.out.println("\nNumb mean job Cloudlet: " + bs.getCloudletMeanPopulation().getCurrent_mean() +
                    ", var: " + bs.getCloudletMeanPopulation().getVariance());
            System.out.println("\nNumb mean job Global: " + bs.getGlobalMeanPopulation().getCurrent_mean() +
                    ", var: " + bs.getGlobalMeanPopulation().getVariance());
            //service time
            System.out.println("\nMean service time Cloud: " + bs.getCloudServiceMeanTime().getCurrent_mean() +
                    ", var: " + bs.getCloudServiceMeanTime().getVariance());
            System.out.println("\nMean service time Cloudlet: " + bs.getCloudletServiceMeanTime().getCurrent_mean() +
                    ", var: " + bs.getCloudletServiceMeanTime().getVariance());
            System.out.println("\nMean service time Global: " + bs.getGlobalServiceMeanTime().getCurrent_mean() +
                    ", var: " + bs.getGlobalServiceMeanTime().getVariance());
            //throughput
            System.out.println("\nMean throughput Cloud: " + bs.getCloudThroughputMean().getCurrent_mean() +
                    ", var: " + bs.getCloudThroughputMean().getVariance());
            System.out.println("\nMean throughput Cloudlet: " + bs.getCloudletThroughputMean().getCurrent_mean() +
                    ", var: " + bs.getCloudletThroughputMean().getVariance());
            System.out.println("\nMean throughput Global: " + bs.getGlobalThroughputMean().getCurrent_mean() +
                    ", var: " + bs.getGlobalThroughputMean().getVariance());
            // statistics on each class job
            // mean population
            System.out.println("\nMean population job C1 Cloudlet: " + bs.getMeanPopulationJobClassOneClet().getCurrent_mean() +
                    ", var: " + bs.getMeanPopulationJobClassOneClet().getVariance());
            System.out.println("\nMean population job C2 Cloudlet: " + bs.getMeanPopulationJobClassTwoClet().getCurrent_mean() +
                    ", var: " + bs.getMeanPopulationJobClassTwoClet().getVariance());
            System.out.println("\nMean population job C1 Cloud: " + bs.getMeanPopulationJobClassOneCloud().getCurrent_mean() +
                    ", var: " + bs.getMeanPopulationJobClassOneCloud().getVariance());
            System.out.println("\nMean population job C2 Cloud: " + bs.getMeanPopulationJobClassTwoCloud().getCurrent_mean() +
                    ", var: " + bs.getMeanPopulationJobClassTwoCloud().getVariance());
            // mean throughput
            System.out.println("\nMean throughput job C1 Cloudlet: " + bs.getMeanThroughputJobClassOneClet().getCurrent_mean() +
                    ", var: " + bs.getMeanThroughputJobClassOneClet().getVariance());
            System.out.println("\nMean throughput job C2 Cloudlet: " + bs.getMeanThroughputJobClassTwoClet().getCurrent_mean() +
                    ", var: " + bs.getMeanThroughputJobClassTwoClet().getVariance());
            System.out.println("\nMean throughput job C1 Cloud: " + bs.getMeanThroughputJobClassOneCloud().getCurrent_mean() +
                    ", var: " + bs.getMeanThroughputJobClassOneCloud().getVariance());
            System.out.println("\nMean throughput job C2 Cloud: " + bs.getMeanThroughputJobClassTwoCloud().getCurrent_mean() +
                    ", var: " + bs.getMeanThroughputJobClassTwoCloud().getVariance());
            // mean service time
            System.out.println("\nMean Service time job C1 Cloudlet: " + bs.getMeanServiceTimeJobClassOneClet().getCurrent_mean() +
                    ", var: " + bs.getMeanServiceTimeJobClassOneClet().getVariance());
            System.out.println("\nMean Service time job C2 Cloudlet: " + bs.getMeanServiceTimeJobClassTwoClet().getCurrent_mean() +
                    ", var: " + bs.getMeanServiceTimeJobClassTwoClet().getVariance());
            System.out.println("\nMean Service time job C1 Cloud: " + bs.getMeanServiceTimeJobClassOneCloud().getCurrent_mean() +
                    ", var: " + bs.getMeanServiceTimeJobClassOneCloud().getVariance());
            System.out.println("\nMean Service time job C2 Cloud: " + bs.getMeanServiceTimeJobClassTwoCloud().getCurrent_mean() +
                    ", var: " + bs.getMeanServiceTimeJobClassTwoCloud().getVariance());
            if(algorithm == 2){
                System.out.println("\nMean Service time job C2 Preempted: " + bs.getMeanServiceTimeJobClassTwoPreempted().getCurrent_mean() +
                        ", var: " + bs.getMeanServiceTimeJobClassTwoPreempted().getVariance());
            }
        }
        if(algorithm == 2){
            double probLossC1 = (cloud.completedReqJobsClass1/(cloudlet.completedReqJobsClass1+cloud.completedReqJobsClass1));
            double probPreemption = ((double) numbPreemptedRequest()/(cloudlet.completedReqJobsClass2+cloud.completedReqJobsClass2));
            double probLossC2 = (cloud.completedReqJobsClass2/(cloudlet.completedReqJobsClass2+cloud.completedReqJobsClass2));
            System.out.println("\nProbabilità di blocco job classe 1: " + probLossC1*100.0 + " %");
            System.out.println("\nProbabilità di blocco job classe 2: " + (probLossC2)*100.0 + " %");
            System.out.println("\nNumber of preempted job: " + probPreemption + " %\n");
        }else{
            double probLossC1 = (cloud.completedReqJobsClass1/numberAllJobClass1)*100.0;
            double probLossC2 = (cloud.completedReqJobsClass2/numberAllJobClass2)*100.0;
            System.out.println("\nProbabilità di blocco job classe 1: " + probLossC1 + " %");
            System.out.println("\nProbabilità di blocco job classe 2: " + probLossC2 + " %");
        }
    }

    public void getRequestAlgorithm2() {
        Request re = requestQueue.poll();

        if(re != null){
            //TODO CONTROLLARE COME VIENE AGGIORNATO IL TEMPO IN QUESTO CASO!!!!!!!
            if(clock.currentTime > re.getRequestTime()){
                System.out.println("\ncurrent clock: " + clock.currentTime);
                System.out.println("\nlast request time: " + re.getRequestTime());
                System.out.println("\nrequest: " + re);

                Request r;
                while ((r = requestQueue.poll()) != null) {
                    System.out.println(r);
                }

                System.exit(1);
            }
            clock.currentTime = re.getRequestTime();
            if(re instanceof CompletedRequest && ((CompletedRequest) re).isToDelete()){
                // handle two queues
                int jobId = re.getJob().getId();
                if(checkList(jobId)){
                    // the preempted job is removed from the checklist
                    // update the job elaboration time and send it to the cloud
                    preemptedRequests.add(jobId);
                    //System.out.println("Job ID: " + jobId + "preempted\n");
                }/*else{
                    // it executes the normal handlerequest function if it recived a new class 2 job
                    handleRequest(re);
                }*/
            }else if(re instanceof CompletedRequest && ((CompletedRequest) re).getServer() instanceof Cloudlet){
                removeFromList(re.getJob().getId());
                handleRequest(re);
            }else{
                // it handle all the arrival request and the completed request of the cloud
                handleRequest(re);
            }
        }
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
                    Job job = chooseWhichClassTwoJobRemove();
                    typeTwoJobToMove.add(job.getId());
                    //job.getCompletedRequest().setToDelete(true);
                    //job.setCompletedRequest(null);
                    //reqToDelete.setJob(null);
                    cloudlet.nJobsClass2-=1;
                    //handle jobclassone/two
                    //nJobClass1 and completedRequest are updating in the cloudlet class
                    cloud.handleRequestFromCloudlet(job);
                    cloudlet.handleRequest((ArrivalRequest) re);
                    //if there's space in the cloudlet
                } else {
                    cloudlet.handleRequest((ArrivalRequest) re);
                }
            //check cloudlet space class 2 job
            }else{
                int totCletJobs = cloudlet.nJobsClass1+cloudlet.nJobsClass2;
                //if the cloudlet is full, send the request to the cloud
                if(totCletJobs >= S){
                    cloud.handleRequest((ArrivalRequest) re);
                }else{
                    //adding the job to the list of class 2 jobs accepted by cloudlet
                    type2JobRequestInCloudlet.add(re);
                    cloudlet.handleRequest((ArrivalRequest) re);
                    //gestire l'add del server che gestisce la richiesta alla richeista stessa
                }
            }
        }else if(re instanceof CompletedRequest){
            completedRequests.add((CompletedRequest) re);
            //TODO gestire la richiesta prelazionata che deve dare come instanceof quella del cloud
            if(((CompletedRequest) re).getServer() instanceof Cloudlet){
                if(re.getJobType()==1){
                    cloudlet.nJobsClass1-=1;
                    cloudlet.completedReqJobsClass1+=1;
                }else{
                    //TODO sistemare l'errore qua
                    cloudlet.nJobsClass2-=1;
                    cloudlet.completedReqJobsClass2+=1;
                }
                cloudlet.completedRequests++;
            }else{
                if(re.getJobType()==1){
                    cloud.nJobsClass1-=1;
                    cloud.completedReqJobsClass1+=1;
                }else{
                    cloud.nJobsClass2-=1;
                    cloud.completedReqJobsClass2+=1;
                }
                cloud.completedRequests++;
            }
        }
        //writeOnFile(clock, cloudlet, cloud);
        if(re instanceof ArrivalRequest){
            s.updateStatistic(clock, cloudlet, cloud, null);
        }else if(re instanceof CompletedRequest){
            s.updateStatistic(clock, cloudlet, cloud, (CompletedRequest) re);
        }
    }

    private Job chooseWhichClassTwoJobRemove(){
        Random random = new Random();
        //Collections.sort(type2JobRequestInCloudlet, new SortByCompletionTime());
        int randomInteger = random.nextInt(type2JobRequestInCloudlet.size());
        Job job = type2JobRequestInCloudlet.get(randomInteger).getJob();
        //System.out.println("Job da eliminare: " + job);
        job.getCompletedRequest().setToDelete(true);
        type2JobRequestInCloudlet.remove(randomInteger);
        return job;
    }

    private boolean checkList(int jobTwoId){
        for(int i=0; i<typeTwoJobToMove.size(); i++){
            if(jobTwoId == typeTwoJobToMove.get(i)){
                typeTwoJobToMove.remove(i);
                return true;
            }
        }
        return false;
    }

    private void removeFromList(int jobId){
        for(int i=0; i<type2JobRequestInCloudlet.size(); i++){
            if(jobId == type2JobRequestInCloudlet.get(i).getJob().getId()){
                type2JobRequestInCloudlet.remove(i);
            }
        }
    }
}
