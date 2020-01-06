package Util;

import Request.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortByCompletionTime implements Comparator<Request> {

    @Override
    public int compare(Request r1, Request r2) {
        if(r1.getJob().getServiceTime() < r2.getJob().getServiceTime()){
            return 1;
        }
        return -1;
    }


    public static void main(String[] args) {
        ArrayList<Request> lr = new ArrayList<>();
        Job j1 = new Job(1, 1);
        Job j2 = new Job(2, 1);
        j1.setServiceTime(10);
        j2.setServiceTime(5);
        Request r1 = new CompletedRequest(j1);
        lr.add(r1);
        Request r2 = new CompletedRequest(j2);
        lr.add(r2);
        Collections.sort(lr, new SortByCompletionTime());
        for (int i=0; i<lr.size(); i++)
            System.out.println(lr.get(i));
    }
}

