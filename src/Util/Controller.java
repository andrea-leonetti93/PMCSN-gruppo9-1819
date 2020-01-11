package Util;

import Request.*;
import Server.Cloud;
import Server.Cloudlet;
import Statistic.*;
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
    private List<Integer> preemptedRequests;
    public ArrayList<Request> type2JobRequestInCloudlet;
    public List<Integer> typeTwoJobToMove;
    private int N = Configuration.N;
    private static boolean batch_means = Configuration.BATCH_MEANS;
    //private static boolean hyperexpo = Configuration.HYPEREXPO;
    //private FileWriter fileWriter;
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
            //System.out.println("Current time:" + clock.currentTime);
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
            //writeOnFile(clock, cloudlet, cloud);
            if(re instanceof ArrivalRequest){
                s.updateStatistic(clock, cloudlet, cloud, null);
            }else{
                s.updateStatistic(clock, cloudlet, cloud, (CompletedRequest) re);
            }
        }
        //clock.incrCurrentTime();
    }

    /*private void writeOnFile(Clock clock, Cloudlet cloudlet, Cloud cloud) {
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
    }*/

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
            // statistics on each class job
            // mean population
            System.out.println("\nMean population job C1 Cloudlet: " + as.getMeanPopulationJobClassOneClet().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanPopulationJobClassOneClet().getwMean().getStDeviation());
            System.out.println("\nMean population job C2 Cloudlet: " + as.getMeanPopulationJobClassTwoClet().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanPopulationJobClassTwoClet().getwMean().getStDeviation());
            System.out.println("\nMean population job C1 Cloud: " + as.getMeanPopulationJobClassOneCloud().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanPopulationJobClassOneCloud().getwMean().getStDeviation());
            System.out.println("\nMean population job C2 Cloud: " + as.getMeanPopulationJobClassTwoCloud().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanPopulationJobClassTwoCloud().getwMean().getStDeviation());
            // mean throughput
            System.out.println("\nMean throughput job C1 Cloudlet: " + as.getMeanThroughputJobClassOneClet().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanThroughputJobClassOneClet().getwMean().getStDeviation());
            System.out.println("\nMean throughput job C2 Cloudlet: " + as.getMeanThroughputJobClassTwoClet().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanThroughputJobClassTwoClet().getwMean().getStDeviation());
            System.out.println("\nMean throughput job C1 Cloud: " + as.getMeanThroughputJobClassOneCloud().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanThroughputJobClassOneCloud().getwMean().getStDeviation());
            System.out.println("\nMean throughput job C2 Cloud: " + as.getMeanThroughputJobClassTwoCloud().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanThroughputJobClassTwoCloud().getwMean().getStDeviation());
            // mean service time
            System.out.println("\nMean Service time job C1 Cloudlet: " + as.getMeanServiceTimeJobClassOneClet().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanServiceTimeJobClassOneClet().getwMean().getStDeviation());
            System.out.println("\nMean Service time job C2 Cloudlet: " + as.getMeanServiceTimeJobClassTwoClet().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanServiceTimeJobClassTwoClet().getwMean().getStDeviation());
            System.out.println("\nMean Service time job C1 Cloud: " + as.getMeanServiceTimeJobClassOneCloud().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanServiceTimeJobClassOneCloud().getwMean().getStDeviation());
            System.out.println("\nMean Service time job C2 Cloud: " + as.getMeanServiceTimeJobClassTwoCloud().getwMean().getCurrent_mean() +
                    ", dev: " + as.getMeanServiceTimeJobClassTwoCloud().getwMean().getStDeviation());
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
            // statistics on each class job
            // mean population
            System.out.println("\nMean population job C1 Cloudlet: " + bs.getMeanPopulationJobClassOneClet().getCurrent_mean() +
                    ", dev: " + bs.getMeanPopulationJobClassOneClet().getStDeviation());
            System.out.println("\nMean population job C2 Cloudlet: " + bs.getMeanPopulationJobClassTwoClet().getCurrent_mean() +
                    ", dev: " + bs.getMeanPopulationJobClassTwoClet().getStDeviation());
            System.out.println("\nMean population job C1 Cloud: " + bs.getMeanPopulationJobClassOneCloud().getCurrent_mean() +
                    ", dev: " + bs.getMeanPopulationJobClassOneCloud().getStDeviation());
            System.out.println("\nMean population job C2 Cloud: " + bs.getMeanPopulationJobClassTwoCloud().getCurrent_mean() +
                    ", dev: " + bs.getMeanPopulationJobClassTwoCloud().getStDeviation());
            // mean throughput
            System.out.println("\nMean throughput job C1 Cloudlet: " + bs.getMeanThroughputJobClassOneClet().getCurrent_mean() +
                    ", dev: " + bs.getMeanThroughputJobClassOneClet().getStDeviation());
            System.out.println("\nMean throughput job C2 Cloudlet: " + bs.getMeanThroughputJobClassTwoClet().getCurrent_mean() +
                    ", dev: " + bs.getMeanThroughputJobClassTwoClet().getStDeviation());
            System.out.println("\nMean throughput job C1 Cloud: " + bs.getMeanThroughputJobClassOneCloud().getCurrent_mean() +
                    ", dev: " + bs.getMeanThroughputJobClassOneCloud().getStDeviation());
            System.out.println("\nMean throughput job C2 Cloud: " + bs.getMeanThroughputJobClassTwoCloud().getCurrent_mean() +
                    ", dev: " + bs.getMeanThroughputJobClassTwoCloud().getStDeviation());
            // mean service time
            System.out.println("\nMean Service time job C1 Cloudlet: " + bs.getMeanServiceTimeJobClassOneClet().getCurrent_mean() +
                    ", dev: " + bs.getMeanServiceTimeJobClassOneClet().getStDeviation());
            System.out.println("\nMean Service time job C2 Cloudlet: " + bs.getMeanServiceTimeJobClassTwoClet().getCurrent_mean() +
                    ", dev: " + bs.getMeanServiceTimeJobClassTwoClet().getStDeviation());
            System.out.println("\nMean Service time job C1 Cloud: " + bs.getMeanServiceTimeJobClassOneCloud().getCurrent_mean() +
                    ", dev: " + bs.getMeanServiceTimeJobClassOneCloud().getStDeviation());
            System.out.println("\nMean Service time job C2 Cloud: " + bs.getMeanServiceTimeJobClassTwoCloud().getCurrent_mean() +
                    ", dev: " + bs.getMeanServiceTimeJobClassTwoCloud().getStDeviation());
        }
    }

    public void getRequestAlgorithm2() {
        Request re = requestQueue.poll();
        if(re != null){
            //TODO aggiornare qui clock globale
            clock.currentTime = re.getRequestTime();
            //System.out.println("To handle:" + re);
            //System.out.println("Current time:" + clock.currentTime);
            //TODO cambiare questo controllo che è sbagliato!!!!!!!
            /*if(re instanceof ArrivalRequest){
                handleRequest(re);
            }else if(re.getJobType() == 1){
                handleRequest(re);
            }
            int jobId = re.getJob().getId();
            if(re instanceof CompletedRequest && ((CompletedRequest) re).isToDelete()){
                preemptedRequests.add(jobId);
            }else{
                handleRequest(re);
            }*/
            int jobId = re.getJob().getId();
            if(typeTwoJobToMove.size()!=0 && re instanceof CompletedRequest && ((CompletedRequest) re).isToDelete()){
                // handle two queues
                //int jobId = re.getJob().getId();
                if(checkList(jobId)){
                    // the job is removed in checklist function
                    // removeFromList(jobId);
                    // update the job elaboration time and send it to the cloud
                    //cloud.handleRequestFromCloudlet((CompletedRequest) re);
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
                    Job job = chooseWhichClassTwoJobRemove();
                    typeTwoJobToMove.add(job.getId());
                    //job.getCompletedRequest().setToDelete(true);
                    //job.setCompletedRequest(null);
                    //reqToDelete.setJob(null);
                    cloudlet.nJobsClass2-=1;
                    cloudlet.completedRequests--;
                    cloudlet.completedReqJobsClass2-=1;
                    //gestire jobclassone/two
                    //nJobClass1 and completedRequest are updating in the cloudlet class
                    cloudlet.handleRequest((ArrivalRequest) re);
                    cloud.handleRequestFromCloudlet(job);
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
        }else if(re instanceof CompletedRequest){
            completedRequests.add((CompletedRequest) re);
            //TODO gestire la richiesta prelazionata che deve dare come instanceof quella del cloud
            if(((CompletedRequest) re).getServer() instanceof Cloudlet){
                if(re.getJobType()==1){
                    cloudlet.nJobsClass1-=1;
                }else{
                    //TODO sistemare l'errore qua
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
        }else if(re instanceof CompletedRequest){
            s.updateStatistic(clock, cloudlet, cloud, (CompletedRequest) re);
        }
    }

    private Job chooseWhichClassTwoJobRemove(){
        Collections.sort(type2JobRequestInCloudlet, new SortByCompletionTime());
        Job job = type2JobRequestInCloudlet.get(0).getJob();
        job.getCompletedRequest().setToDelete(true);
        type2JobRequestInCloudlet.remove(0);
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
