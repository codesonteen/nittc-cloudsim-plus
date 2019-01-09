package nittc.dhea.core.vms;

import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.vms.VmSimple;

public class SimpleGatewayEdgeVm extends VmSimple {
    /**
     * Creates a Vm with 1024 MEGA of RAM, 1000 Megabits/s of Bandwidth and 1024 MEGA of Storage Size.
     * <p>
     * To change these values, use the respective setters. While the Vm {@link #isCreated()
     * is being instantiated}, such values can be changed freely.
     *
     * @param id           unique ID of the VM
     * @param mipsCapacity the mips capacity of each Vm {@link Pe}
     * @param numberOfPes  amount of {@link Pe} (CPU cores)
     */
    public SimpleGatewayEdgeVm(int id, long mipsCapacity, long numberOfPes) {
        super(id, mipsCapacity, numberOfPes);
    }
}
