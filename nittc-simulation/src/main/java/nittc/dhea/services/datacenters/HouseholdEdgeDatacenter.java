package nittc.dhea.services.datacenters;

import nittc.dhea.core.allocationpolicies.HouseholdEdgeVmAllocationPolicy;
import nittc.dhea.core.datacenters.DatacenterService;
import nittc.dhea.core.hosts.HostService;

public class HouseholdEdgeDatacenter extends DatacenterService {

    public HouseholdEdgeDatacenter(){
        super("HouseholdEdgeDatacenter", new HouseholdEdgeVmAllocationPolicy());
    }

    @Override
    protected void createHosts() {
        hostService = new HostService();
        hostService.setPeList(8, 10000);
        hostService.setRam(16384);
        hostService.setBandwidth(10000);
        hostService.create();
    }
}
