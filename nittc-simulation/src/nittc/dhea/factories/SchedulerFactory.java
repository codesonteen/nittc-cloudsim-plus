package nittc.dhea.scenarios.factories;

import nittc.dhea.core.schedulers.CloudletSchedulerSpaceSharedDropTimeout;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerCompletelyFair;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;

public class SchedulerFactory {
    public CloudletScheduler getScheduler(String name){
        if(name.equalsIgnoreCase("spaceshareddroptimeout"))
            return new CloudletSchedulerSpaceSharedDropTimeout();
        else if(name.equalsIgnoreCase("spaceshared"))
            return new CloudletSchedulerSpaceShared();
        else if(name.equalsIgnoreCase("timeshared"))
            return new CloudletSchedulerTimeShared();
        else if(name.equalsIgnoreCase("completelyshared"))
            return new CloudletSchedulerCompletelyFair();
        else
            return null;
    }
}
