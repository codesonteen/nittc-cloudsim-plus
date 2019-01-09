package nittc.dhea.core.networks.delay;

import nittc.dhea.core.networks.NetworkDelay;

import java.util.Random;

public class WanNetworkDelay extends NetworkDelay {

    public WanNetworkDelay(){
        setRate(20);
    }

    public Double getPropogationDelay(){
        Random random = new Random();
        double rangeMin = 0.0020;
        double rangeMax = 0.0060;
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }

    public Double getTotalDelay(){
        return getPropogationDelay() + getTransmissionDelay() + getQueueingDelay() + getProcessingDelay();
    }
}
