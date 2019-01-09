package nittc.dhea.core.vms.services;

import nittc.dhea.core.SimulationService;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.List;

public abstract class VmService extends SimulationService {

    private List<Vm> vmList = new ArrayList<>();
    private int idShift;
    private CloudletScheduler cloudletScheduler;

    public VmService(int idShift){
        this.idShift = idShift;
    }

    protected abstract void createVirtualMachine(int id, long mips, int pesNumber, long ram, long bandwidth, long storage);

    public void submit() {
        getDatacenterBroker().submitVmList(vmList);
    }

    public void addToVmList(Vm vm){
        vmList.add(vm);
    }

    public List<Vm> getVmList() {
        return vmList;
    }

    public Vm getFirstVm(){
        return getVmList().get(0);
    }

    protected int getIdShift() {
        return idShift;
    }

    public void setCloudletScheduler(CloudletScheduler scheduler){
        cloudletScheduler = scheduler;
    }

    public CloudletScheduler getCloudletScheduler(){
        return cloudletScheduler;
    }
}
