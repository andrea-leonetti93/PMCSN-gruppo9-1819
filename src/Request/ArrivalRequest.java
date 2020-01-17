package Request;

import Util.Job;

public class ArrivalRequest extends Request {

    double requestTime = 0.0;

    public ArrivalRequest(Job job) {
        super(job);
        requestTime = job.getArrivalTime();
    }

    @Override
    public double getRequestTime() {
        //return job.getArrivalTime();
        return requestTime;
    }

    @Override
    public String toString() {
        return "AR{" +
                 job +
                ", RT=" + getRequestTime() +
                '}';
    }

}
