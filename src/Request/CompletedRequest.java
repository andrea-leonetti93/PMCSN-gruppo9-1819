package Request;

import Server.Server;
import Util.Job;

public class CompletedRequest extends Request {

    private Server server = null;
    private boolean preempted = false;
    private boolean toDelete = false;

    private double requestTime = 0.0;


    public CompletedRequest(Job job) {
        super(job);
        requestTime = job.getArrivalTime()+job.getServiceTime();
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public boolean isPreempted() {
        return preempted;
    }

    public void setPreempted(boolean preempted) {
        this.preempted = preempted;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    @Override
    public double getRequestTime() {
        //return job.getArrivalTime()+job.getServiceTime();
        return requestTime;
    }

    @Override
    public String toString() {
        return "CR{" +
                 job +
                ", RT=" + getRequestTime() +
                ", Server= " + getServer().toString() +
                ", Preempted= " + isPreempted() +
                '}';
    }
}
