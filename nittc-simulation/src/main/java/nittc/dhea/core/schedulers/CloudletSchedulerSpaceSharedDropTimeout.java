package nittc.dhea.core.schedulers;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletExecution;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.vms.Vm;

public class CloudletSchedulerSpaceSharedDropTimeout extends CloudletSchedulerSpaceShared {
    protected boolean canExecuteCloudletInternal(final CloudletExecution cloudlet) {
        Vm vm = getVm();
        // Check for Network usage before execute cloudlet
        //Logger LOGGER = LoggerFactory.getLogger(DatacenterBroker.class.getSimpleName());
        //LOGGER.info("{}: Current Requested Bandwidth on VM {} = {}", vm.getHost().getSimulation().clock(), vm.getId(), vm.getCurrentRequestedBw());
        return isTimeeout(cloudlet);
        //return isThereEnoughFreePesForCloudlet(cloudlet);
    }

    protected boolean isTimeeout(CloudletExecution cle){
        double simulationCurrentTime = cle.getCloudlet().getVm().getSimulation().clock();
        double cloudletArrivalTime = cle.getCloudlet().getLastDatacenterArrivalTime();
        double timeOut = 30.0;

        if(simulationCurrentTime - cloudletArrivalTime < timeOut){
            return isThereEnoughFreePesForCloudlet(cle);
        }
        else{
            cle.setStatus(Cloudlet.Status.CANCELED);
            return false;
        }
    }
}
