package nittc.dhea.core;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.core.CloudSim;
import scenarios.dhea.profiles.BrokerProfile;
import scenarios.dhea.profiles.SimulationProfile;

public abstract class SimulationService {
    private CloudSim simulation;
    private DatacenterBroker broker;

    public void setSimulation(CloudSim simulation){
        this.simulation = simulation;
    }

    public void setDatacenterBroker(DatacenterBroker broker){
        this.broker = broker;
    }

    public CloudSim getSimulation(){
        return simulation;
    }

    public DatacenterBroker getDatacenterBroker(){
        return broker;
    }
}
