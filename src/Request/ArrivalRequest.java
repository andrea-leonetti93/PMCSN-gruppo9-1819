package Request;

import Util.Job;

public class ArrivalRequest extends Request {


    public ArrivalRequest(Job job) {
        super(job);
    }

    @Override
    public double getRequestTime() {
        return job.getArrivalTime();
    }

    @Override
    public String toString() {
        return "AR{" +
                 job +
                ", RT=" + getRequestTime() +
                '}';
    }

}
