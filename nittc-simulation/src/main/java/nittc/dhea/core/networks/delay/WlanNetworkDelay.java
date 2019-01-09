package nittc.dhea.core.networks.delay;

import nittc.dhea.core.networks.NetworkDelay;

import java.text.DecimalFormat;
import java.util.Random;

public class WlanNetworkDelay extends NetworkDelay {

    public WlanNetworkDelay(){
        setRate(300);
    }

    public Double getPropogationDelay(){
        Random random = new Random();
        double rangeMin = 0.0001;
        double rangeMax = 0.0008;
        return Double.parseDouble(new DecimalFormat("####.##").format(Math.ceil(rangeMin + (rangeMax - rangeMin) * random.nextDouble())));
    }

    public Double getTotalDelay(){
        return Double.parseDouble(new DecimalFormat("####.##").format(Math.ceil(getPropogationDelay() + getTransmissionDelay() + getQueueingDelay() + getProcessingDelay())));
    }
}
