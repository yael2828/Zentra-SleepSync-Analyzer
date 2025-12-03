package inheritance;
import abstraction.SleepAnalyzer;

public class WeekendAnalyzer extends SleepAnalyzer {
    
    public WeekendAnalyzer() {
        super("Weekend");
    }
    
    @Override
    public String analyzePattern(double avgHours) {
        if (avgHours >= 7 && avgHours <= 10) {
            return "Great weekend rest!";
        } else if (avgHours > 10) {
            return "You're catching up on sleep during weekends.";
        } else {
            return "Even on weekends, you need more rest.";
        }
    }
    
    @Override
    public String getRecommendation(double avgHours) {
        if (avgHours > 10) {
            return "Oversleeping on weekends might indicate weekday sleep debt.";
        } else if (avgHours < 7) {
            return "Use weekends to catch up on rest and maintain consistency.";
        }
        return "Your weekend sleep pattern looks healthy!";
    }
}