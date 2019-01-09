package nittc.dhea.core.cloudlets;

import nittc.dhea.core.SimulationService;
import nittc.dhea.core.networks.NetworkDelay;
import nittc.dhea.factories.DheaTierFactory;

import java.text.DecimalFormat;
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
        return Double.parseDouble(new DecimalFormat("####.##").format(Math.ceil(rangeMin + (rangeMax - rangeMin) * random.nextDouble())));
    }
}
