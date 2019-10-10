package Request;

import Util.Job;


public abstract class Request implements Comparable<Request> {

    protected Job job;

    public Request(Job job) {
        this.job = job;
    }

    @Override
    public int compareTo(Request request) {
        if(getRequestTime()>request.getRequestTime()){
            return 1;
        }else if(getRequestTime()<request.getRequestTime()){
            return -1;
        }else{
            return 0;
        }
    }

    public Job getJob() {
        return job;
    }

    public int getJobType(){
        //TODO cambia type come private
        return job.type;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public abstract double getRequestTime();

    @Override
    public abstract String toString();
}
