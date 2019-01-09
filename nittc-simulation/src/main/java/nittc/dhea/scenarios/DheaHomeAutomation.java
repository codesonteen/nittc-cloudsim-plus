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
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.util.Log;
import nittc.dhea.managers.DatacenterManager;
import nittc.dhea.managers.PrototypeSensorSmartScarecrowCloudletManager;
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

    private final DatacenterBroker broker;
    private final CloudSim simulation;
    private final double simulationTimeLimit;
    private static HashMap<Double, Integer> numberOfCloudlets = new LinkedHashMap<>();
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
        for(double time = 1; time <= 30; time++){
            Random random = new Random();
            int rangeMin = 1;
            int rangeMax = 60;
            int randomNumberOfCloudlets = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
            //{1.0=26, 2.0=44, 3.0=31, 4.0=24, 5.0=21, 6.0=45, 7.0=14, 8.0=29, 9.0=57, 10.0=46, 11.0=48, 12.0=20,
            // 13.0=40, 14.0=17, 15.0=36, 16.0=10, 17.0=28, 18.0=54, 19.0=10, 20.0=23, 21.0=45, 22.0=21, 23.0=39,
            // 24.0=22, 25.0=4, 26.0=37, 27.0=60, 28.0=47, 29.0=24, 30.0=36}
            numberOfCloudlets.put(time, numbers[(int)time-1]);
        }


        String[] vmList = {"CCC", "HLES", "GLES"};
        String[] schedulerList = {"CompletelyFair"};

        for(String scheduler : schedulerList){
            for(String vm : vmList){
                simulationList.add(
                    new DheaHomeAutomation(30, numberOfCloudlets, vm, scheduler)
                );
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
        simulation = new CloudSim();
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

        decimalFormat = new DecimalFormat();
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

        cloudletManager = new PrototypeSensorSmartScarecrowCloudletManager(virtualMachineManager.getVm(vm), simulationTimeLimit, numberOfCloudlets);
        cloudletManager.setDatacenterBroker(broker);
        cloudletManager.setServerTier(vm);
        cloudletManager.createAndSubmitCloudlets();

        simulation.start();
    }

    private void printResults() {
        new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();
        new DheaResultTableBuilder(broker.getCloudletCreatedList()).build();
        //new CloudletsTableBuilder(broker.getCloudletCreatedList()).build();

        List<Cloudlet> cloudletFinishedList = broker.getCloudletFinishedList();
        List<Cloudlet> cloudletCreatedList = broker.getCloudletCreatedList();

        System.out.println("=====================================");
        System.out.println("VM Type = " + vm);
        System.out.println("CloudletScheduler: " + scheduler);
        System.out.println("No. of Cloudlets = " + numberOfCloudlets);
        //System.out.println("-------------------------------------");

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
