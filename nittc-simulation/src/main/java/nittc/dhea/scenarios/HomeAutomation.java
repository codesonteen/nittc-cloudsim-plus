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

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerCompletelyFair;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class HomeAutomation {
    /**
     * Number of Processor Elements (CPU Cores) of each Host.
     */
    private static final int HOST_PES_NUMBER = 4;

    /**
     * Number of Processor Elements (CPU Cores) of each VM and cloudlet.
     */
    private static final int VM_PES_NUMBER = HOST_PES_NUMBER;

    /**
     * Number of Cloudlets to create simultaneously.
     * Other cloudlets will be enqueued.
     */
    private static final int NUMBER_OF_CLOUDLETS = VM_PES_NUMBER*2;

    private final List<Host> hostList;
    private final List<Vm> vmList;
    private final List<Cloudlet> cloudletList;
    private final DatacenterBroker broker;
    private final Datacenter datacenter;
    private final CloudSim simulation;

    private static int cloudletId = 1;

    /**
     * Starts the example execution, calling the class constructor\
     * to build and run the simulation.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        new HomeAutomation();
    }

    /**
     * Default constructor that builds and starts the simulation.
     */
    public HomeAutomation() {
        /*Enables just some level of log messages.
          Make sure to import org.cloudsimplus.util.Log;*/
        Log.setLevel(ch.qos.logback.classic.Level.INFO);

        System.out.println("Starting " + getClass().getSimpleName());
        simulation = new CloudSim();

        this.hostList = new ArrayList<>();
        this.vmList = new ArrayList<>();
        this.cloudletList = new ArrayList<>();
        this.datacenter = createDatacenter();
        this.broker = new DatacenterBrokerSimple(simulation);

        Vm vm = createAndSubmitVmAndCloudlets();

        for(int i = 1; i <= 60; i++){
            createAndSubmitCloudlets(vm, i);
        }

        runSimulationAndPrintResults();
        System.out.println(getClass().getSimpleName() + " finished!");
        System.out.println(broker.getCloudletCreatedList().size() - broker.getCloudletFinishedList().size());
    }

    private void runSimulationAndPrintResults() {
        simulation.start();
        List<Cloudlet> cloudlets = broker.getCloudletSubmittedList();
        new CloudletsTableBuilder(cloudlets).build();
    }

    /**
     * Creates cloudlets and submit them to the broker, applying
     * a submission delay for each one (simulating the dynamic cloudlet arrival).
     *
     * @param vm Vm to run the cloudlets to be created
     * @param submissionDelay the delay the broker has to include when submitting the Cloudlets
     *
     */
    private void createAndSubmitCloudlets(Vm vm, double submissionDelay) {

        Random random = new Random();
        int rangeMin = 30;
        int rangeMax = 30;
        int randomNumberOfCloudlets = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        List<Cloudlet> list = new ArrayList<>(randomNumberOfCloudlets);

        System.out.println("=======================================");
        System.out.println("Schedule at second: " + submissionDelay);
        System.out.println("Number of random cloudlets = " + randomNumberOfCloudlets);
        System.out.println("---------------------------------------");

        for(int i = 0; i < randomNumberOfCloudlets; i++){
            Cloudlet cloudlet = createCloudlet(cloudletId++, vm, broker, submissionDelay);
            list.add(cloudlet);
        }


        broker.submitCloudletList(list, submissionDelay);
        cloudletList.addAll(list);
    }

    /**
     * Creates one Vm and a group of cloudlets to run inside it,
     * and submit the Vm and its cloudlets to the broker.
     * @return the created VM
     *
     * @see #createVm(int, DatacenterBroker)
     */
    private Vm createAndSubmitVmAndCloudlets() {
        List<Vm> list = new ArrayList<>();
        Vm vm = createVm(this.vmList.size(), broker);
        list.add(vm);

        broker.submitVmList(list);
        this.vmList.addAll(list);

        //Submit cloudlets without delay
        //createAndSubmitCloudlets(vm, 0);
        return vm;
    }

    /**
     * Creates a VM with pre-defined configuration.
     *
     * @param id the VM id
     * @param broker the broker that will be submit the VM
     * @return the created VM
     *
     */
    private Vm createVm(int id, DatacenterBroker broker) {
        int mips = 10000;
        long size = 10000; // image size (Megabyte)
        int ram = 4096; // vm memory (Megabyte)
        long bw = 1000;

        return new VmSimple(id, mips, VM_PES_NUMBER)
            .setRam(ram).setBw(bw).setSize(size)
            .setCloudletScheduler(new CloudletSchedulerCompletelyFair());
    }

    /**
     * Creates a cloudlet with pre-defined configuration.
     *
     * @param id Cloudlet id
     * @param vm vm to run the cloudlet
     * @param broker the broker that will submit the cloudlets
     * @return the created cloudlet
     */
    private Cloudlet createCloudlet(int id, Vm vm, DatacenterBroker broker, double time) {
        long fileSize = 300;
        long outputSize = 300;
        long length = 100; //in number of Million Instructions (MI)
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();
        System.out.println("Cloudlet #" + id);
        Cloudlet cloudlet = new CloudletSimple(id, length, pesNumber)
            .setFileSize(fileSize)
            .setOutputSize(outputSize)
            .setUtilizationModel(utilizationModel)
            .setVm(vm);

        double delay = time + getRandomGeneratedDelay();
        System.out.println("Time: " + delay);

        cloudlet.setSubmissionDelay(delay);

        return cloudlet;
    }

    /**
     * Creates a Datacenter with pre-defined configuration.
     *
     * @return the created Datacenter
     */
    private Datacenter createDatacenter() {
        Host host = createHost(0);
        hostList.add(host);
        return new DatacenterSimple(simulation, hostList, new VmAllocationPolicySimple());
    }

    /**
     * Creates a host with pre-defined configuration.
     *
     * @param id The Host id
     * @return the created host
     */
    private Host createHost(int id) {
        List<Pe> peList = new ArrayList<>();
        long mips = 10000;
        for(int i = 0; i < HOST_PES_NUMBER; i++){
            peList.add(new PeSimple(mips, new PeProvisionerSimple()));
        }
        long ram = 4096; // host memory (Megabyte)
        long storage = 1000000; // host storage (Megabyte)
        long bw = 10000; //Megabits/s

       return new HostSimple(ram, bw, storage, peList)
           .setRamProvisioner(new ResourceProvisionerSimple())
           .setBwProvisioner(new ResourceProvisionerSimple())
            .setVmScheduler(new VmSchedulerSpaceShared());

    }

    public double getRandomGeneratedDelay(){
        Random random = new Random();
        double rangeMin = 0.01;
        double rangeMax = 0.99;
        return Double.parseDouble(new DecimalFormat("####.##").format(rangeMin + (rangeMax - rangeMin) * random.nextDouble()));
    }
}