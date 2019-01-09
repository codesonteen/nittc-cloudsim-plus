package nittc.dhea.core.datacenters;

import nittc.dhea.core.SimulationService;
import nittc.dhea.core.allocationpolicies.CloudVmAllocationPolicy;
import nittc.dhea.core.hosts.HostService;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;

import java.util.ArrayList;
import java.util.List;

public abstract class DatacenterService extends SimulationService {

    /**
     * Relationship: HostService
     */
    protected HostService hostService;

    private List<Datacenter> datacenters = new ArrayList<>();

    private String name = "UnnamedDatacenter";

    private VmAllocationPolicy vmAllocationPolicy;

    /**
     * Constructor
     * @param name
     */
    protected DatacenterService(String name, VmAllocationPolicy vmAllocationPolicy){
        this.name = name;
        this.vmAllocationPolicy = vmAllocationPolicy;
    }

    /**
     * Create a Datacenter
     * @param hosts - List of Hosts in this Datacenter
     */
    public void createDatacenter(List<Host> hosts){
        Datacenter datacenter = new DatacenterSimple(super.getSimulation(), hosts, vmAllocationPolicy);
        datacenter.setName(name);
        datacenters.add(datacenter);
    }

    public void create(){
        createHosts();
        createDatacenter(hostService.getAllHost());
    }

    protected abstract void createHosts();

    /**
     * Retrieve the list of datacenters
     * @return List
     */
    public List<Datacenter> all(){
        return datacenters;
    }

    /**
     * Retrieve a datacenter from index argument
     * @param index - The number which indicates the datacenter in the list
     * @return Datacenter
     */
    public Datacenter getDatacenter(int index){
        return datacenters.get(index);
    }

    public Datacenter getFirstDatacenter(){
        return datacenters.get(0);
    }

    public Datacenter getLastDatacenter(){
        return datacenters.get(datacenters.size() - 1);
    }
}
