package Request;

import Server.Server;
import Util.Job;

public class CompletedRequest extends Request {

    private Server server = null;
    private boolean preempted = false;

    public CompletedRequest(Job job) {
        super(job);
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

    @Override
    public double getRequestTime() {
        return job.getArrivalTime()+job.getServiceTime();
    }

    @Override
    public String toString() {
        return "CR{" +
                 job +
                ", RT=" + getRequestTime() +
                '}';
    }
}
