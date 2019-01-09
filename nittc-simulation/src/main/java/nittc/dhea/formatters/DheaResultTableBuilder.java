package nittc.dhea.formatters;

import nittc.dhea.core.cloudlets.DheaCloudlet;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.Identifiable;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.builders.tables.TableColumn;

import java.util.List;

public class DheaResultTableBuilder extends CloudletsTableBuilder {

    private static final String TIME_FORMAT = "%.4f";
    private static final String SECONDS = "Seconds";
    private static final String CPU_CORES = "CPU cores";

    public DheaResultTableBuilder(final List<? extends Cloudlet> list){
        super(list);
    }

    @Override
    protected void createTableColumns() {
        final String ID = "ID";
        addColumnDataFunction(getTable().addColumn("Cloudlet", ID), Identifiable::getId);
        addColumnDataFunction(getTable().addColumn("Status "), cloudlet -> cloudlet.getStatus().name());
        addColumnDataFunction(getTable().addColumn("DC", ID), cloudlet -> cloudlet.getVm().getHost().getDatacenter().getId());
        addColumnDataFunction(getTable().addColumn("Host", ID), cloudlet -> cloudlet.getVm().getHost().getId());
        addColumnDataFunction(getTable().addColumn("Host PEs ", CPU_CORES), cloudlet -> cloudlet.getVm().getHost().getNumberOfWorkingPes());
        addColumnDataFunction(getTable().addColumn("VM", ID), cloudlet -> cloudlet.getVm().getId());
        addColumnDataFunction(getTable().addColumn("VM PEs   ", CPU_CORES), cloudlet -> cloudlet.getVm().getNumberOfPes());
        addColumnDataFunction(getTable().addColumn("CloudletLen", "MI"), Cloudlet::getLength);
        addColumnDataFunction(getTable().addColumn("CloudletPEs", CPU_CORES), Cloudlet::getNumberOfPes);

        TableColumn col = getTable().addColumn("Generated Time", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, cloudlet -> ((DheaCloudlet) cloudlet).getGeneratedTime());

        col = getTable().addColumn("Network Delay", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, cloudlet -> (cloudlet.getSubmissionDelay() - ((DheaCloudlet) cloudlet).getGeneratedTime()));

        col = getTable().addColumn("Arrival Time", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, cloudlet -> cloudlet.getSubmissionDelay());

        col = getTable().addColumn("StartTime", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, cloudlet -> cloudlet.getExecStartTime());

        col = getTable().addColumn("FinishTime", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, cloudlet -> cloudlet.getFinishTime());

        col = getTable().addColumn("ExecTime", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, cloudlet -> cloudlet.getActualCpuTime());

        col = getTable().addColumn("Total Time", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, cloudlet -> (cloudlet.getFinishTime() - ((DheaCloudlet) cloudlet).getGeneratedTime()));
    }
}
