import java.util.*;
import java.io.IOException;
import encapsulation.DailySleepRecord;
import encapsulation.WeeklySleepLog;
import polymorphism.ReportGenerator;
import polymorphism.DetailedReport;
import polymorphism.SummaryReport;
import polymorphism.GraphicalReport;
import exception.SleepLogFileManager;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SleepLogFileManager fileManager = new SleepLogFileManager();
    
    private static final String RESET = "\033[0m";
    private static final String PRIMARY_DARK = "\033[38;5;27m";
    private static final String PRIMARY_MID = "\033[38;5;45m";
    private static final String PRIMARY_LIGHT = "\033[38;5;123m";
    private static final String ACCENT_SUCCESS = "\033[38;5;46m";
    private static final String ACCENT_WARNING = "\033[38;5;220m";
    private static final String ACCENT_ERROR = "\033[38;5;196m";
    private static final String ACCENT_INFO = "\033[38;5;141m";
    private static final String TEXT_BRIGHT = "\033[38;5;255m";
    private static final String TEXT_NORMAL = "\033[38;5;253m";
    private static final String TEXT_SOFT = "\033[38;5;250m";
    
    public static void main(String[] args) {
        try (scanner) {
            displayWelcomeScreen();
            
            int choice;
            do {
                clearScreen();
                displayMainMenu();
                choice = getIntInput();
                clearScreen();
                
                try {
                    switch(choice) {
                        case 1 -> recordWeeklySleep();
                        case 2 -> viewSleepAnalysis();
                        case 3 -> generateReports();
                        case 4 -> viewSavedLogs();
                        case 5 -> updateWeeklySleep();
                        case 6 -> deleteLogs();
                        case 7 -> displayExitMessage();
                        default -> System.out.println("\n" + centerText(ACCENT_ERROR + "Invalid choice! Try again." + RESET, 100) + "\n");
                    }
                } catch (Exception e) {
                    System.out.println("\n" + centerText(ACCENT_ERROR + "Error: " + e.getMessage() + RESET, 100) + "\n");
                }
                
                if (choice != 7) {
                    System.out.println("\n" + centerText(TEXT_BRIGHT + "Press Enter to continue..." + RESET, 100));
                    scanner.nextLine();
                }
                
            } while (choice != 7);
        }
    }
    
    private static WeeklySleepLog currentWeekLog = null;
    
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private static String centerText(String text, int width) {
        String plainText = text.replaceAll("\033\\[[;\\d]*m", "");
        int padding = (width - plainText.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text;
    }
    
    private static void displayWelcomeScreen() {
        String YELLOW_TEXT = "\033[38;5;226m";
        String WHITE_TEXT = "\033[38;5;255m";
        
        System.out.println("\n\n");
        System.out.println(centerText(PRIMARY_MID + "╔════════════════════════════════════════════════════════════════╗" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + YELLOW_TEXT + "       ███████ ███████ ███    ██ ████████ ██████   █████        " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + YELLOW_TEXT + "            ██ ██      ████   ██    ██    ██   ██ ██   ██       " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + YELLOW_TEXT + "         ████  █████   ██ ██  ██    ██    ██████  ███████       " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + YELLOW_TEXT + "       ██      ██      ██  ██ ██    ██    ██   ██ ██   ██       " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + YELLOW_TEXT + "       ███████ ███████ ██   ████    ██    ██   ██ ██   ██       " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + WHITE_TEXT + "                      SLEEPSYNC ANALYZER                        " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "╚════════════════════════════════════════════════════════════════╝" + RESET, 100));
        System.out.println("\n" + centerText(TEXT_BRIGHT + "Press Enter to continue..." + RESET, 100));
        scanner.nextLine();
    }
    
    private static void displayMainMenu() {
        System.out.println("\n\n");
        System.out.println(centerText(PRIMARY_MID + "╔════════════════════════════════════════════════════════════════╗" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + TEXT_BRIGHT + "                          MAIN MENU                             " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "╚════════════════════════════════════════════════════════════════╝" + RESET, 100));
        System.out.println();
        
        System.out.println(centerText(PRIMARY_MID + "[1] " + TEXT_BRIGHT + "Record Weekly Sleep" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Log your sleep data for the entire week" + RESET, 100));
        System.out.println();
        
        System.out.println(centerText(PRIMARY_MID + "[2] " + TEXT_BRIGHT + "View Sleep Analysis" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Get insights on your current week's sleep" + RESET, 100));
        System.out.println();
        
        System.out.println(centerText(PRIMARY_MID + "[3] " + TEXT_BRIGHT + "Generate Sleep Report" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Create detailed, summary, or graphical reports" + RESET, 100));
        System.out.println();
        
        System.out.println(centerText(PRIMARY_MID + "[4] " + TEXT_BRIGHT + "View Saved Logs" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "See all previously saved weekly logs" + RESET, 100));
        System.out.println();
        
        System.out.println(centerText(PRIMARY_MID + "[5] " + TEXT_BRIGHT + "Update Weekly Sleep" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Modify current week's sleep records" + RESET, 100));
        System.out.println();
        
        System.out.println(centerText(PRIMARY_MID + "[6] " + TEXT_BRIGHT + "Delete Saved Logs" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Clear saved sleep logs" + RESET, 100));
        System.out.println();
        
        System.out.println(centerText(PRIMARY_MID + "[7] " + TEXT_BRIGHT + "Exit" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Close the application" + RESET, 100));
        System.out.println();
        
        System.out.print(centerText(PRIMARY_MID + "Enter your choice: " + TEXT_BRIGHT, 100));
    }
    
    private static void recordWeeklySleep() {
        System.out.println("\n\n");
        System.out.println(centerText(PRIMARY_MID + "╔════════════════════════════════════════════════════════════════╗" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + TEXT_BRIGHT + "                     RECORD WEEKLY SLEEP                        " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "╚════════════════════════════════════════════════════════════════╝" + RESET, 100));
        System.out.println();
        
        System.out.print(centerText(PRIMARY_MID + "Enter week label (e.g., Week 1, November): " + TEXT_BRIGHT, 100));
        String weekLabel = scanner.nextLine();
        System.out.print(RESET);
        
        System.out.print(centerText(PRIMARY_MID + "Enter week start date (YYYY-MM-DD): " + TEXT_BRIGHT, 100));
        String weekStart = scanner.nextLine();
        System.out.print(RESET);
        
        currentWeekLog = new WeeklySleepLog(weekStart, weekLabel);
        
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        
        for (String day : days) {
            System.out.println("\n" + centerText(PRIMARY_LIGHT + "═══ Recording for " + day + " ═══" + RESET, 100));
            
            System.out.print(centerText(PRIMARY_MID + "Sleep hours (0-24): " + TEXT_BRIGHT, 100));
            double hours = getDoubleInput();
            System.out.print(RESET);
            
            System.out.print(centerText(PRIMARY_MID + "Sleep quality (1-5): " + TEXT_BRIGHT, 100));
            int quality = getIntInput();
            System.out.print(RESET);
            
            System.out.print(centerText(PRIMARY_MID + "Notes (optional): " + TEXT_BRIGHT, 100));
            String notes = scanner.nextLine();
            System.out.print(RESET);
            
            try {
                DailySleepRecord record = new DailySleepRecord(day, hours, quality, notes);
                currentWeekLog.addDailyRecord(record);
                System.out.println(centerText(ACCENT_SUCCESS + "✓ " + day + " recorded successfully!" + RESET, 100));
            } catch (IllegalArgumentException e) {
                System.out.println(centerText(ACCENT_ERROR + "Error: " + e.getMessage() + RESET, 100));
            }
        }
        
        System.out.println("\n" + centerText(ACCENT_SUCCESS + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(ACCENT_SUCCESS + "Weekly sleep data recorded successfully!" + RESET, 100));
        System.out.println(centerText(ACCENT_SUCCESS + "═══════════════════════════════════════════════════════════" + RESET, 100));
        
        System.out.print("\n" + centerText(PRIMARY_MID + "Save to file? (y/n): " + TEXT_BRIGHT, 100));
        String save = scanner.nextLine();
        System.out.print(RESET);
        
        if (save.equalsIgnoreCase("y")) {
            try {
                fileManager.saveToFile(currentWeekLog);
                System.out.println("\n" + centerText(ACCENT_SUCCESS + "✓ Sleep log saved to file!" + RESET, 100));
            } catch (IOException e) {
                System.out.println("\n" + centerText(ACCENT_ERROR + "✗ Error saving file: " + e.getMessage() + RESET, 100));
            }
        }
    }
    
    private static void viewSleepAnalysis() {
        System.out.println("\n\n");
        System.out.println(centerText(PRIMARY_MID + "╔════════════════════════════════════════════════════════════════╗" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + TEXT_BRIGHT + "                      SLEEP ANALYSIS                            " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "╚════════════════════════════════════════════════════════════════╝" + RESET, 100));
        System.out.println();
        
        if (currentWeekLog == null || currentWeekLog.getSleepRecords().isEmpty()) {
            System.out.println(centerText(ACCENT_INFO + "No sleep data recorded yet. Please record weekly sleep first." + RESET, 100));
            return;
        }
        
        System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "  WEEKLY SLEEP SUMMARY" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Week: " + TEXT_BRIGHT + currentWeekLog.getWeekLabel() + RESET, 100));
        System.out.println(centerText(TEXT_SOFT + "Starting: " + TEXT_BRIGHT + currentWeekLog.getWeekStartDate() + RESET, 100));
        System.out.println();
        
        double avgHours = currentWeekLog.calculateAverageSleepHours();
        double avgQuality = currentWeekLog.calculateAverageSleepQuality();
        
        System.out.println(centerText(PRIMARY_MID + "Average Sleep Hours: " + TEXT_BRIGHT + String.format("%.2f", avgHours) + " hours" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "Average Sleep Quality: " + TEXT_BRIGHT + String.format("%.2f", avgQuality) + "/5" + RESET, 100));
        System.out.println();
        
        DailySleepRecord best = currentWeekLog.getBestSleepDay();
        DailySleepRecord worst = currentWeekLog.getWorstSleepDay();
        
        if (best != null) {
            System.out.println(centerText(ACCENT_SUCCESS + "  Best Sleep: " + best.getDay() + " (" + best.getSleepHours() + " hours)" + RESET, 100));
        }
        if (worst != null) {
            System.out.println(centerText(ACCENT_WARNING + "⚠ Worst Sleep: " + worst.getDay() + " (" + worst.getSleepHours() + " hours)" + RESET, 100));
        }
        
        System.out.println("\n" + centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "  WEEKDAY ANALYSIS" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        String weekdayAnalysis = currentWeekLog.getWeekdayAnalysis();
        for (String line : weekdayAnalysis.split("\n")) {
            System.out.println(centerText(TEXT_NORMAL + line + RESET, 100));
        }
        
        System.out.println("\n" + centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "  WEEKEND ANALYSIS" + RESET, 100));
        System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
        String weekendAnalysis = currentWeekLog.getWeekendAnalysis();
        for (String line : weekendAnalysis.split("\n")) {
            System.out.println(centerText(TEXT_NORMAL + line + RESET, 100));
        }
        
        List<String> patterns = currentWeekLog.detectIrregularPatterns();
        if (!patterns.isEmpty()) {
            System.out.println("\n" + centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
            System.out.println(centerText(ACCENT_WARNING + "⚠ IRREGULAR PATTERNS DETECTED" + RESET, 100));
            System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
            for (String pattern : patterns) {
                System.out.println(centerText(ACCENT_WARNING + "• " + pattern + RESET, 100));
            }
        }
    }
    
    private static void generateReports() {
        System.out.println("\n\n");
        System.out.println(centerText(PRIMARY_MID + "╔════════════════════════════════════════════════════════════════╗" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + TEXT_BRIGHT + "                     GENERATE SLEEP REPORT                      " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "╚════════════════════════════════════════════════════════════════╝" + RESET, 100));
        System.out.println();
        
        if (currentWeekLog == null || currentWeekLog.getSleepRecords().isEmpty()) {
            System.out.println(centerText(ACCENT_INFO + "No sleep data available. Please record weekly sleep first." + RESET, 100));
            return;
        }
        
        System.out.println(centerText(PRIMARY_MID + "Select Report Type:" + RESET, 100));
        System.out.println();
        System.out.println(centerText(TEXT_BRIGHT + "[1] " + TEXT_NORMAL + "Detailed Report" + TEXT_SOFT + " - Complete breakdown with all data" + RESET, 100));
        System.out.println(centerText(TEXT_BRIGHT + "[2] " + TEXT_NORMAL + "Summary Report" + TEXT_SOFT + " - Quick overview of key metrics" + RESET, 100));
        System.out.println(centerText(TEXT_BRIGHT + "[3] " + TEXT_NORMAL + "Graphical Report" + TEXT_SOFT + " - Visual charts and trends" + RESET, 100));
        System.out.println(centerText(TEXT_BRIGHT + "[0] " + TEXT_SOFT + "Cancel" + RESET, 100));
        System.out.println();
        
        System.out.print(centerText(PRIMARY_MID + "Enter your choice: " + TEXT_BRIGHT, 100));
        int choice = getIntInput();
        System.out.print(RESET);
        
        if (choice == 0) {
            System.out.println("\n" + centerText(PRIMARY_LIGHT + "Report generation cancelled." + RESET, 100));
            return;
        }
        
        ReportGenerator report;
        
        switch(choice) {
            case 1 -> report = new DetailedReport(currentWeekLog);
            case 2 -> report = new SummaryReport(currentWeekLog);
            case 3 -> report = new GraphicalReport(currentWeekLog);
            default -> {
                System.out.println("\n" + centerText(ACCENT_ERROR + "Invalid choice!" + RESET, 100));
                return;
            }
        }
        
        System.out.println("\n" + centerText(ACCENT_SUCCESS + "Generating " + report.getReportFormat() + "..." + RESET, 100));
        System.out.println();
        
        report.generateReport();
        
        System.out.println("\n" + centerText(ACCENT_SUCCESS + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(ACCENT_SUCCESS + "✓ Report generated successfully!" + RESET, 100));
        System.out.println(centerText(ACCENT_SUCCESS + "═══════════════════════════════════════════════════════════" + RESET, 100));
    }
    
    private static void viewSavedLogs() {
        System.out.println("\n\n");
        System.out.println(centerText(PRIMARY_MID + "╔════════════════════════════════════════════════════════════════╗" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + TEXT_BRIGHT + "                       SAVED LOGS                               " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "╚════════════════════════════════════════════════════════════════╝" + RESET, 100));
        System.out.println();
        
        try {
            fileManager.viewAllLogs();
        } catch (IOException e) {
            System.out.println(centerText(ACCENT_ERROR + "Error reading logs: " + e.getMessage() + RESET, 100));
        }
    }
    
    private static void updateWeeklySleep() {
        System.out.println("\n\n");
        System.out.println(centerText(PRIMARY_MID + "╔════════════════════════════════════════════════════════════════╗" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + TEXT_BRIGHT + "                    UPDATE WEEKLY SLEEP                         " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "╚════════════════════════════════════════════════════════════════╝" + RESET, 100));
        System.out.println();
        
        if (currentWeekLog == null || currentWeekLog.getSleepRecords().isEmpty()) {
            System.out.println(centerText(ACCENT_INFO + "No sleep data to update. Please record weekly sleep first." + RESET, 100));
            return;
        }
        
        System.out.println(centerText(PRIMARY_MID + "Current Week's Records:" + RESET, 100));
        System.out.println();
        List<DailySleepRecord> records = currentWeekLog.getSleepRecords();
        for (int i = 0; i < records.size(); i++) {
            System.out.println(centerText(TEXT_BRIGHT + "[" + (i + 1) + "] " + TEXT_NORMAL + records.get(i).toString() + RESET, 100));
        }
        
        System.out.print("\n" + centerText(PRIMARY_MID + "Select day to update (1-7) or 0 to cancel: " + TEXT_BRIGHT, 100));
        int choice = getIntInput();
        System.out.print(RESET);
        
        if (choice < 1 || choice > 7) {
            System.out.println("\n" + centerText(PRIMARY_LIGHT + "Update cancelled." + RESET, 100));
            return;
        }
        
        DailySleepRecord record = records.get(choice - 1);
        
        System.out.println("\n" + centerText(PRIMARY_LIGHT + "═══ Updating " + record.getDay() + " ═══" + RESET, 100));
        
        System.out.print(centerText(PRIMARY_MID + "New sleep hours (current: " + record.getSleepHours() + "): " + TEXT_BRIGHT, 100));
        double newHours = getDoubleInput();
        System.out.print(RESET);
        
        System.out.print(centerText(PRIMARY_MID + "New sleep quality (current: " + record.getSleepQuality() + "): " + TEXT_BRIGHT, 100));
        int newQuality = getIntInput();
        System.out.print(RESET);
        
        System.out.print(centerText(PRIMARY_MID + "New notes (current: " + record.getNotes() + "): " + TEXT_BRIGHT, 100));
        String newNotes = scanner.nextLine();
        System.out.print(RESET);
        
        try {
            record.setSleepHours(newHours);
            record.setSleepQuality(newQuality);
            record.setNotes(newNotes);
            System.out.println("\n" + centerText(ACCENT_SUCCESS + "✓ Record updated successfully!" + RESET, 100));
            
            System.out.print("\n" + centerText(PRIMARY_MID + "Save updated log to file? (y/n): " + TEXT_BRIGHT, 100));
            String save = scanner.nextLine();
            System.out.print(RESET);
            
            if (save.equalsIgnoreCase("y")) {
                fileManager.saveToFile(currentWeekLog);
                System.out.println("\n" + centerText(ACCENT_SUCCESS + "✓ Updated log saved to file!" + RESET, 100));
            }
        } catch (IllegalArgumentException | IOException e) {
            System.out.println("\n" + centerText(ACCENT_ERROR + "Error: " + e.getMessage() + RESET, 100));
        }
    }
    
    private static void deleteLogs() {
        System.out.println("\n\n");
        System.out.println(centerText(PRIMARY_MID + "╔════════════════════════════════════════════════════════════════╗" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║" + TEXT_BRIGHT + "                       DELETE LOGS                              " + PRIMARY_MID + "║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "║                                                                ║" + RESET, 100));
        System.out.println(centerText(PRIMARY_MID + "╚════════════════════════════════════════════════════════════════╝" + RESET, 100));
        System.out.println();
        
        try {
            List<String> savedWeeks = fileManager.getAllSavedWeeks();
            
            if (savedWeeks.isEmpty()) {
                System.out.println(centerText(ACCENT_INFO + "No saved logs found." + RESET, 100));
                return;
            }
            
            System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
            System.out.println(centerText(PRIMARY_DARK + "SAVED WEEKS" + RESET, 100));
            System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════" + RESET, 100));
            System.out.println();
            
            for (int i = 0; i < savedWeeks.size(); i++) {
                System.out.println(centerText(TEXT_BRIGHT + "[" + (i + 1) + "] " + TEXT_NORMAL + savedWeeks.get(i) + RESET, 100));
            }
            
            System.out.println();
            System.out.println(centerText(TEXT_BRIGHT + "[" + (savedWeeks.size() + 1) + "] " + ACCENT_ERROR + "Delete All Logs" + RESET, 100));
            System.out.println(centerText(TEXT_BRIGHT + "[0] " + TEXT_SOFT + "Cancel" + RESET, 100));
            System.out.println();
            
            System.out.print(centerText(PRIMARY_MID + "Select option: " + TEXT_BRIGHT, 100));
            int choice = getIntInput();
            System.out.print(RESET);
            
            if (choice == 0) {
                System.out.println("\n" + centerText(PRIMARY_LIGHT + "Delete operation cancelled." + RESET, 100));
                return;
            }
            
            if (choice == savedWeeks.size() + 1) {
                System.out.println("\n" + centerText(ACCENT_WARNING + "═══════════════════════════════════════════════════════════" + RESET, 100));
                System.out.println(centerText(ACCENT_WARNING + "⚠ WARNING: This will delete ALL saved sleep logs!" + RESET, 100));
                System.out.println(centerText(ACCENT_WARNING + "═══════════════════════════════════════════════════════════" + RESET, 100));
                System.out.print("\n" + centerText(PRIMARY_MID + "Are you sure? (y/n): " + TEXT_BRIGHT, 100));
                
                String confirm = scanner.nextLine();
                System.out.print(RESET);
                
                if (confirm.equalsIgnoreCase("y")) {
                    if (fileManager.deleteAllLogs()) {
                        System.out.println("\n" + centerText(ACCENT_SUCCESS + "✓ All logs deleted successfully!" + RESET, 100));
                    } else {
                        System.out.println("\n" + centerText(ACCENT_INFO + "No logs found to delete." + RESET, 100));
                    }
                } else {
                    System.out.println("\n" + centerText(PRIMARY_LIGHT + "Delete operation cancelled." + RESET, 100));
                }
            } else if (choice >= 1 && choice <= savedWeeks.size()) {
                String weekToDelete = savedWeeks.get(choice - 1);
                
                System.out.println("\n" + centerText(ACCENT_WARNING + "Delete week: " + TEXT_BRIGHT + weekToDelete + ACCENT_WARNING + "?" + RESET, 100));
                System.out.print(centerText(PRIMARY_MID + "Confirm (y/n): " + TEXT_BRIGHT, 100));
                
                String confirm = scanner.nextLine();
                System.out.print(RESET);
                
                if (confirm.equalsIgnoreCase("y")) {
                    if (fileManager.deleteWeekLog(weekToDelete)) {
                        System.out.println("\n" + centerText(ACCENT_SUCCESS + "✓ Week deleted successfully!" + RESET, 100));
                    } else {
                        System.out.println("\n" + centerText(ACCENT_ERROR + "✗ Error deleting week." + RESET, 100));
                    }
                } else {
                    System.out.println("\n" + centerText(PRIMARY_LIGHT + "Delete operation cancelled." + RESET, 100));
                }
            } else {
                System.out.println("\n" + centerText(ACCENT_ERROR + "Invalid choice!" + RESET, 100));
            }
        
        } catch (IOException e) {
            System.out.println("\n" + centerText(ACCENT_ERROR + "Error: " + e.getMessage() + RESET, 100));
        }
    }
    
    private static void displayExitMessage() {
        System.out.println("\n\n\n");
        System.out.println(centerText(ACCENT_SUCCESS + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println(centerText(ACCENT_SUCCESS + " Thank you for using SleepWell! " + RESET, 100));
        System.out.println(centerText(ACCENT_SUCCESS + "═══════════════════════════════════════════════════════════" + RESET, 100));
        System.out.println();
        System.out.println(centerText(TEXT_BRIGHT + "Getting enough sleep is important for your health and happiness." + RESET, 100));
        System.out.println(centerText(TEXT_BRIGHT + "Try to sleep 7–9 hours every night and go to bed at the same time each day." + RESET, 100));
        System.out.println(centerText(TEXT_BRIGHT + "A healthy sleep schedule helps your body grow, your mind stay sharp," + RESET, 100));
        System.out.println(centerText(TEXT_BRIGHT + "and your mood stay bright." + RESET, 100));
        System.out.println();
        System.out.println(centerText(TEXT_BRIGHT + "Stay safe, sleep well, and take care of your health! 🛌💤" + RESET, 100));
        System.out.println();
        System.out.println(centerText(ACCENT_SUCCESS + "— Supporting SDG 3: Good Health and Well-Being" + RESET, 100));
        System.out.println("\n\n");
    }
    
    private static int getIntInput() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print(centerText(ACCENT_ERROR + "Invalid input. Please enter a number: " + TEXT_BRIGHT, 100));
            }
        }
    }
    
    private static double getDoubleInput() {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value < 0) {
                    System.out.print(centerText(ACCENT_ERROR + "Value cannot be negative. Try again: " + TEXT_BRIGHT, 100));
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.print(centerText(ACCENT_ERROR + "Invalid input. Please enter a number: " + TEXT_BRIGHT, 100));
            }
        }
    }
}