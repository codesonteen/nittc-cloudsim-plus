package nittc.dhea.core.utilizationmodels;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelAbstract;
import org.cloudbus.cloudsim.vms.Vm;

public class UtilizationModelAbsolute extends UtilizationModelAbstract {

    private double utilizationValue;

    public UtilizationModelAbsolute(double value) {
        super(Unit.ABSOLUTE);
        this.utilizationValue = value;
    }

    /**
     * Gets the <b>expected</b> utilization of resource at a given simulation time.
     * Such a value can be a percentage in scale from [0 to 1] or an absolute value,
     * depending on the {@link #getUnit()}.
     *
     * <p><b>It is an expected usage value because the actual {@link Cloudlet} resource usage
     * depends on the available {@link Vm} resource.</b></p>
     *
     * @param time the time to get the resource usage.
     * @return the resource utilization at the given time
     * @see #getUnit()
     */
    @Override
    public double getUtilization(double time) {
        return utilizationValue;
    }
}
