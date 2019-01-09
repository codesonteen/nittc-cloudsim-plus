package scenarios.dhea.profiles;

import org.cloudbus.cloudsim.core.CloudSim;

public class SimulationProfile {

    private static CloudSim simulation;

    public static CloudSim getSimulation(){
        if(simulation == null){
            simulation = new CloudSim();
        }
        return simulation;
    }
}
