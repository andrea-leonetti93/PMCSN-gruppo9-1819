package Distribution;

import Util.Configuration;

public class Distribution {

    private static Distribution distribution = null;
    private Rngs rngs;
    private Rvgs rvgs;

    private Distribution(){
        rngs = new Rngs();
        long seed = Configuration.SEED;
        rngs.plantSeeds(seed);
        rvgs = new Rvgs(this.rngs);
    }

    public static Distribution getInstance(){
        if(distribution == null){
            distribution = new Distribution();
        }
        return distribution;
    }

    public void selectStream (int index ) {
        rvgs.rngs.selectStream(index) ;
    }

    public double exponential(double m)
    {
        return (-m * Math.log(1.0 - rngs.random()));
    }

    public double uniform(double a, double b)
    {
        return (a + (b - a) * rngs.random());
    }

    //TODO vedere se usare o meno la distribuzione di poisson per gli arrivi
    /*public long poisson(double m)
    {
        double t = 0.0;
        long   x = 0;

        while (t < m) {
            t += exponential(1.0);
            x++;
        }
        return (x - 1);
    }*/

}
