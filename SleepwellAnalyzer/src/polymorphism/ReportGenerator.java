package polymorphism;
import encapsulation.WeeklySleepLog;

public abstract class ReportGenerator {
    protected WeeklySleepLog log;
    
    public ReportGenerator(WeeklySleepLog log) {
        this.log = log;
    }
    
    public abstract void generateReport();
    public abstract String getReportFormat();
}