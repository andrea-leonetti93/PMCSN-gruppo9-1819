package Util;

import Request.Request;

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
}
