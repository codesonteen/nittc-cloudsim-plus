package nittc.dhea.scenarios.services.vms;

import nittc.dhea.core.schedulers.CloudletSchedulerSpaceSharedDropTimeout;
import nittc.dhea.core.vms.SimpleGatewayEdgeVm;
import nittc.dhea.core.vms.services.VmService;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;

public class GatewayEdgeVmService extends VmService {

    public GatewayEdgeVmService(){
        super(400);
    }

    public void create(){
        createVirtualMachine(1, 1400, 2, 1024, 20, 32000);
    }

    protected void createVirtualMachine(int id, long mips, int pesNumber, long ram, long bandwidth, long storage) {
        addToVmList(
            new SimpleGatewayEdgeVm(id + getIdShift(), mips, pesNumber)
                .setRam(ram).setBw(bandwidth).setSize(storage)
                .setCloudletScheduler(getCloudletScheduler())
        );
    }
}
