package nittc.dhea.formatters;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.List;

public class SaveResultToDatabase {

    private Connection mysqlConnection;
    private String databaseUrl;
    private String databaseName;

    public SaveResultToDatabase(){
        setDatabase();
        try{
            // create a mysql database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            mysqlConnection = DriverManager.getConnection(getDatabase(), "root", "");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDatabase(){
        databaseUrl = "jdbc:mysql://localhost/";
        databaseName = "dhea";
    }

    private String getDatabase(){
        return databaseUrl + databaseName;
    }

    private int insertCase(String name, String description) {
        String sql = "insert into simulation_cases (name, description) values (?, ?)";
        try{
            PreparedStatement statement = mysqlConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, description);
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private void insertResults(int caseId, List<Cloudlet> cloudlets) {
        String sql = "insert into simulation_results (case_id, cloudlet_id, cloudlet_status, datacenter_id, " +
            "host_id, host_pes, vm_id, vm_pes, cloudlet_length, cloudlet_pes, " +
            "submission_time, start_time, execution_time, finish_time, total_time) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement statement = mysqlConnection.prepareStatement(sql);
            int totalRows = 0;
            for(Cloudlet cloudlet : cloudlets){
                totalRows++;
                statement.setString(1, String.valueOf(caseId));
                statement.setString(2, String.valueOf(cloudlet.getId()) );
                statement.setString(3, String.valueOf(cloudlet.getStatus()));
                statement.setString(4, String.valueOf(cloudlet.getVm().getHost().getDatacenter().getId()));
                statement.setString(5, String.valueOf(cloudlet.getVm().getHost().getId()));
                statement.setString(6, String.valueOf(cloudlet.getVm().getHost().getNumberOfWorkingPes()));
                statement.setString(7, String.valueOf(cloudlet.getVm().getId()));
                statement.setString(8, String.valueOf(cloudlet.getVm().getNumberOfPes()));
                statement.setString(9, String.valueOf(cloudlet.getLength()));
                statement.setString(10, String.valueOf(cloudlet.getNumberOfPes()));
                statement.setString(11, String.valueOf(new DecimalFormat().format(cloudlet.getSubmissionDelay())));
                statement.setString(12, String.valueOf(new DecimalFormat().format(cloudlet.getExecStartTime())));
                statement.setString(13, String.valueOf(new DecimalFormat().format(cloudlet.getActualCpuTime())));
                statement.setString(14, String.valueOf(new DecimalFormat().format(cloudlet.getFinishTime())));
                statement.setString(15, String.valueOf(new DecimalFormat().format(cloudlet.getFinishTime() - cloudlet.getSubmissionDelay())));
                statement.addBatch();
                if(totalRows % 1000 == 0 || totalRows == cloudlets.size()){
                    statement.executeBatch();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void execute(String name, String description, List<Cloudlet> cloudlets){
        int caseId = insertCase(name, description);
        insertResults(caseId, cloudlets);
        System.out.println("Simulation result in chart formatted is available at http://localhost:8200/case/" + caseId);
    }
}
