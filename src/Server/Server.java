package Server;

public class Server {

    public int nJobsClass1 = 0;
    public int nJobsClass2 = 0;
    public double completedRequests = 0.0;
    public double completedReqJobsClass1 = 0.0;
    public double completedReqJobsClass2 = 0.0;

    public int getAllJobs(){
        return nJobsClass1+nJobsClass2;
    }
}
