package nittc.dhea.services.cloudlets;

import nittc.dhea.core.cloudlets.CloudletService;
import nittc.dhea.core.cloudlets.DheaCloudlet;
import nittc.dhea.core.utilizationmodels.UtilizationModelAbsolute;
import nittc.dhea.core.utilizationmodels.UtilizationModelRelative;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrototypeSensorCloudletService extends CloudletService {

    private double timeLimit;
    private HashMap numberOfCloudlets;

    public void setTimeLimit(double time){
        timeLimit = time;
    }

    public void setNumberOfCloudlets(HashMap numberOfCloudletsPerSecond){
        numberOfCloudlets = numberOfCloudletsPerSecond;
    }

    public Integer getNumberOfCloudlets(Object time){
        return (Integer) numberOfCloudlets.get(time);
    }

    public void createAndSubmitCloudlets(Vm vm) {
        long fileSize = 10;
        long outputSize = 10;
        long length = 20; //in number of Million Instructions (MI)
        int pesNumber = 1;

        long cloudletId = 1;

        for(double time = 1; time <= timeLimit; time++){
            for(int i = 0; i < getNumberOfCloudlets(time); i++){
                List<Cloudlet> list = new ArrayList<>();
                DheaCloudlet cloudlet = (DheaCloudlet) new DheaCloudlet(cloudletId++, length, pesNumber)
                    .setFileSize(fileSize)
                    .setOutputSize(outputSize)
                    .setUtilizationModelCpu(new UtilizationModelRelative(1))
                    .setUtilizationModelRam(new UtilizationModelAbsolute(32))
                    .setUtilizationModelBw(new UtilizationModelAbsolute(0.008))
                    .setVm(vm);

                double randomGeneratedTime = getRandomGeneratedDelay();
                cloudlet.setGeneratedTime(time + randomGeneratedTime);

                list.add(cloudlet);
                getDatacenterBroker().submitCloudletList(list, time + randomGeneratedTime + getNetworkDelay().setLength(0.008).getTotalDelay());
                //getDatacenterBroker().submitCloudletList(list, time);
            }
        }
    }
}
