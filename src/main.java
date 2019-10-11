import Request.RequestGenerator;
import Util.Clock;
import Util.Configuration;
import Util.Controller;
import Util.RequestQueue;


public class main {


    public static void main(String[] args) {
        //initialize variables
        Configuration.startConfiguration();
        algorithm1();
    }

    private static void algorithm1(){
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
            controller.getRequest();
        }
        System.out.println("Numero job completati: " + controller.numbCompletedRequest());
        controller.numbJobEachServer();
    }

    public void algorithm2(){

    }


}
