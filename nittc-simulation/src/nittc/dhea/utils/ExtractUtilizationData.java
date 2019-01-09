package utils;

import java.io.File;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.*;

public class ExtractUtilizationData {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(new File("/Users/babebbu/Tsuruoka/Projects/nittc-cloudsim-plus/nittc/output/utilization.raw"));
        PrintStream out = new PrintStream(new File("/Users/babebbu/Tsuruoka/Projects/nittc-cloudsim-plus/nittc/output/utilization-30fps.csv"));

        Integer time;
        String vmId;

        HashMap<String, HashMap> utilization = new LinkedHashMap<>();

        int i = 0;

        while(input.hasNextLine()){
            //System.out.print(i++ + " ");
            String line = input.nextLine();
            String[] values = line.split(" ");
            time = (int) Double.parseDouble(values[1]);
            System.out.print(values[1] + " => " + time + " ");
            vmId = values[2];

            DecimalFormat dcf = new DecimalFormat("##.##");

            VmUtilization vmUtilization = new VmUtilization();
            vmUtilization.cpu = dcf.format(Double.parseDouble(values[3]));
            vmUtilization.ram = values[4];
            vmUtilization.bandwidth = values[5];

            if(utilization.get(vmId) == null) {
                utilization.put(vmId, new LinkedHashMap<>());
                i = 0;

                System.out.print("- new LinkedHashMap ");
            }

            if(utilization.get(vmId).get(time) == null){
                utilization.get(vmId).put(time, vmUtilization);
                i = 0;
                i++;
                System.out.print("- First Value ");
            }
            else if(i++ %8 == 0){
                VmUtilization vmu = (VmUtilization) utilization.get(vmId).get(time);
                Double ram = Double.parseDouble(vmUtilization.ram) + Double.parseDouble(vmu.ram);
                vmu.ram = ram.toString();
                Double bandwidth = Double.parseDouble(vmUtilization.bandwidth) + Double.parseDouble(vmu.bandwidth);
                vmu.bandwidth = bandwidth.toString();
                utilization.get(vmId).put(time, vmu);
                System.out.print("- Add Value ");
            }
            System.out.println("- Value of i = " + (i-1));
        }

        utilization.forEach((id, vmUtilization) -> {
            //out.println("VM ID: " + id);
            vmUtilization.forEach((second, data) -> {
                try {
                    out.println(second + "," + data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            System.out.println("------------------------------");
        });
    }
}
