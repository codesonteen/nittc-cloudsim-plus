package nittc.dhea.core.allocationpolicies;

import nittc.dhea.core.vms.SimpleGatewayEdgeVm;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyAbstract;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GatewayEdgeVmAllocationPolicy extends VmAllocationPolicyAbstract {
    /**
     * Instantiates a VmAllocationPolicySimple.
     */
    public GatewayEdgeVmAllocationPolicy() {
        super();
    }

    /**
     * Instantiates a VmAllocationPolicySimple, changing the {@link Function} to select a Host for a Vm
     * in order to define a different policy.
     *
     * @param findHostForVmFunction a {@link Function} to select a Host for a given Vm.
     * @see VmAllocationPolicy#setFindHostForVmFunction(BiFunction)
     */
    public GatewayEdgeVmAllocationPolicy(final BiFunction<VmAllocationPolicy, Vm, Optional<Host>> findHostForVmFunction) {
        super(findHostForVmFunction);
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        boolean result = false;
        if(vm instanceof SimpleGatewayEdgeVm){
            Host host = getHostList().get(0);
            result = host.createVm(vm);
        }
        else{
            //System.out.println("[CloudVmAllocationPolicy] The requested VM " + vm + " is not Cloud VM, Skipping");
        }
        return result;
    }

    /**
     * Finds a host that has enough resources to place a given VM.
     * <b>Classes must implement this method to define how to select a Host for a given VM.</b>
     * They just have to provide a default implementation. However, this implementation can be dynamically
     * changed by calling {@link #setFindHostForVmFunction(BiFunction)}.
     *
     * @param vm the vm to find a host for it
     * @return an {@link Optional} containing a suitable Host to place the VM or an empty {@link Optional} if no suitable Host was found
     */
    @Override
    public Optional<Host> findHostForVm(Vm vm) {
        return Optional.empty();
    }
}
