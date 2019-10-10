package Request;

import Server.Server;
import Util.Job;

public class CompletedRequest extends Request {

    private Server server = null;

    public CompletedRequest(Job job) {
        super(job);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
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
