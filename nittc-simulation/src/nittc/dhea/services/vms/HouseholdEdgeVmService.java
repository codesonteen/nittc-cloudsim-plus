package nittc.dhea.scenarios.services.vms;

import nittc.dhea.core.schedulers.CloudletSchedulerSpaceSharedDropTimeout;
import nittc.dhea.core.vms.SimpleHouseholdEdgeVm;
import nittc.dhea.core.vms.services.VmService;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;

public class HouseholdEdgeVmService extends VmService {

    public HouseholdEdgeVmService(){
        super(300);
    }

    public void create(){
        createVirtualMachine(1, 10000, 2, 8192, 1000, 1000000);
    }

    protected void createVirtualMachine(int id, long mips, int pesNumber, long ram, long bandwidth, long storage) {
        addToVmList(
            new SimpleHouseholdEdgeVm(id + getIdShift(), mips, pesNumber)
                .setRam(ram).setBw(bandwidth).setSize(storage)
                .setCloudletScheduler(getCloudletScheduler())
        );
    }
}
