package inheritance;
import abstraction.SleepAnalyzer;

public class WeekdayAnalyzer extends SleepAnalyzer {
    
    public WeekdayAnalyzer() {
        super("Weekday");
    }
    
    @Override
    public String analyzePattern(double avgHours) {
        if (avgHours >= 7 && avgHours <= 9) {
            return "Excellent weekday sleep pattern!";
        } else if (avgHours >= 6 && avgHours < 7) {
            return "Good, but could use a bit more rest on weekdays.";
        } else if (avgHours > 9) {
            return "You're sleeping quite a lot on weekdays.";
        } else {
            return "Your weekday sleep is insufficient.";
        }
    }
    
    @Override
    public String getRecommendation(double avgHours) {
        if (avgHours < 7) {
            return "Try to get at least 7-9 hours of sleep on weekdays for optimal health.";
        } else if (avgHours > 9) {
            return "Consider if oversleeping might be affecting your productivity.";
        }
        return "Keep maintaining your healthy weekday sleep schedule!";
    }
}