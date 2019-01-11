/*
 * CloudSim Plus: A modern, highly-extensible and easier-to-use Framework for
 * Modeling and Simulation of Cloud Computing Infrastructures and Services.
 * http://cloudsimplus.org
 *
 *     Copyright (C) 2015-2018 Universidade da Beira Interior (UBI, Portugal) and
 *     the Instituto Federal de Educação Ciência e Tecnologia do Tocantins (IFTO, Brazil).
 *
 *     This file is part of CloudSim Plus.
 *
 *     CloudSim Plus is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     CloudSim Plus is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with CloudSim Plus. If not, see <http://www.gnu.org/licenses/>.
 */
package nittc.dhea.scenarios;

import ch.qos.logback.classic.Level;
import nittc.dhea.core.cloudlets.CloudletManager;
import nittc.dhea.core.cloudlets.DheaCloudlet;
import nittc.dhea.formatters.DheaResultTableBuilder;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudsimplus.util.Log;
import nittc.dhea.managers.DatacenterManager;
import nittc.dhea.managers.PrototypeSensorCloudletManager;
import nittc.dhea.managers.VirtualMachineManager;

import java.text.DecimalFormat;
import java.util.*;

/**
 * An example showing how to delay the submission of cloudlets.
 * Although there is enough resources to run all cloudlets simultaneously,
 * the example delays the creation and execution of some cloudlets inside a VM,
 * simulating the dynamic arrival of cloudlets.
 * It first creates a set of cloudlets without delay and
 * another set of cloudlets all with the same submission delay.
 *
 * @author Manoel Campos da Silva Filho
 * @since CloudSim Plus 1.0
 */
public class DheaHomeAutomation {
    /**
     * Number of Processor Elements (CPU Cores) of each Host.
     */
    private static final int HOST_PES_NUMBER = 8;

    /**
     * Number of Processor Elements (CPU Cores) of each VM and cloudlet.
     */
    private static final int VM_PES_NUMBER = HOST_PES_NUMBER;

    /**
     * Number of Cloudlets to create simultaneously.
     * Other cloudlets will be enqueued.
     */
    private static final int NUMBER_OF_CLOUDLETS = VM_PES_NUMBER*2;

    private static final double MIN_TIME_BETWEEN_EVENTS = 0.01;

    private final DatacenterBroker broker;
    private final CloudSim simulation;
    private final double simulationTimeLimit;
    private HashMap<Double, Integer> numberOfCloudlets;
    private final String vm;
    private final String scheduler;
    private DatacenterManager datacenterManager;
    private VirtualMachineManager virtualMachineManager;
    private CloudletManager cloudletManager;
    private DecimalFormat decimalFormat;
    private String simulationName;
    private String simulationDescription;

    /**
     * Starts the example execution, calling the class constructor\
     * to build and run the simulation.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        Log.setLevel(Level.OFF);

        List<DheaHomeAutomation> simulationList = new ArrayList<>();

        int[] numbers = {26,44,31,24,21,45,14,29,57,46,48,20,40,17,36,10,28,54,10,23,45,21,39,22,4,37,60,47,24,36};


        String[] vmList = {"CCC", "HLES", "GLES"};
        String[] schedulerList = {"CompletelyFair", "SpaceShared", "SpaceSharedDropTimeOut"};
        int[] numberOfCloudletsPerSecond = {1,2,4,6,8,10,12,14,16,18,20,24,28,30,32};


        for(int number : numberOfCloudletsPerSecond){
            Random random = new Random();
            HashMap<Double, Integer> numberOfCloudlets = new LinkedHashMap<>();
            for(double time = 1; time <= 30; time++){
                int rangeMin = 1;
                int rangeMax = 10;
                int randomNumberOfCloudlets = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
                //numberOfCloudlets.put(time, randomNumberOfCloudlets);
                //numberOfCloudlets.put(time, numbers[(int)time - 1]);)
                numberOfCloudlets.put(time, number);
            }
            for(String scheduler : schedulerList){
                for(String vm : vmList){
                    simulationList.add(
                        new DheaHomeAutomation(30, numberOfCloudlets, vm, scheduler)
                    );
                }
            }
        }

        final long startTimeMilliSec = System.currentTimeMillis();

        simulationList.parallelStream().forEach(DheaHomeAutomation::run);
        simulationList.forEach(DheaHomeAutomation::printResults);

        final long finishTimeMilliSec = System.currentTimeMillis() - startTimeMilliSec;
        System.out.println("=====================================");
        System.out.printf("Time to run %d simulations: %d milliseconds\n", simulationList.size(), finishTimeMilliSec);
    }

    /**
     * Default constructor that builds and starts the simulation.
     */
    public DheaHomeAutomation(double timeLimit, HashMap numberOfCloudlets, String vm, String scheduler) {
        simulation = new CloudSim(MIN_TIME_BETWEEN_EVENTS);
        broker     = new DatacenterBrokerSimple(simulation);

        this.simulationTimeLimit = timeLimit;
        this.numberOfCloudlets = numberOfCloudlets;
        this.vm = vm;
        this.scheduler = scheduler;

        initialize();
    }

    private void initialize() {
        simulationName = "Object Recognition " + scheduler + " on " + vm;
        simulationDescription = "Process Object Recognition on Cloud, Cloud VM using SpaceSharedScheduler.";

        decimalFormat = new DecimalFormat("####.####");
    }

    private void run() {
        System.out.println("Starting " + simulationName);
        datacenterManager = new DatacenterManager();
        datacenterManager.setSimulation(simulation);
        datacenterManager.createDatacenters();

        virtualMachineManager = new VirtualMachineManager();
        virtualMachineManager.setDatacenterBroker(broker);
        virtualMachineManager.setScheduler(scheduler);
        virtualMachineManager.createVirtualMachines();
        virtualMachineManager.submitVirtualMachines();

        cloudletManager = new PrototypeSensorCloudletManager(virtualMachineManager.getVm(vm), simulationTimeLimit, numberOfCloudlets);
        cloudletManager.setDatacenterBroker(broker);
        cloudletManager.setServerTier(vm);
        cloudletManager.createAndSubmitCloudlets();

        simulation.start();
    }

    private void printResults() {
        //new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();
        //new DheaResultTableBuilder(broker.getCloudletCreatedList()).build();
        //new CloudletsTableBuilder(broker.getCloudletCreatedList()).build();

        List<Cloudlet> cloudletFinishedList = broker.getCloudletFinishedList();
        List<Cloudlet> cloudletCreatedList = broker.getCloudletCreatedList();

        System.out.println("=====================================");
        System.out.println("VM Type = " + vm);
        System.out.println("CloudletScheduler: " + scheduler);
        System.out.println("No. of Cloudlets = " + numberOfCloudlets.get(1.0));
        //System.out.println("-------------------------------------");

        /**
         * Average Network Delay
         */
        double sumOfNetworkDelay = 0;

        /**
         * Average Execution Time
         */
        double sumOfExecutionTime = 0;

        /**
         * Average Total Time
         */
        double sumOfTotalTime = 0;

        for(Cloudlet cloudlet : cloudletFinishedList){
            sumOfNetworkDelay += (cloudlet.getSubmissionDelay() - ((DheaCloudlet) cloudlet).getGeneratedTime());
            sumOfExecutionTime += cloudlet.getActualCpuTime();
            sumOfTotalTime += (cloudlet.getFinishTime() - ((DheaCloudlet) cloudlet).getGeneratedTime());
        }

        double avgOfNetworkDelay = sumOfNetworkDelay / cloudletFinishedList.size();
        System.out.println("Average Network Delay = " + decimalFormat.format(avgOfNetworkDelay));

        double avgOfExecutionTime = sumOfExecutionTime / cloudletFinishedList.size();
        System.out.println("Average Execution Time = " + decimalFormat.format(avgOfExecutionTime));

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
