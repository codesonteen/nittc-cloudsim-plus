package nittc.dhea.services.vms;

import nittc.dhea.core.vms.SimpleGatewayEdgeVm;
import nittc.dhea.core.vms.services.VmService;

public class GatewayEdgeVmService extends VmService {

    public GatewayEdgeVmService(){
        super(400);
    }

    public void create(){
        createVirtualMachine(1, 600, 4, 1024, 1000, 32000);
    }

    protected void createVirtualMachine(int id, long mips, int pesNumber, long ram, long bandwidth, long storage) {
        addToVmList(
            new SimpleGatewayEdgeVm(id + getIdShift(), mips, pesNumber)
                .setRam(ram).setBw(bandwidth).setSize(storage)
                .setCloudletScheduler(getCloudletScheduler())
        );
    }
}
