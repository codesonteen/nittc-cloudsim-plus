package scenarios.dhea.profiles;

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;

public class BrokerProfile {

    private static DatacenterBrokerSimple broker;

    public static DatacenterBrokerSimple getDatacenterBroker(){
        if(broker == null){
            broker = new DatacenterBrokerSimple(SimulationProfile.getSimulation());
        }
        return broker;
    }
}
