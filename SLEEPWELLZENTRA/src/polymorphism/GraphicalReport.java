package polymorphism;
import encapsulation.WeeklySleepLog;
import encapsulation.DailySleepRecord;

public class GraphicalReport extends ReportGenerator {
    private static final String RESET = "\033[0m";
    private static final String PRIMARY_DARK = "\033[38;5;27m";
    private static final String PRIMARY_MID = "\033[38;5;45m";
    private static final String PRIMARY_LIGHT = "\033[38;5;123m";
    private static final String ACCENT_SUCCESS = "\033[38;5;46m";
    private static final String TEXT_BRIGHT = "\033[38;5;255m";
    private static final String TEXT_NORMAL = "\033[38;5;253m";
    private static final String TEXT_SOFT = "\033[38;5;250m";
    
    public GraphicalReport(WeeklySleepLog log) {
        super(log);
    }
    
    @Override
    public void generateReport() {
        System.out.println("\n" + centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "  GRAPHICAL REPORT" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + log.getWeekLabel() + RESET, 100));
        System.out.println();
        
        System.out.println(centerText(PRIMARY_MID + "Sleep Hours Visualization:" + RESET, 100));
        System.out.println();
        
        int leftPadding = 35;
        
        for (DailySleepRecord record : log.getSleepRecords()) {
            String dayName = String.format("%-10s", record.getDay());
            int bars = (int) Math.round(record.getSleepHours());
            String barChart = ACCENT_SUCCESS + "█".repeat(Math.min(bars, 24)) + RESET;
            
            String line = " ".repeat(leftPadding) + TEXT_BRIGHT + dayName + TEXT_NORMAL + " " + barChart + TEXT_SOFT + " " + record.getSleepHours() + "h" + RESET;
            System.out.println(line);
        }
        
        System.out.println();
        System.out.println(centerText(TEXT_SOFT + "Scale: Each █ = 1 hour" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Recommended: 7-9 hours (███████ to █████████)" + RESET, 100));
        
        System.out.println("\n" + centerText(PRIMARY_MID + "Sleep Quality Trend:" + RESET, 100));
        System.out.println();
        
        for (DailySleepRecord record : log.getSleepRecords()) {
            String dayName = String.format("%-10s", record.getDay());
            int quality = record.getSleepQuality();
            String stars = PRIMARY_LIGHT + "★".repeat(quality) + TEXT_SOFT + "☆".repeat(5 - quality) + RESET;
            
            System.out.println(centerText(TEXT_BRIGHT + dayName + TEXT_NORMAL + " | " + stars + TEXT_SOFT + " (" + quality + "/5)" + RESET, 100));
        }
        
        System.out.println();
        double avgHours = log.calculateAverageSleepHours();
        System.out.println(centerText(PRIMARY_MID + "Weekly Average: " + TEXT_BRIGHT + String.format("%.1f", avgHours) + " hours" + RESET, 100));
    }
    
    @Override
    public String getReportFormat() {
        return "Graphical Report";
    }
    
    private static String centerText(String text, int width) {
        String plainText = text.replaceAll("\033\\[[;\\d]*m", "");
        int padding = (width - plainText.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text;
    }
}