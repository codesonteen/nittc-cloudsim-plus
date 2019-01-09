package nittc.dhea.core.networks;

import java.text.DecimalFormat;

public class NetworkDelay {

    private double length;
    private double rate;

    public NetworkDelay setLength(double length){
        this.length = length;
        return this;
    }

    public void setRate(double rate){
        this.rate = rate;
    }

    /**
     * Get Propagation Delay (Time elapsed sending data from end point of the medium to another end point of the medium)
     * @param distance meters unit
     * @return time
     */
    public double getPropogationDelay(double distance){
        final double speed = 3 * Math.pow(10, 8); // meters/second
        return distance / speed; // Time (second)
    }

    /**
     * Get Transmission Delay (Time elapsed when the router push out the packet)
     * @return time
     */
    public double getTransmissionDelay(){
        if(rate == 0){
            throw new IllegalArgumentException("Rate can not be zero. Divide by zero cause infinite delay");
        }
        return Double.parseDouble(new DecimalFormat("####.##").format(length / rate));
    }

    /**
     * Get Queueing Delay
     * queueing: (a * L)/ R; a => no. of arrivalPacketsPerSecond, L => Length of packet, R =>
     * @return time
     */
    public double getQueueingDelay(){
        return 0;
    }

    /**
     * Get Processing Delay
     * @return time
     */
    public double getProcessingDelay(){
        return 0;
    }

    public Double getTotalDelay(){
        return Double.parseDouble(new DecimalFormat("####.####").format(getPropogationDelay(0) + getTransmissionDelay() + getQueueingDelay() + getProcessingDelay()));
    }

    public String toString(){
        return getTotalDelay().toString();
    }
}
