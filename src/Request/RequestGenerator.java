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
        // lambda1/2 gets value from config file
        double lambda1 = Configuration.LAMBDA1;
        double lambda2 = Configuration.LAMBDA2;
        double prob_lambda1 = Configuration.PROB_LAMBDA1;
        //double prob_lambda2 = Configuration.PROB_LAMBDA2;
        double arrive;
        double arrive1;
        double arrive2;
        int i = 0;
        ArrivalRequest r;
        Job j;
        double curTime = 0;
        while(curTime <= STOP_TIME){
            distribution.selectStream(1);
            arrive = distribution.exponential(1.0/(lambda1+lambda2));
            distribution.selectStream(0);
            uniform = distribution.uniform(0.0, 1.0);
            if(uniform < prob_lambda1){
                //distribution.selectStream(1);
                arrive1 = clock.lastClass1Arrival + arrive;
                j = new Job(i, 1);
                j.setArrivalTime(arrive1);
                //TODO non aggiornare tempo globale qui
                //time = time + arrive1;
                clock.lastClass1Arrival = arrive1;
            }else{
                //distribution.selectStream(2);
                arrive2 = clock.lastClass1Arrival + arrive;
                j = new Job(i, 2);
                j.setArrivalTime(arrive2);
                //TODO non aggiornare tempo globale qui
                //time = time + arrive2;
                clock.lastClass1Arrival = arrive2;
            }
            i++;
            // put the request in the queue
            r = new ArrivalRequest(j);
            requestsQueue.add(r);
            //clock.currentTime = j.getArrivalTime();
            curTime = Math.max(clock.lastClass1Arrival,clock.lastClass1Arrival);
            //System.out.println("Arrival time = " + curTime);
        }
        System.out.println("Numero richieste generate: " + i);
    }

    @Override
    public void run() {
        generateRequest();
    }

    public static void main(String[] args) {
        Configuration.getInstance();
        Thread t = new Thread(new RequestGenerator());
        RequestQueue.getInstance().printQueue();
        t.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RequestQueue.getInstance().printQueue();

    }
}

