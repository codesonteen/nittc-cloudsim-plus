package nittc.dhea.services.datacenters;

import nittc.dhea.core.allocationpolicies.CloudVmAllocationPolicy;
import nittc.dhea.core.datacenters.DatacenterService;
import nittc.dhea.core.hosts.HostService;

public class CloudDatacenter extends DatacenterService {

    public CloudDatacenter(){
        super("CloudDatacenter", new CloudVmAllocationPolicy());
    }

    @Override
    protected void createHosts(){
        hostService = new HostService();
        hostService.setPeList(8, 10000);
        hostService.setRam(16384);
        hostService.setBandwidth(10000);
        hostService.create();
    }
}
