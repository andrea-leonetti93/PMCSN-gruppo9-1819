package Server;

import Distribution.Distribution;
import Request.ArrivalRequest;
import Request.CompletedRequest;
import Util.Configuration;
import Util.RequestQueue;

public class Cloud extends Server {

    private RequestQueue requestQueue = RequestQueue.getInstance();
    private Distribution distribution = Distribution.getInstance();
    private double serviceTimeMu1;
    private static double mu1 = Configuration.MU1CLOUD;
    private static double mu2 = Configuration.MU2CLOUD;
    private static double setup_time = Configuration.SETUP_TIME;
    private double serviceTimeMu2;
    private static Cloud cloudInstance = null;

    private Cloud() {
    }

    public static Cloud getInstance() {
        if(cloudInstance == null){
            cloudInstance = new Cloud();
        }
        return cloudInstance;
    }

    public void handleRequest(ArrivalRequest r){
        //TODO gestire la richiesta in arrrivo
        CompletedRequest cr;
        if(r.getJobType()==1){
            // it handle class1 request
            distribution.selectStream(3);
            //TODO prendi valori da config file
            serviceTimeMu1= distribution.exponential(1.0/mu1);
            r.getJob().setServiceTime(serviceTimeMu1);
            cr = new CompletedRequest(r.getJob());
            this.nJobsClass1+=1;
            this.completedReqJobsClass1+=1;
        }else{
            // it handle class2 request
            distribution.selectStream(4);
            //TODO prendi valori da config file
            serviceTimeMu2 = distribution.exponential(1.0/mu2);
            r.getJob().setServiceTime(serviceTimeMu2);
            cr = new CompletedRequest(r.getJob());
            this.nJobsClass2+=1;
            this.completedReqJobsClass2+=1;
        }
        this.completedRequests++;
        cr.setServer(this);
        requestQueue.add(cr);
    }

    // handle request from cloudlet to cloud second algorithm
    public void handleRequestFromCloudlet(CompletedRequest cr){
        distribution.selectStream(4);
        serviceTimeMu2 = distribution.exponential(1.0/mu2);
        // setting the new completion value of the job + the setup time for moving the job from the cloudlet to the cloud
        cr.getJob().setServiceTime(serviceTimeMu2 + setup_time);
        cr.setPreempted(true);
        this.nJobsClass2+=1;
        completedRequests++;
        cr.setServer(this);
        requestQueue.add(cr);
    }

}
