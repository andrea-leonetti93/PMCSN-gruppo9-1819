package Server;

import Distribution.Distribution;
import Request.ArrivalRequest;
import Request.CompletedRequest;
import Util.Configuration;
import Util.RequestQueue;

public class Cloudlet extends Server {

    private RequestQueue requestQueue = RequestQueue.getInstance();
    private Distribution distribution = Distribution.getInstance();
    private double serviceTimeMu1;
    private double serviceTimeMu2;
    private static double mu1 = Configuration.MU1CLET;
    private static double mu2 = Configuration.MU2CLET;
    private static double p_hyperexpo = Configuration.P_HYPEREXPO;
    private static boolean hyperexpo = Configuration.HYPEREXPO;
    private int nServerUsed;
    private static Cloudlet cloudletInstance = null;
    private double uniform;

    private Cloudlet() {
    }

    public static Cloudlet getInstance(){
        if(cloudletInstance == null){
            cloudletInstance = new Cloudlet();
        }
        return cloudletInstance;
    }

    public void handleRequest(ArrivalRequest r){
        //TODO gestire la richiesta in arrrivo
        CompletedRequest cr;
        if(r.getJobType()==1){
            //it handle class1 request
            distribution.selectStream(5);
            // se hyperexpo allora ci calcoliamo il nuovo service time
            if(hyperexpo){
                serviceTimeMu1 = serviceTimeMu1Hyper();
            }else{
                serviceTimeMu1 = distribution.exponential(1.0/mu1);
            }
            r.getJob().setServiceTime(serviceTimeMu1);
            cr = new CompletedRequest(r.getJob());
            this.nJobsClass1+=1;
        }else{
            //it handle class2 request
            distribution.selectStream(6);
            // se hyperexpo allora ci calcoliamo il nuovo service time
            if(hyperexpo){
                serviceTimeMu2 = serviceTimeMu2Hyper();
            }else{
                serviceTimeMu2 = distribution.exponential(1.0/mu2);
            }
            r.getJob().setServiceTime(serviceTimeMu2);
            cr = new CompletedRequest(r.getJob());
            this.nJobsClass2+=1;
        }
        completedRequests++;
        cr.setServer(this);
        requestQueue.add(cr);
    }

    private double serviceTimeMu1Hyper() {
        distribution.selectStream(2);
        uniform = distribution.uniform(0.0, 1.0);
        distribution.selectStream(5);
        if(uniform < p_hyperexpo){
            serviceTimeMu1 = distribution.exponential(1.0/2*p_hyperexpo*mu1);
            return serviceTimeMu1;
        }else{
            serviceTimeMu1 = distribution.exponential(1.0/2*(1-p_hyperexpo)*mu1);
            return serviceTimeMu1;
        }
    }

    private double serviceTimeMu2Hyper() {
        distribution.selectStream(7);
        uniform = distribution.uniform(0.0, 1.0);
        distribution.selectStream(6);
        if(uniform < p_hyperexpo){
            serviceTimeMu2 = distribution.exponential(1.0/2*p_hyperexpo*mu2);
            return serviceTimeMu2;
        }else{
            serviceTimeMu2 = distribution.exponential(1.0/2*(1-p_hyperexpo)*mu2);
            return serviceTimeMu2;
        }
    }

    public void incrNServerUsed(){
        nServerUsed++;
    }

    public void decrNServerUsed(){
        nServerUsed--;
    }

    public int getnServerUsed() {
        return nServerUsed;
    }

    public void setnServerUsed(int nServerUsed) {
        this.nServerUsed = nServerUsed;
    }
}
