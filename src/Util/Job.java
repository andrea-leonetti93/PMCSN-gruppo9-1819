package Util;

public class Job {

    public int id;
    public int type;
    private double arrivalTime;
    private double serviceTime;

    @Override
    public String toString() {
        return  "[#" + id +
                ", T=" + type +
                ", A=" + arrivalTime +
                ", S=" + serviceTime +
                ']';
    }

    /**
     *
     * @param id id of the job
     * @param type type of the job
     */
    public Job(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }
}
