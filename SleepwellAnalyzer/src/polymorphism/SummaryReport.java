package polymorphism;
import encapsulation.WeeklySleepLog;
import encapsulation.DailySleepRecord;

public class SummaryReport extends ReportGenerator {
    private static final String RESET = "\033[0m";
    private static final String PRIMARY_DARK = "\033[38;5;27m";
    private static final String PRIMARY_MID = "\033[38;5;45m";
    private static final String ACCENT_SUCCESS = "\033[38;5;46m";
    private static final String TEXT_BRIGHT = "\033[38;5;255m";
    private static final String TEXT_NORMAL = "\033[38;5;253m";
    private static final String TEXT_SOFT = "\033[38;5;250m";
    
    public SummaryReport(WeeklySleepLog log) {
        super(log);
    }
    
    @Override
    public void generateReport() {
        System.out.println("\n" + centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "📊 SUMMARY REPORT" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + log.getWeekLabel() + " (" + log.getWeekStartDate() + ")" + RESET, 100));
        System.out.println();
        
        double avgHours = log.calculateAverageSleepHours();
        double avgQuality = log.calculateAverageSleepQuality();
        
        System.out.println(centerText(PRIMARY_MID + "Key Metrics:" + RESET, 100));
        System.out.println(centerText(TEXT_NORMAL + "• Average Sleep: " + TEXT_BRIGHT + String.format("%.1f", avgHours) + " hours/night" + RESET, 100));
        System.out.println(centerText(TEXT_NORMAL + "• Average Quality: " + TEXT_BRIGHT + String.format("%.1f", avgQuality) + "/5 stars" + RESET, 100));
        
        DailySleepRecord best = log.getBestSleepDay();
        DailySleepRecord worst = log.getWorstSleepDay();
        
        if (best != null) {
            System.out.println(centerText(TEXT_NORMAL + "• Best Night: " + TEXT_BRIGHT + best.getDay() + " (" + best.getSleepHours() + "h)" + RESET, 100));
        }
        if (worst != null) {
            System.out.println(centerText(TEXT_NORMAL + "• Worst Night: " + TEXT_BRIGHT + worst.getDay() + " (" + worst.getSleepHours() + "h)" + RESET, 100));
        }
        
        System.out.println();
        
        String status;
        if (avgHours >= 7 && avgHours <= 9) {
            status = ACCENT_SUCCESS + "✓ Healthy Sleep Pattern" + RESET;
        } else if (avgHours < 7) {
            status = TEXT_NORMAL + "⚠ Sleep Debt Detected" + RESET;
        } else {
            status = TEXT_NORMAL + "ℹ Oversleeping Pattern" + RESET;
        }
        
        System.out.println(centerText(PRIMARY_MID + "Overall Status: " + status, 100));
    }
    
    @Override
    public String getReportFormat() {
        return "Summary Report";
    }
    
    private static String centerText(String text, int width) {
        String plainText = text.replaceAll("\033\\[[;\\d]*m", "");
        int padding = (width - plainText.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text;
    }
}