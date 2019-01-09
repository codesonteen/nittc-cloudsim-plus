package nittc.dhea.managers;

import nittc.dhea.core.datacenters.DatacenterService;
import org.cloudbus.cloudsim.core.CloudSim;
import nittc.dhea.services.datacenters.CloudDatacenter;
import nittc.dhea.services.datacenters.GatewayEdgeDatacenter;
import nittc.dhea.services.datacenters.HouseholdEdgeDatacenter;

public class DatacenterManager {

    DatacenterService cloud;
    DatacenterService household;
    DatacenterService gateway;

    public DatacenterManager(){
        cloud     = new CloudDatacenter();
        household = new HouseholdEdgeDatacenter();
        gateway   = new GatewayEdgeDatacenter();
    }

    public void setSimulation(CloudSim simulation){
        cloud.setSimulation(simulation);
        household.setSimulation(simulation);
        gateway.setSimulation(simulation);
    }

    public void createDatacenters(){
        cloud.create();
        household.create();
        gateway.create();
    }
}
