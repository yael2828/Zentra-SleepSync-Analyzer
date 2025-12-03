package encapsulation;

public class DailySleepRecord {
    private final String day;
    private double sleepHours;
    private int sleepQuality;
    private String notes;
    
    public DailySleepRecord(String day, double sleepHours, int sleepQuality, String notes) {
        this.day = day;
        this.sleepHours = sleepHours;
        this.sleepQuality = sleepQuality;
        this.notes = notes;
    }
    
    public String getDay() { return day; }
    public double getSleepHours() { return sleepHours; }
    public int getSleepQuality() { return sleepQuality; }
    public String getNotes() { return notes; }
    
    public void setSleepHours(double hours) {
        if (hours >= 0 && hours <= 24) {
            this.sleepHours = hours;
        } else {
            throw new IllegalArgumentException("Sleep hours must be between 0 and 24");
        }
    }
    
    public void setSleepQuality(int quality) {
        if (quality >= 1 && quality <= 5) {
            this.sleepQuality = quality;
        } else {
            throw new IllegalArgumentException("Sleep quality must be between 1 and 5");
        }
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %.1f hours | Quality: %d/5 | Notes: %s", day, sleepHours, sleepQuality, notes);
    }
}