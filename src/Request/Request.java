package Request;

import Util.Job;


public abstract class Request implements Comparable<Request> {

    private Job job;
    private int type;
    private double orderTime;

    public Request(Job job, int type) {
        this.job = job;
        this.type = type;
    }

    @Override
    public int compareTo(Request request) {
        if(orderTime<request.getOrderTime()){
            return 1;
        }else if(orderTime>request.getOrderTime()){
            return -1;
        }else{
            return 0;
        }
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(double orderTime) {
        this.orderTime = orderTime;
    }


}
