package nittc.dhea.scenarios.services.vms;

import nittc.dhea.core.schedulers.CloudletSchedulerSpaceSharedDropTimeout;
import nittc.dhea.core.vms.SimpleCloudVm;
import nittc.dhea.core.vms.services.VmService;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;

public class CloudVmService extends VmService {

    public CloudVmService(){
        super(100);
    }

    public void create(){
        createVirtualMachine(1, 10000, 8, 16384, 1000, 1000000);
    }

    protected void createVirtualMachine(int id, long mips, int pesNumber, long ram, long bandwidth, long storage) {
        addToVmList(
            new SimpleCloudVm(id + getIdShift(), mips, pesNumber)
                .setRam(ram).setBw(bandwidth).setSize(storage)
                .setCloudletScheduler(getCloudletScheduler())
        );
    }
}
