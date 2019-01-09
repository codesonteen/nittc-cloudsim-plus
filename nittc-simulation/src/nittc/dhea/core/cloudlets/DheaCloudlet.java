package nittc.dhea.core.cloudlets;

import org.cloudbus.cloudsim.cloudlets.CloudletSimple;

public class DheaCloudlet extends CloudletSimple {

    private double generatedTime;

    /**
     * Creates a Cloudlet with no priority or id. The id is defined when the Cloudlet is submitted to
     * a {@link DatacenterBroker}. The file size and output size is defined as 1.
     *
     * @param length    the length or size (in MI) of this cloudlet to be executed in a VM (check out {@link #setLength(long)})
     * @param pesNumber number of PEs that Cloudlet will require
     */
    public DheaCloudlet(long length, int pesNumber) {
        super(length, pesNumber);
    }

    /**
     * Creates a Cloudlet with no priority or id. The id is defined when the Cloudlet is submitted to
     * a {@link DatacenterBroker}. The file size and output size is defined as 1.
     *
     * @param length    the length or size (in MI) of this cloudlet to be executed in a VM (check out {@link #setLength(long)})
     * @param pesNumber number of PEs that Cloudlet will require
     */
    public DheaCloudlet(long length, long pesNumber) {
        super(length, pesNumber);
    }

    /**
     * Creates a Cloudlet with no priority and file size and output size equal to 1.
     * To change these values, use the respective setters.
     *
     * @param id        the unique ID of this cloudlet
     * @param length    the length or size (in MI) of this cloudlet to be executed in a VM (check out {@link #setLength(long)})
     * @param pesNumber the pes number
     */
    public DheaCloudlet(long id, long length, long pesNumber) {
        super(id, length, pesNumber);
    }

    public void setGeneratedTime(double time){
        this.generatedTime = time;
    }

    public Double getGeneratedTime(){
        return generatedTime;
    }
}
