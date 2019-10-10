package Server;

import Distribution.Distribution;
import Request.ArrivalRequest;
import Request.CompletedRequest;
import Util.Configuration;
import Util.RequestQueue;

public class Cloud extends Server {

    private RequestQueue requestQueue = RequestQueue.getInstance();
    private Distribution distribution = Distribution.getInstance();
    private static double serviceTimeMu1;
    private static double mu1 = Configuration.MU1CLOUD;
    private static double mu2 = Configuration.MU2CLOUD;
    private static double serviceTimeMu2;
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
        if(r.getType()==1){
            //it handle class1 request
            distribution.selectStream(3);
            //TODO prendi valori da config file
            serviceTimeMu1= distribution.exponential(1.0/mu1);
            cr = new CompletedRequest(r.getJob(),1, serviceTimeMu1);
        }else{
            //it handle class2 request
            distribution.selectStream(4);
            //TODO prendi valori da config file
            serviceTimeMu2 = distribution.exponential(1.0/mu2);
            cr = new CompletedRequest(r.getJob(),2, serviceTimeMu2);
        }
        cr.setServer(this);
        requestQueue.add(cr);
    }

    public static double getServiceTimeMu1() {
        return serviceTimeMu1;
    }

    public static void setServiceTimeMu1(double serviceTimeMu1) {
        Cloud.serviceTimeMu1 = serviceTimeMu1;
    }

    public static double getServiceTimeMu2() {
        return serviceTimeMu2;
    }

    public static void setServiceTimeMu2(double serviceTimeMu2) {
        Cloud.serviceTimeMu2 = serviceTimeMu2;
    }
}
