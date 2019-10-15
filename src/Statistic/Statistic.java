package Statistic;

import Request.CompletedRequest;
import Server.Cloud;
import Server.Cloudlet;
import Util.Clock;

public abstract class Statistic {

    public abstract void updateStatistic(Clock clock, Cloudlet cloudlet, Cloud cloud);

    public abstract void updateStatistic(Clock clock, Cloudlet cloudlet, Cloud cloud, CompletedRequest request);

}
