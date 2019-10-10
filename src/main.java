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
        RequestGenerator requestGenerator = new RequestGenerator();
        Thread t = new Thread(requestGenerator);
        t.start();
        clock.currentTime = clock.start_time;
        while(clock.currentTime < Configuration.STOP_TIME){
            controller.getRequest();
        }

    }

    public void algorithm2(){

    }


}
