package nittc.dhea.services.datacenters;

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
        hostService.setPeList(4, 2458);
        hostService.setRam(1024);
        hostService.setStorage(32000);
        hostService.setBandwidth(1000);
        hostService.create();
    }
}
