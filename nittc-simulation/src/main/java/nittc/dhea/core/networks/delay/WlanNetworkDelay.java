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
        double rangeMin = 0.001;
        double rangeMax = 0.008;
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }

    public Double getTotalDelay(){
        return getPropogationDelay() + getTransmissionDelay() + getQueueingDelay() + getProcessingDelay();
    }
}
