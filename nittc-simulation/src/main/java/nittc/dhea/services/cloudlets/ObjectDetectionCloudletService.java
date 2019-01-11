package nittc.dhea.services.cloudlets;

import nittc.dhea.core.cloudlets.CloudletService;
import nittc.dhea.core.cloudlets.DheaCloudlet;
import nittc.dhea.core.utilizationmodels.UtilizationModelAbsolute;
import nittc.dhea.core.utilizationmodels.UtilizationModelRelative;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.List;

public class ObjectDetectionCloudletService extends CloudletService {

    private double timeLimit;
    private int numberOfCloudlets;

    public void setTimeLimit(double time){
        timeLimit = time;
    }

    public void setNumberOfCloudlets(int number){
        numberOfCloudlets = number;
    }

    public void createAndSubmitCloudlets(Vm vm) {
        long fileSize = 10;
        long outputSize = 10;
        long length = 1400; //in number of Million Instructions (MI)
        int pesNumber = 1;

        long cloudletId = 1;

        for(double time = 1; time <= timeLimit; time++){
            for(int i = 0; i < numberOfCloudlets; i++){
                List<Cloudlet> list = new ArrayList<>();
                DheaCloudlet cloudlet = (DheaCloudlet) new DheaCloudlet(cloudletId++, length, pesNumber)
                    .setFileSize(fileSize)
                    .setOutputSize(outputSize)
                    .setUtilizationModelCpu(new UtilizationModelRelative(1))
                    .setUtilizationModelRam(new UtilizationModelAbsolute(128))
                    .setUtilizationModelBw(new UtilizationModelAbsolute(1000))
                    .setVm(vm);

                double randomGeneratedTime = getRandomGeneratedDelay();
                cloudlet.setGeneratedTime(time + randomGeneratedTime);

                list.add(cloudlet);
                getDatacenterBroker().submitCloudletList(list, time + randomGeneratedTime + getNetworkDelay().setLength(2.0).getTotalDelay());
            }
        }
    }
}
