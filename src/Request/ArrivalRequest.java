package Request;

import Util.Job;

public class ArrivalRequest extends Request {

    private double arrivalTime;

    public ArrivalRequest(Job job, int type, double arrivalTime) {
        super(job, type);
        this.arrivalTime = arrivalTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }


}
