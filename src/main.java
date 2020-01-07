import Request.RequestGenerator;
import Statistic.IoC;
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
        controller.numbJobEachServer();
        double[] confIntCMP = ioC.computeIoC(ioC.iocCloudMeanPopulation);
        double[] confIntCLMP = ioC.computeIoC(ioC.iocCloudletMeanPopulation);
        double[] confIntGMP = ioC.computeIoC(ioC.iocGlobalMeanPopulation);
        double[] confIntCTM = ioC.computeIoC(ioC.iocCloudThroughputMean);
        double[] confIntCLTM = ioC.computeIoC(ioC.iocCloudletThroughputMean);
        double[] confIntGTM = ioC.computeIoC(ioC.iocGlobalThroughputMean);
        double[] confIntCSM = ioC.computeIoC(ioC.iocCloudServiceMeanTime);
        double[] confIntCLSM = ioC.computeIoC(ioC.iocCloudletServiceMeanTime);
        double[] confIntGSM = ioC.computeIoC(ioC.iocGlobalServiceMeanTime);

        System.out.println("\n INTERVALLI DI CONFIDENZA ");

        System.out.println("\nNumb mean job Cloud: " + confIntCMP[0] + ", width: " + confIntCMP[1]);
        System.out.println("\nNumb mean job Cloudlet: " + confIntCLMP[0] + ", width: " + confIntCLMP[1]);
        System.out.println("\nNumb mean job Global: " + confIntGMP[0] + ", width: " + confIntGMP[1]);

        System.out.println("\nThroughput mean job Cloud: " + confIntCTM[0] + ", width: " + confIntCTM[1]);
        System.out.println("\nThroughput mean job Cloudlet: " + confIntCLTM[0] + ", width: " + confIntCLTM[1]);
        System.out.println("\nThroughput mean job Global: " + confIntGTM[0] + ", width: " + confIntGTM[1]);

        System.out.println("\nService mean time job Cloud: " + confIntCSM[0] + ", width: " + confIntCSM[1]);
        System.out.println("\nService mean time job Cloudlet: " + confIntCLSM[0] + ", width: " + confIntCLSM[1]);
        System.out.println("\nService mean time job Global: " + confIntGSM[0] + ", width: " + confIntGSM[1]);
    }

    private static void algorithm2(){
        RequestQueue requestQueue = RequestQueue.getInstance();
        Controller controller = Controller.getInstance();
        Clock clock = Clock.getInstance();
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
        controller.numbJobEachServer();
    }


}
