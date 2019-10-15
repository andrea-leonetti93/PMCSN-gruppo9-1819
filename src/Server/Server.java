package Server;

public class Server {

    public int nJobsClass1 = 0;
    public int nJobsClass2 = 0;
    public double completedRequests = 0.0;

    public int getnJobsClass1() {
        return nJobsClass1;
    }

    public void setnJobsClass1(int nJobsClass1) {
        this.nJobsClass1 = nJobsClass1;
    }

    public int getnJobsClass2() {
        return nJobsClass2;
    }

    public void setnJobsClass2(int nJobsClass2) {
        this.nJobsClass2 = nJobsClass2;
    }

    public int getAllJobs(){
        return nJobsClass1+nJobsClass2;
    }
}
