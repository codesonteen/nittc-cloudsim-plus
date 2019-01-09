package nittc.dhea.core.hosts;

import nittc.dhea.core.SimulationService;
import nittc.dhea.core.allocationpolicies.CloudVmAllocationPolicy;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;

import java.util.ArrayList;
import java.util.List;

public class HostService extends SimulationService {

    private List<Host> hosts = new ArrayList<>();

    /**
     * Processing Element or CPU
     */
    private List<Pe> peList = new ArrayList<>();

    /**
     * RAM in Megabytes (MB)
     */
    private long ram;

    /**
     * Host Storage in Megabyte
     */
    private long storage = 1000000;

    /**
     * Bandwidth in Megabits per Second (Mbps)
     */
    private long bandwidth;

    /**
     * Set CPU
     * @param number
     * @param mips
     */
    public void setPeList(int number, long mips){
        for(int i = 0; i < number; i++){
            peList.add(new PeSimple(mips, new PeProvisionerSimple()));
        }
    }

    /**
     * Set RAM
     * @param size
     */
    public void setRam(long size){
        ram = size;
    }

    /**
     * Set Bandwidth in Megabits per second (Mbps)
     * @param speed
     */
    public void setBandwidth(long speed){
        bandwidth = speed;
    }

    /**
     * Set Storage size in Megabytes (MB)
     * @param size
     */
    public void setStorage(long size) {
        storage = size;
    }

    /**
     * Create a Host
     */
    public void create(){
        hosts.add(
            new HostSimple(ram, bandwidth, storage, peList)
                .setRamProvisioner(new ResourceProvisionerSimple())
                .setBwProvisioner(new ResourceProvisionerSimple())
                .setVmScheduler(new VmSchedulerSpaceShared())
        );
        flush();
    }

    private void flush(){
        peList = new ArrayList<>();
        ram = 0;
        bandwidth = 0;
    }

    public List<Host> getAllHost(){
        return hosts;
    }
}
