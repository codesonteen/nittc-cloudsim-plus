package nittc.dhea.core.networks.delay;

import nittc.dhea.core.networks.NetworkDelay;

import java.text.DecimalFormat;
import java.util.Random;

public class NoNetworkDelay extends NetworkDelay {

    public NoNetworkDelay(){
        // Nothing to do here
    }

    public Double getPropogationDelay(){
        Random random = new Random();
        double rangeMin = 0.0000;
        double rangeMax = 0.0000;
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }

    @Override
    public double getPropogationDelay(double distance) {
        return 0;
    }

    @Override
    public double getTransmissionDelay() {
        return 0;
    }

    public Double getTotalDelay(){
        return getPropogationDelay() + getTransmissionDelay() + getQueueingDelay() + getProcessingDelay();
    }
}
