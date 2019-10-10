package Request;

import Server.Server;
import Util.Job;

public class CompletedRequest extends Request {

    private double completingTime;

    private Server server = null;

    public CompletedRequest(Job job, int type, double completingTime) {
        super(job, type);
        this.completingTime = completingTime;
    }

    public double getCompletingTime() {
        return completingTime;
    }

    public void setCompletingTime(double completingTime) {
        this.completingTime = completingTime;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
