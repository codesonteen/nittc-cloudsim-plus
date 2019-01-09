package nittc.dhea.scenarios;

import core.cloudlets.CloudletManager;
import core.cloudlets.DheaCloudlet;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudsimplus.util.Log;
import scenarios.dhea.formatters.DheaResultTableBuilder;
import scenarios.dhea.managers.SmartScarecrowCloudletManager;
import scenarios.dhea.managers.DatacenterManager;
import scenarios.dhea.managers.VirtualMachineManager;
import ch.qos.logback.classic.Level;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DheaSmartScarecrow {

    private String simulationName;
    private String simulationDescription;

    private CloudSim simulation;
    private DatacenterBroker broker;

    private DatacenterManager datacenterManager;
    private VirtualMachineManager virtualMachineManager;
    private CloudletManager cloudletManager;

    private double simulationTimeLimit;
    private int numberOfCloudlets;
    private String vm;
    private String scheduler;

    private DecimalFormat decimalFormat;

    public static void main(String[] args) {
        Log.setLevel(Level.OFF);

        List<DheaSmartScarecrow> simulationList = new ArrayList<>();

        int[] numberOfCloudletsList = {2};
        String[] vmList = {"CCC", "HLES", "GLES"};
        String[] schedulerList = {"SpaceShared"};

        for(String scheduler : schedulerList){
            for(String vm : vmList){
                for(int numberOfCloudlets : numberOfCloudletsList){
                    simulationList.add(
                        new DheaSmartScarecrow(30, numberOfCloudlets, vm, scheduler)
                    );
                }
            }
        }

        final long startTimeMilliSec = System.currentTimeMillis();

        simulationList.parallelStream().forEach(DheaSmartScarecrow::run);
        simulationList.forEach(DheaSmartScarecrow::printResults);

        final long finishTimeMilliSec = System.currentTimeMillis() - startTimeMilliSec;
        System.out.println("=====================================");
        System.out.printf("Time to run %d simulations: %d milliseconds\n", simulationList.size(), finishTimeMilliSec);
    }

    private void initialize() {
        simulationName = "Object Recognition SpaceShared on Cloud";
        simulationDescription = "Process Object Recognition on Cloud, Cloud VM using SpaceSharedScheduler.";

        decimalFormat = new DecimalFormat();
    }

    private DheaSmartScarecrow(double timeLimit, int numberOfCloudlets, String vm, String scheduler) {
        initialize();

        simulation = new CloudSim();
        broker = new DatacenterBrokerSimple(simulation);

        simulationTimeLimit = timeLimit;
        this.numberOfCloudlets = numberOfCloudlets;
        this.vm = vm;
        this.scheduler = scheduler;
    }

    private void run(){
        datacenterManager = new DatacenterManager();
        datacenterManager.setSimulation(simulation);
        datacenterManager.createDatacenters();

        virtualMachineManager = new VirtualMachineManager();
        virtualMachineManager.setDatacenterBroker(broker);
        virtualMachineManager.setScheduler(scheduler);
        virtualMachineManager.createVirtualMachines();
        virtualMachineManager.submitVirtualMachines();

        cloudletManager = new SmartScarecrowCloudletManager(virtualMachineManager.getVm(vm), simulationTimeLimit, numberOfCloudlets);
        cloudletManager.setDatacenterBroker(broker);
        cloudletManager.setServerTier(vm);
        cloudletManager.createAndSubmitCloudlets();

        simulation.start();
    }

    private void printResults() {
        //new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();
        new DheaResultTableBuilder(broker.getCloudletFinishedList()).build();
        //new CloudletsTableBuilder(broker.getCloudletCreatedList()).build();

        List<Cloudlet> cloudletFinishedList = broker.getCloudletFinishedList();
        List<Cloudlet> cloudletCreatedList = broker.getCloudletCreatedList();

        System.out.println("=====================================");
        System.out.println("VM Type = " + vm);
        System.out.println("No. of Cloudlets = " + numberOfCloudlets);
        System.out.println("-------------------------------------");

        /**
         * Average Execution Time
         */
        double sumOfExecutionTime = 0;
        for(Cloudlet cloudlet : cloudletFinishedList){
            sumOfExecutionTime += cloudlet.getActualCpuTime();
        }
        double avgOfExecutionTime = sumOfExecutionTime / cloudletFinishedList.size();
        System.out.println("Average Execution Time = " + decimalFormat.format(avgOfExecutionTime));

        /**
         * Average Total Time
         */
        double sumOfTotalTime = 0;
        for(Cloudlet cloudlet : cloudletFinishedList){
            sumOfTotalTime += (cloudlet.getFinishTime() - ((DheaCloudlet) cloudlet).getGeneratedTime());
        }
        double avgOfTotalTime = sumOfTotalTime / cloudletFinishedList.size();
        System.out.println("Average Total Time = " + decimalFormat.format(avgOfTotalTime));

        /**
         * Fail Percentage
         */
        int numberOfFailedCloudlets = cloudletCreatedList.size() - cloudletFinishedList.size();
        //System.out.println("Total Created Cloudlets = " + cloudletCreatedList.size());
        //System.out.println("Total Success Cloudlets = " + cloudletFinishedList.size());
        //System.out.println("Total Failed Cloudlets  = " + numberOfFailedCloudlets);
        double failPercentage = 100.0 * numberOfFailedCloudlets / cloudletCreatedList.size();
        System.out.println("Fail Percentage = " + decimalFormat.format(failPercentage) + "%" + " (" + cloudletFinishedList.size() + "/" + cloudletCreatedList.size() + ")");

        //new CsvBuilder().build(broker.getCloudletFinishedList());
        //new SaveResultToDatabase().execute(simulationName, simulationDescription, broker.getCloudletFinishedList());
    }
}
