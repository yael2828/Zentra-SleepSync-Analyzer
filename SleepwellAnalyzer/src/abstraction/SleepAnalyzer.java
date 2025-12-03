package abstraction;

public abstract class SleepAnalyzer {
    protected String dayType;
    
    public SleepAnalyzer(String dayType) {
        this.dayType = dayType;
    }
    
    public abstract String analyzePattern(double avgHours);
    public abstract String getRecommendation(double avgHours);
}