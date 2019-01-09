package nittc.dhea.scenarios.factories;

import nittc.dhea.core.networks.NetworkDelay;
import nittc.dhea.core.networks.delay.NoNetworkDelay;
import nittc.dhea.core.networks.delay.WanNetworkDelay;
import nittc.dhea.core.networks.delay.WlanNetworkDelay;
import nittc.dhea.core.schedulers.CloudletSchedulerSpaceSharedDropTimeout;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;

public class DheaTierFactory {
    public NetworkDelay getNetworkDelay(String vmType){
        if(vmType.equalsIgnoreCase("ccc") || vmType.equalsIgnoreCase("cloud"))
            return new WanNetworkDelay();
        else if(vmType.equalsIgnoreCase("hles"))
            return new WlanNetworkDelay();
        else if(vmType.equalsIgnoreCase("gles"))
            return new NoNetworkDelay();
        else
            return null;
    }
}
