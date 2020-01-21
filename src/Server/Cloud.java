package Server;

import Distribution.Distribution;
import Request.ArrivalRequest;
import Request.CompletedRequest;
import Util.Configuration;
import Util.Job;
import Util.RequestQueue;

//import java.util.ArrayList;

public class Cloud extends Server {

    private RequestQueue requestQueue = RequestQueue.getInstance();
    private Distribution distribution = Distribution.getInstance();
    private static double mu1 = Configuration.MU1CLOUD;
    private static double mu2 = Configuration.MU2CLOUD;
    private static double setup_time = Configuration.SETUP_TIME;
    //private double nJobClass2PreemptedCompleted = 0.0;
    private static Cloud cloudInstance = null;
    //private ArrayList<CompletedRequest> preemptedRequest = new ArrayList<>();

    private Cloud() {
    }

    public static Cloud getInstance() {
        if(cloudInstance == null){
            cloudInstance = new Cloud();
        }
        return cloudInstance;
    }

    public void handleRequest(ArrivalRequest r){
        CompletedRequest cr;
        if(r.getJobType()==1){
            double serviceTimeMu1;
            // it handle class1 request
            distribution.selectStream(3);
            serviceTimeMu1= distribution.exponential(1.0/mu1);
            r.getJob().setServiceTime(serviceTimeMu1);
            cr = new CompletedRequest(r.getJob());
            this.nJobsClass1+=1;
        }else{
            double serviceTimeMu2;
            // it handle class2 request
            distribution.selectStream(4);
            serviceTimeMu2 = distribution.exponential(1.0/mu2);
            r.getJob().setServiceTime(serviceTimeMu2);
            cr = new CompletedRequest(r.getJob());
            this.nJobsClass2+=1;
        }
        cr.setServer(this);
        requestQueue.add(cr);
    }

    // handle request from cloudlet to cloud second algorithm
    public void handleRequestFromCloudlet(Job job){
        CompletedRequest cr;
        double serviceTimeMu2;
        distribution.selectStream(4);
        serviceTimeMu2 = distribution.exponential(1.0/mu2);
        // setting the new completion value of the job + the setup time for moving the job from the cloudlet to the cloud
        cr = new CompletedRequest(job);
        double newServiceTime = serviceTimeMu2 + setup_time;
        job.setServiceTime(newServiceTime);
        cr.setPreempted(true);
        this.nJobsClass2+=1;
        cr.setServer(this);
        //preemptedRequest.add(cr);
        requestQueue.add(cr);
    }
}
