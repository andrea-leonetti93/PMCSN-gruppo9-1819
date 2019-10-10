package Util;

import Request.*;

import java.util.concurrent.PriorityBlockingQueue;

public class RequestQueue extends PriorityBlockingQueue<Request> {

    private static RequestQueue requestQueue = null;

    private RequestQueue(){
    }

    public static RequestQueue getInstance()
    {
        if (requestQueue == null)
            requestQueue = new RequestQueue();

        return requestQueue;
    }

    @Override
    public Request poll() {
        return super.poll();
    }

    @Override
    public boolean add(Request request) {
        return super.add(request);
    }

    @Override
    public int size() {
        return super.size();
    }

    public void printQueue() {
        System.out.println(this);
    }

    public static void main(String[] args) {
        RequestQueue q = RequestQueue.getInstance();
        Job j1 = new Job(1, 1);
        Job j2 = new Job(2, 1);
        Job j3 = new Job(3, 2);
        j1.setArrivalTime(2);
        j2.setArrivalTime(4);
        j3.setArrivalTime(1);
        j1.setServiceTime(3);
        j2.setServiceTime(4);
        j3.setServiceTime(1);

        q.printQueue();

        q.add(new ArrivalRequest(j1));

        q.printQueue();

        q.add(new ArrivalRequest(j2));

        q.printQueue();

        q.add(new ArrivalRequest(j3));

        q.printQueue();

        q.add(new CompletedRequest(j1));

        q.add(new CompletedRequest(j2));

        q.add(new CompletedRequest(j3));

        for (int i = 0; i<6; i++) {
            Request poll = q.poll();
            System.out.println(poll);
        }

    }
}
