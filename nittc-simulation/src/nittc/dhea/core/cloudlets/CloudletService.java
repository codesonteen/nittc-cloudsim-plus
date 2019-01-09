package nittc.dhea.core.cloudlets;

import nittc.dhea.core.SimulationService;
import core.networks.NetworkDelay;
import scenarios.dhea.factories.DheaTierFactory;

import java.util.Random;

public abstract class CloudletService extends SimulationService {

    private NetworkDelay networkDelay;

    public void setNetworkDelay(NetworkDelay networkDelayObject){
        networkDelay = networkDelayObject;
    }

    public void setNetworkDelayFromDheaServerTier(String vmType){
        networkDelay = new DheaTierFactory().getNetworkDelay(vmType);
    }

    public NetworkDelay getNetworkDelay(){
        return networkDelay;
    }

    public double getRandomGeneratedDelay(){
        Random random = new Random();
        double rangeMin = 0.0;
        double rangeMax = 0.9;
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }
}
