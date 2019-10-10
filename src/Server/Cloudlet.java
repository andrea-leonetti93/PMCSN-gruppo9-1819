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
    private int nServerUsed;
    private static Cloudlet cloudletInstance = null;

    private Cloudlet() {
    }

    public static Cloudlet getInstance(){
        if(cloudletInstance == null){
            //TODO valori presi dal file di configurazione!!!!!
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
            //TODO prendi valori da config file
            serviceTimeMu1 = distribution.exponential(1.0/mu1);
            r.getJob().setServiceTime(serviceTimeMu1);
            cr = new CompletedRequest(r.getJob());
            this.nJobsClass1+=1;
        }else{
            //it handle class2 request
            distribution.selectStream(6);
            //TODO prendi valori da config file
            serviceTimeMu2 = distribution.exponential(1.0/mu2);
            r.getJob().setServiceTime(serviceTimeMu2);
            cr = new CompletedRequest(r.getJob());
            this.nJobsClass2+=1;
        }
        cr.setServer(this);
        requestQueue.add(cr);
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
