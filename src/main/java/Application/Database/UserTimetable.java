package Application.Database;

public class UserTimetable {
    private String studentNumber;
    private String eventType;
    private String eventStartDate;
    private String eventEndDate;
    private String eventLocation;
    private int eventAttendance;

    public UserTimetable(String studentNumber, String eventType, String eventStartDate, String eventEndDate, String eventLocation, int eventAttendance) {
        this.studentNumber = studentNumber;
        this.eventType = eventType;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventLocation = eventLocation;
        this.eventAttendance = eventAttendance;
    }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getEventStartDate() { return eventStartDate; }
    public void setEventStartDate(String eventStartDate) { this.eventStartDate = eventStartDate; }
    public String getEventEndDate() { return eventEndDate; }
    public void setEventEndDate(String eventEndDate) { this.eventEndDate = eventEndDate; }
    public String getEventLocation() { return eventLocation; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }
    public int getEventAttendance() { return eventAttendance; }
    public void setEventAttendance(int eventAttendance) { this.eventAttendance = eventAttendance; }

    @Override
    public String toString() {
        return "UserTimetable{" +
                "studentNumber='" + studentNumber + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventStartDate='" + eventStartDate + '\'' +
                ", eventEndDate='" + eventEndDate + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", eventAttendance=" + eventAttendance +
                '}';
    }
}
