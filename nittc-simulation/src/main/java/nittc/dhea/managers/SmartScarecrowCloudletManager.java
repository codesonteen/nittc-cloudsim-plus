package nittc.dhea.managers;

import nittc.dhea.core.cloudlets.CloudletManager;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.vms.Vm;
import nittc.dhea.services.cloudlets.ObjectDetectionCloudletService;

public class SmartScarecrowCloudletManager implements CloudletManager {

    private Vm vm;
    private ObjectDetectionCloudletService objectDetectionCloudletService;

    public SmartScarecrowCloudletManager(Vm vm, double timeLimit, int numberOfCloudlets){
        this.vm = vm;
        this.objectDetectionCloudletService = new ObjectDetectionCloudletService();
        this.objectDetectionCloudletService.setTimeLimit(timeLimit);
        this.objectDetectionCloudletService.setNumberOfCloudlets(numberOfCloudlets);
    }

    public void setDatacenterBroker(DatacenterBroker broker){
        objectDetectionCloudletService.setDatacenterBroker(broker);
    }

    public void setServerTier(String vmType) {
        objectDetectionCloudletService.setNetworkDelayFromDheaServerTier(vmType);
    }

    public void createAndSubmitCloudlets() {
        objectDetectionCloudletService.createAndSubmitCloudlets(vm);
    }
}
