package nittc.dhea.scenarios.formatters;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

public class CsvBuilder {
    public void build(List<Cloudlet> cloudlets) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("/Users/babebbu/Tsuruoka/Projects/nittc-cloudsim-plus/nittc/output/cloudlet_results/result_10_gles.csv"));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

            // Print Header Row
            csvPrinter.printRecord(
                "Cloudlet ID", "Status", "Datacenter ID", "Host ID", "Host PEs", "VM ID", "VM PEs",
                "Cloudlet Len", "Cloudlet PEs", "Submission Time", "Start Time", "FinishTime", "ExecTime", "Total Time"
            );

            // Print Data Rows
            for(Cloudlet cloudlet : cloudlets) {
                csvPrinter.printRecord(
                    cloudlet.getId(),
                    cloudlet.getStatus().name(),
                    cloudlet.getVm().getHost().getDatacenter().getId(),
                    cloudlet.getVm().getHost().getId(),
                    cloudlet.getVm().getHost().getNumberOfWorkingPes(),
                    cloudlet.getVm().getId(),
                    cloudlet.getVm().getNumberOfPes(),
                    cloudlet.getLength(),
                    cloudlet.getNumberOfPes(),
                    new DecimalFormat().format(cloudlet.getSubmissionDelay()),
                    new DecimalFormat().format(cloudlet.getExecStartTime()),
                    new DecimalFormat().format(cloudlet.getFinishTime()),
                    new DecimalFormat().format(cloudlet.getActualCpuTime()),
                    new DecimalFormat().format(cloudlet.getFinishTime() - cloudlet.getSubmissionDelay())
                );
            }
            csvPrinter.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
