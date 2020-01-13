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

}

