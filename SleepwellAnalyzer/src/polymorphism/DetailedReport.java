package polymorphism;
import encapsulation.WeeklySleepLog;
import encapsulation.DailySleepRecord;
import java.util.List;

public class DetailedReport extends ReportGenerator {
    private static final String RESET = "\033[0m";
    private static final String PRIMARY_DARK = "\033[38;5;27m";
    private static final String PRIMARY_MID = "\033[38;5;45m";
    private static final String ACCENT_SUCCESS = "\033[38;5;46m";
    private static final String ACCENT_WARNING = "\033[38;5;220m";
    private static final String TEXT_BRIGHT = "\033[38;5;255m";
    private static final String TEXT_NORMAL = "\033[38;5;253m";
    private static final String TEXT_SOFT = "\033[38;5;250m";
    
    public DetailedReport(WeeklySleepLog log) {
        super(log);
    }
    
    @Override
    public void generateReport() {
        System.out.println("\n" + centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "  DETAILED WEEKLY REPORT" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Week: " + TEXT_BRIGHT + log.getWeekLabel() + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Starting: " + TEXT_BRIGHT + log.getWeekStartDate() + RESET, 100));
        System.out.println();
        
        System.out.println(centerText(PRIMARY_MID + "Daily Sleep Records:" + RESET, 100));
        System.out.println();
        for (DailySleepRecord record : log.getSleepRecords()) {
            System.out.println(centerText(TEXT_NORMAL + record.toString() + RESET, 100));
        }
        
        System.out.println("\n" + centerText(PRIMARY_MID + "Statistics:" + RESET, 100));
        System.out.println(centerText(TEXT_NORMAL + "Average Sleep: " + TEXT_BRIGHT + String.format("%.2f", log.calculateAverageSleepHours()) + " hours" + RESET, 100));
        System.out.println(centerText(TEXT_NORMAL + "Average Quality: " + TEXT_BRIGHT + String.format("%.2f", log.calculateAverageSleepQuality()) + "/5" + RESET, 100));
        
        DailySleepRecord best = log.getBestSleepDay();
        DailySleepRecord worst = log.getWorstSleepDay();
        if (best != null) {
            System.out.println(centerText(ACCENT_SUCCESS + "  Best: " + best.getDay() + " (" + best.getSleepHours() + "h)" + RESET, 100));
        }
        if (worst != null) {
            System.out.println(centerText(ACCENT_WARNING + "⚠ Worst: " + worst.getDay() + " (" + worst.getSleepHours() + "h)" + RESET, 100));
        }
        
        System.out.println("\n" + centerText(PRIMARY_MID + "Weekday Analysis:" + RESET, 100));
        String weekdayAnalysis = log.getWeekdayAnalysis();
        for (String line : weekdayAnalysis.split("\n")) {
            System.out.println(centerText(TEXT_NORMAL + line + RESET, 100));
        }
        
        System.out.println("\n" + centerText(PRIMARY_MID + "Weekend Analysis:" + RESET, 100));
        String weekendAnalysis = log.getWeekendAnalysis();
        for (String line : weekendAnalysis.split("\n")) {
            System.out.println(centerText(TEXT_NORMAL + line + RESET, 100));
        }
        
        List<String> patterns = log.detectIrregularPatterns();
        if (!patterns.isEmpty()) {
            System.out.println("\n" + centerText(ACCENT_WARNING + "⚠ Irregular Patterns:" + RESET, 100));
            for (String pattern : patterns) {
                System.out.println(centerText(ACCENT_WARNING + "• " + pattern + RESET, 100));
            }
        }
    }
    
    @Override
    public String getReportFormat() {
        return "Detailed Report";
    }
    
    private static String centerText(String text, int width) {
        String plainText = text.replaceAll("\033\\[[;\\d]*m", "");
        int padding = (width - plainText.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text;
    }
}