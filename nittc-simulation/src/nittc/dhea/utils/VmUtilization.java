package utils;

public class VmUtilization {
    public String cpu;
    public String ram;
    public String bandwidth;

    @Override
    public String toString() {
        //return "CPU = " + cpu + ", RAM = " + ram + ", Bandwidth = " + bandwidth;
        return cpu + "," + ram + "," + bandwidth;
    }

    public String getBandwidth(){
        return bandwidth;
    }
}
