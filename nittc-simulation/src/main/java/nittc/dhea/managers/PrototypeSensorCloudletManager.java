package nittc.dhea.managers;

import nittc.dhea.core.cloudlets.CloudletManager;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.vms.Vm;
import nittc.dhea.services.cloudlets.PrototypeSensorCloudletService;

import java.util.HashMap;

public class PrototypeSensorCloudletManager implements CloudletManager {

    private Vm vm;
    private PrototypeSensorCloudletService cloudletService;

    public PrototypeSensorCloudletManager(Vm vm, double timeLimit, HashMap numberOfCloudlets){
        this.vm = vm;
        this.cloudletService = new PrototypeSensorCloudletService();
        this.cloudletService.setTimeLimit(timeLimit);
        this.cloudletService.setNumberOfCloudlets(numberOfCloudlets);
    }

    public void setDatacenterBroker(DatacenterBroker broker){
        cloudletService.setDatacenterBroker(broker);
    }

    public void setServerTier(String vmType) {
        cloudletService.setNetworkDelayFromDheaServerTier(vmType);
    }

    public void createAndSubmitCloudlets() {
        cloudletService.createAndSubmitCloudlets(vm);
    }
}
