package nittc.dhea.factories;

import nittc.dhea.core.networks.NetworkDelay;
import nittc.dhea.core.networks.delay.NoNetworkDelay;
import nittc.dhea.core.networks.delay.WanNetworkDelay;
import nittc.dhea.core.networks.delay.WlanNetworkDelay;

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
