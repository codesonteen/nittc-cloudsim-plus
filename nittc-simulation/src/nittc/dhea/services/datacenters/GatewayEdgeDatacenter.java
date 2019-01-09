package nittc.dhea.scenarios.services.datacenters;

import nittc.dhea.core.allocationpolicies.GatewayEdgeVmAllocationPolicy;
import nittc.dhea.core.datacenters.DatacenterService;
import nittc.dhea.core.hosts.HostService;

public class GatewayEdgeDatacenter extends DatacenterService {

    public GatewayEdgeDatacenter(){
        super("GatewayEdgeDatacenter", new GatewayEdgeVmAllocationPolicy());
    }

    @Override
    protected void createHosts() {
        hostService = new HostService();
        hostService.setPeList(2, 2800);
        hostService.setRam(1024);
        hostService.setStorage(32000);
        hostService.setBandwidth(20);
        hostService.create();
    }
}
