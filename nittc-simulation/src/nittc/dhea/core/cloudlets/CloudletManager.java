package nittc.dhea.core.cloudlets;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;

public interface CloudletManager {
    void setDatacenterBroker(DatacenterBroker broker);
    void setServerTier(String vmType);
    void createAndSubmitCloudlets();
}
