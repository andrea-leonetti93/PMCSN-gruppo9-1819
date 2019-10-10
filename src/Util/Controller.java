package Util;

import Request.*;
import Server.Cloud;
import Server.Cloudlet;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private static Controller controller = null;
    private RequestQueue requestQueue = RequestQueue.getInstance();
    private Cloudlet cloudlet = Cloudlet.getInstance();
    private Cloud cloud = Cloud.getInstance();
    private Clock clock = Clock.getInstance();
    private List<CompletedRequest> completedRequests;
    private int N = Configuration.N;

    private Controller(){
        completedRequests = new ArrayList<CompletedRequest>();
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
                if(cloudlet.getnServerUsed() <= N){
                    cloudlet.incrNServerUsed();
                    cloudlet.handleRequest((ArrivalRequest) re);
                    //gestire l'add del server che gestisce la richiesta alla richeista stessa
                }
                //it the cloudlet is full, send the request to the cloud
                else{
                    cloud.handleRequest((ArrivalRequest) re);
                }
            }
            else{
                completedRequests.add((CompletedRequest) re);
                if(((CompletedRequest) re).getServer() instanceof Cloudlet){
                    cloudlet.decrNServerUsed();
                }
            }
        }
        //clock.incrCurrentTime();
    }

}
