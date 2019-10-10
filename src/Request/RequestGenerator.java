package Request;

import Distribution.Distribution;
import Util.Clock;
import Util.Configuration;
import Util.Job;
import Util.RequestQueue;

public class RequestGenerator implements Runnable{

    private double STOP_TIME = Configuration.STOP_TIME;

    private void generateRequest(){
        Distribution distribution = Distribution.getInstance();
        RequestQueue requestsQueue = RequestQueue.getInstance();
        Clock clock = Clock.getInstance();
        double uniform;
        //lambda1/2 gets value from config file
        double lambda1 = Configuration.LAMBDA1;
        double lambda2 = Configuration.LAMBDA2;
        double prob_lambda1 = Configuration.PROB_LAMBDA1;
        //double prob_lambda2 = Configuration.PROB_LAMBDA2;
        double arrive1;
        double arrive2;
        int i = 0;
        ArrivalRequest r;
        while(clock.currentTime < STOP_TIME){
            distribution.selectStream(0);
            uniform = distribution.uniform(0.0, 1.0);
            if(uniform < prob_lambda1){
                distribution.selectStream(1);
                arrive1 = distribution.exponential(1.0/lambda1);
                Job j = new Job(i);
                r = new ArrivalRequest(j, 1, arrive1);
                r.setOrderTime(time+arrive1);
                //TODO non aggiornare tempo globale qui
                time = time + arrive1;
            }else{
                distribution.selectStream(2);
                arrive2 = distribution.exponential(1.0/lambda2);
                Job j = new Job(i);
                r = new ArrivalRequest(j, 2, arrive2);
                r.setOrderTime(time+arrive2);
                //TODO non aggiornare tempo globale qui
                time = time + arrive2;
            }
            i++;
            //put the request in the queue
            requestsQueue.add(r);
        }
    }

    @Override
    public void run() {
        generateRequest();
    }
}
