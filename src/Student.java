import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class Student {

    private HashMap<LocalDate, Integer> attendancedat;

    private String ID;
    private String firstName;
    private String surName;
    private String ASUID;
    private String level;
    private String course;

    public Student(
            String ID,
            String firstName,
            String surName,
            String course,
            String level,
            String ASUID) {
        this.ID = ID;
        this.firstName = firstName;
        this.surName = surName;
        this.course = course;
        this.level = level;
        this.ASUID = ASUID;
        this.attendancedat = new LinkedHashMap();
    }

    public void addAttendance(LocalDate date, int time) {
        attendancedat.put(date, attendancedat.getOrDefault(date, 0) + time);
    }

    public int getDateAttendance(LocalDate date) {
        return attendancedat.get(date);
    }
    public HashMap<LocalDate, Integer> getAttendancedat() {
        return new LinkedHashMap(attendancedat);
    }
    public String getID() {
        return this.ID;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getSurName() {
        return this.surName;
    }
    public String getASUID() {
        return this.ASUID;
    }
    public String getLevel() {
        return level;
    }
    public String getCourse() {
        return course;
    }

}
