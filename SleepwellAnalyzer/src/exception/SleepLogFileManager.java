package exception;
import java.io.*;
import java.util.*;
import encapsulation.WeeklySleepLog;
import encapsulation.DailySleepRecord;

public class SleepLogFileManager {
    private static final String FILE_NAME = "sleep_logs.txt";
    
    private static final String RESET = "\033[0m";
    private static final String PRIMARY_DARK = "\033[38;5;27m";
    private static final String PRIMARY_MID = "\033[38;5;45m";
    private static final String PRIMARY_LIGHT = "\033[38;5;123m";
    private static final String ACCENT_SUCCESS = "\033[38;5;46m";
    private static final String ACCENT_WARNING = "\033[38;5;220m";
    private static final String TEXT_BRIGHT = "\033[38;5;255m";
    private static final String TEXT_NORMAL = "\033[38;5;253m";
    private static final String TEXT_SOFT = "\033[38;5;250m";
    
    public void saveToFile(WeeklySleepLog log) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write("WEEK_START:" + log.getWeekLabel() + "\n");
            writer.write("DATE:" + log.getWeekStartDate() + "\n");
            writer.write("AVG_SLEEP:" + String.format("%.2f", log.calculateAverageSleepHours()) + "\n");
            writer.write("AVG_QUALITY:" + String.format("%.2f", log.calculateAverageSleepQuality()) + "\n");
            
            DailySleepRecord best = log.getBestSleepDay();
            DailySleepRecord worst = log.getWorstSleepDay();
            
            if (best != null) {
                writer.write("BEST:" + best.getDay() + ":" + best.getSleepHours() + "\n");
            }
            if (worst != null) {
                writer.write("WORST:" + worst.getDay() + ":" + worst.getSleepHours() + "\n");
            }
            
            writer.write("RECORDS_START\n");
            for (DailySleepRecord record : log.getSleepRecords()) {
                writer.write(record.getDay() + "|" + record.getSleepHours() + "|" + record.getSleepQuality() + "|" + record.getNotes() + "\n");
            }
            writer.write("RECORDS_END\n");
            
            writer.write("WEEKDAY_ANALYSIS:" + log.getWeekdayAnalysis().replace("\n", "||") + "\n");
            writer.write("WEEKEND_ANALYSIS:" + log.getWeekendAnalysis().replace("\n", "||") + "\n");
            
            List<String> patterns = log.detectIrregularPatterns();
            if (!patterns.isEmpty()) {
                writer.write("PATTERNS_START\n");
                for (String pattern : patterns) {
                    writer.write(pattern + "\n");
                }
                writer.write("PATTERNS_END\n");
            }
            
            writer.write("WEEK_END\n\n");
        }
    }
    
    public List<String> getAllSavedWeeks() throws IOException {
        List<String> weeks = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return weeks;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("WEEK_START:")) {
                    weeks.add(line.substring(11));
                }
            }
        }
        return weeks;
    }
    
    public void viewAllLogs() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("\n" + centerText("No saved logs found.", 100) + "\n");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean inRecords = false;
            boolean inPatterns = false;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("WEEK_START:")) {
                    System.out.println("\n" + centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════════════════════════" + RESET, 100));
                    System.out.println(centerText(PRIMARY_DARK + "WEEKLY SLEEP LOG - " + RESET + TEXT_BRIGHT + line.substring(11) + RESET, 100));
                    System.out.println(centerText(PRIMARY_DARK + "═══════════════════════════════════════════════════════════════════════════════" + RESET, 100) + "\n");
                } else if (line.startsWith("DATE:")) {
                    System.out.println(centerText(TEXT_SOFT + "Week Starting: " + TEXT_BRIGHT + line.substring(5) + RESET, 100));
                } else if (line.startsWith("AVG_SLEEP:")) {
                    System.out.println(centerText(PRIMARY_MID + "Average Sleep: " + TEXT_BRIGHT + line.substring(10) + " hours" + RESET, 100));
                } else if (line.startsWith("AVG_QUALITY:")) {
                    System.out.println(centerText(PRIMARY_MID + "Average Quality: " + TEXT_BRIGHT + line.substring(12) + "/5" + RESET, 100));
                } else if (line.startsWith("BEST:")) {
                    String[] parts = line.substring(5).split(":");
                    if (parts.length >= 2) {
                        System.out.println(centerText(ACCENT_SUCCESS + "  Best Sleep: " + TEXT_BRIGHT + parts[0] + " (" + parts[1] + " hours)" + RESET, 100));
                    }
                } else if (line.startsWith("WORST:")) {
                    String[] parts = line.substring(6).split(":");
                    if (parts.length >= 2) {
                        System.out.println(centerText(ACCENT_WARNING + "⚠ Worst Sleep: " + TEXT_BRIGHT + parts[0] + " (" + parts[1] + " hours)" + RESET, 100));
                    }
                } else if (line.equals("RECORDS_START")) {
                    inRecords = true;
                    System.out.println("\n" + centerText(PRIMARY_LIGHT + "Daily Records:" + RESET, 100));
                } else if (line.equals("RECORDS_END")) {
                    inRecords = false;
                } else if (inRecords) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        System.out.println(centerText(TEXT_NORMAL + parts[0] + ": " + TEXT_BRIGHT + parts[1] + "h" + TEXT_NORMAL + " | Quality: " + TEXT_BRIGHT + parts[2] + "/5" + TEXT_NORMAL + " | " + TEXT_SOFT + parts[3] + RESET, 100));
                    }
                } else if (line.startsWith("WEEKDAY_ANALYSIS:")) {
                    System.out.println("\n" + centerText(PRIMARY_LIGHT + "📅 Weekday Analysis:" + RESET, 100));
                    String[] analyses = line.substring(17).split("\\|\\|");
                    for (String analysis : analyses) {
                        System.out.println(centerText(TEXT_NORMAL + analysis + RESET, 100));
                    }
                } else if (line.startsWith("WEEKEND_ANALYSIS:")) {
                    System.out.println("\n" + centerText(PRIMARY_LIGHT + "🎉 Weekend Analysis:" + RESET, 100));
                    String[] analyses = line.substring(17).split("\\|\\|");
                    for (String analysis : analyses) {
                        System.out.println(centerText(TEXT_NORMAL + analysis + RESET, 100));
                    }
                } else if (line.equals("PATTERNS_START")) {
                    inPatterns = true;
                    System.out.println("\n" + centerText(ACCENT_WARNING + "⚠ Irregular Patterns:" + RESET, 100));
                } else if (line.equals("PATTERNS_END")) {
                    inPatterns = false;
                } else if (inPatterns) {
                    System.out.println(centerText(ACCENT_WARNING + "• " + TEXT_NORMAL + line + RESET, 100));
                }
            }
        }
    }
    
    public boolean deleteWeekLog(String weekLabel) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return false;
        }
        
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean skipWeek = false;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("WEEK_START:")) {
                    if (line.substring(11).equals(weekLabel)) {
                        skipWeek = true;
                    } else {
                        skipWeek = false;
                        lines.add(line);
                    }
                } else if (line.equals("WEEK_END")) {
                    if (!skipWeek) {
                        lines.add(line);
                        lines.add("");
                    }
                    skipWeek = false;
                } else if (!skipWeek) {
                    lines.add(line);
                }
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        }
        
        return true;
    }
    
    public boolean deleteAllLogs() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
    
    private static String centerText(String text, int width) {
        String plainText = text.replaceAll("\033\\[[;\\d]*m", "");
        int padding = (width - plainText.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text;
    }
}