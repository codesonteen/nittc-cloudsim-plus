package nittc.dhea.core.managers;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.vms.Vm;
import nittc.dhea.scenarios.factories.SchedulerFactory;
import nittc.dhea.scenarios.services.vms.CloudVmService;
import nittc.dhea.scenarios.services.vms.GatewayEdgeVmService;
import nittc.dhea.scenarios.services.vms.HouseholdEdgeVmService;

public class VirtualMachineManager {

    public CloudVmService cloudVmService;
    public HouseholdEdgeVmService householdEdgeVmService;
    public GatewayEdgeVmService gatewayEdgeVmService;

    public VirtualMachineManager(){
        cloudVmService = new CloudVmService();
        householdEdgeVmService = new HouseholdEdgeVmService();
        gatewayEdgeVmService = new GatewayEdgeVmService();
    }

    public void setDatacenterBroker(DatacenterBroker broker) {
        cloudVmService.setDatacenterBroker(broker);
        householdEdgeVmService.setDatacenterBroker(broker);
        gatewayEdgeVmService.setDatacenterBroker(broker);
    }

    public void setScheduler(String scheduler){
        cloudVmService.setCloudletScheduler(new SchedulerFactory().getScheduler(scheduler));
        householdEdgeVmService.setCloudletScheduler(new SchedulerFactory().getScheduler(scheduler));
        gatewayEdgeVmService.setCloudletScheduler(new SchedulerFactory().getScheduler(scheduler));
    }

    public void createVirtualMachines(){
        cloudVmService.create();
        householdEdgeVmService.create();
        gatewayEdgeVmService.create();
    }

    public void submitVirtualMachines(){
        cloudVmService.submit();
        householdEdgeVmService.submit();
        gatewayEdgeVmService.submit();
    }

    public Vm getVm(String type){
        if(type.equalsIgnoreCase("ccc") || type.equalsIgnoreCase("cloud"))
            return cloudVmService.getFirstVm();
        else if(type.equalsIgnoreCase("hles"))
            return householdEdgeVmService.getFirstVm();
        else if(type.equalsIgnoreCase("gles"))
            return gatewayEdgeVmService.getFirstVm();
        else
            return null;
    }
}
