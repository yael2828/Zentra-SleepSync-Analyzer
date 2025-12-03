package encapsulation;
import java.util.*;
import inheritance.WeekdayAnalyzer;
import inheritance.WeekendAnalyzer;

public class WeeklySleepLog {
    private final String weekStartDate;
    private final String weekLabel;
    private final List<DailySleepRecord> sleepRecords;
    
    public WeeklySleepLog(String weekStartDate, String weekLabel) {
        this.weekStartDate = weekStartDate;
        this.weekLabel = weekLabel;
        this.sleepRecords = new ArrayList<>();
    }
    
    public void addDailyRecord(DailySleepRecord record) {
        sleepRecords.add(record);
    }
    
    public double calculateAverageSleepHours() {
        if (sleepRecords.isEmpty()) return 0.0;
        double total = 0;
        for (DailySleepRecord record : sleepRecords) {
            total += record.getSleepHours();
        }
        return total / sleepRecords.size();
    }
    
    public double calculateAverageSleepQuality() {
        if (sleepRecords.isEmpty()) return 0.0;
        double total = 0;
        for (DailySleepRecord record : sleepRecords) {
            total += record.getSleepQuality();
        }
        return total / sleepRecords.size();
    }
    
    public DailySleepRecord getBestSleepDay() {
        if (sleepRecords.isEmpty()) return null;
        DailySleepRecord best = sleepRecords.get(0);
        for (DailySleepRecord record : sleepRecords) {
            if (record.getSleepHours() > best.getSleepHours()) {
                best = record;
            }
        }
        return best;
    }
    
    public DailySleepRecord getWorstSleepDay() {
        if (sleepRecords.isEmpty()) return null;
        DailySleepRecord worst = sleepRecords.get(0);
        for (DailySleepRecord record : sleepRecords) {
            if (record.getSleepHours() < worst.getSleepHours()) {
                worst = record;
            }
        }
        return worst;
    }
    
    public List<String> detectIrregularPatterns() {
        List<String> patterns = new ArrayList<>();
        
        int consecutiveLowSleep = 0;
        for (DailySleepRecord record : sleepRecords) {
            if (record.getSleepHours() < 6) {
                consecutiveLowSleep++;
            } else {
                consecutiveLowSleep = 0;
            }
            
            if (consecutiveLowSleep >= 3) {
                patterns.add("Warning: 3+ consecutive days with less than 6 hours of sleep");
                break;
            }
        }
        
        double avg = calculateAverageSleepHours();
        for (DailySleepRecord record : sleepRecords) {
            if (Math.abs(record.getSleepHours() - avg) > 3) {
                patterns.add("High sleep variability detected on " + record.getDay());
            }
        }
        
        return patterns;
    }
    
    public String getWeekdayAnalysis() {
        WeekdayAnalyzer analyzer = new WeekdayAnalyzer();
        double weekdayAvg = 0;
        int count = 0;
        
        for (DailySleepRecord record : sleepRecords) {
            String day = record.getDay();
            if (!day.equals("Saturday") && !day.equals("Sunday")) {
                weekdayAvg += record.getSleepHours();
                count++;
            }
        }
        
        if (count > 0) {
            weekdayAvg /= count;
            return analyzer.analyzePattern(weekdayAvg) + "\n" + analyzer.getRecommendation(weekdayAvg);
        }
        return "No weekday data available.";
    }
    
    public String getWeekendAnalysis() {
        WeekendAnalyzer analyzer = new WeekendAnalyzer();
        double weekendAvg = 0;
        int count = 0;
        
        for (DailySleepRecord record : sleepRecords) {
            String day = record.getDay();
            if (day.equals("Saturday") || day.equals("Sunday")) {
                weekendAvg += record.getSleepHours();
                count++;
            }
        }
        
        if (count > 0) {
            weekendAvg /= count;
            return analyzer.analyzePattern(weekendAvg) + "\n" + analyzer.getRecommendation(weekendAvg);
        }
        return "No weekend data available.";
    }
    
    public List<DailySleepRecord> getSleepRecords() {
        return sleepRecords;
    }
    
    public String getWeekStartDate() {
        return weekStartDate;
    }
    
    public String getWeekLabel() {
        return weekLabel;
    }
}
