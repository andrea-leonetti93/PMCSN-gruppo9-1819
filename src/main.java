import Request.RequestGenerator;
import Server.Cloud;
import Server.Cloudlet;
import Statistic.IoC;
import Statistic.PrintStatistics;
import Util.Clock;
import Util.Configuration;
import Util.Controller;
import Util.RequestQueue;


public class main {


    public static void main(String[] args) {
        //initialize variables
        Configuration.startConfiguration();
        if(Configuration.ALGORITHM == 1){
            algorithm1();
        }else{
            algorithm2();
        }
    }

    private static void algorithm1() {
        RequestQueue requestQueue = RequestQueue.getInstance();
        Controller controller = Controller.getInstance();
        Clock clock = Clock.getInstance();
        IoC ioC = IoC.getInstance();
        PrintStatistics printStatistics = PrintStatistics.getInstance();
        clock.currentTime = clock.start_time;

        /* GENERO LE RICHIESTE DI ARRIVO*/
        RequestGenerator requestGenerator = new RequestGenerator();
        Thread t = new Thread(requestGenerator);
        t.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /* GESTIONE DELLA PRIMA RICHIESTA*/
        while (clock.currentTime < Configuration.STOP_TIME || requestQueue.size() > 0) {
            controller.getRequest();
        }
        System.out.println("Numero job completati: " + controller.numbCompletedRequest());
        controller.printStatistics();
        ioC.computeIoCForEveryMetric();
        printStatistics.writeStatistics();
    }

    private static void algorithm2(){
        RequestQueue requestQueue = RequestQueue.getInstance();
        Controller controller = Controller.getInstance();
        Cloud cloud = Cloud.getInstance();
        Cloudlet cloudlet = Cloudlet.getInstance();
        Clock clock = Clock.getInstance();
        IoC ioC = IoC.getInstance();
        PrintStatistics printStatistics = PrintStatistics.getInstance();
        clock.currentTime = clock.start_time;
        /* GENERO LE RICHIESTE DI ARRIVO*/
        RequestGenerator requestGenerator = new RequestGenerator();
        Thread t = new Thread(requestGenerator);
        t.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /* GESTIONE DELLA PRIMA RICHIESTA*/
        while(clock.currentTime < Configuration.STOP_TIME || requestQueue.size()>0){
            controller.getRequestAlgorithm2();
        }
        System.out.println("Numero job completati: " + controller.numbCompletedRequest());
        System.out.println("Numero job prelazionati: " + controller.numbPreemptedRequest());
        System.out.println("Numero job classe 2 cloudlet finale: (deve essere 0)" + cloudlet.nJobsClass2);
        System.out.println("Numero job classe 1 cloud completati: " + cloud.completedReqJobsClass1);
        System.out.println("Numero job classe 2 cloud completati: " + cloud.completedReqJobsClass2);
        System.out.println("Numero job classe 1 cloudlet completati: " + cloudlet.completedReqJobsClass1);
        System.out.println("Numero job classe 2 cloudlet completati: " + cloudlet.completedReqJobsClass2);
        System.out.println("Numero job cloudlet completati: " + cloudlet.completedRequests);
        System.out.println("Numero jobId lista job prelazionati: " + controller.typeTwoJobToMove.size());
        System.out.println("Numero request nella lista in cloudlet: " + controller.type2JobRequestInCloudlet.size());
        System.out.println("Numero di tutti i job di classe 2 arrivati alla cloudlet: " + cloudlet.allClass2JobsArrivedToCLoudlet);
        controller.printStatistics();
        ioC.computeIoCForEveryMetric();
        printStatistics.writeStatistics();
    }


}
