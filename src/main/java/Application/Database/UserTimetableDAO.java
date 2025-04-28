package Application.Database;

import java.sql.*;

public class UserTimetableDAO extends BaseDAO {

    public UserTimetableDAO() {
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS User_Timetable_Data (" +
                "EventID INT NOT NULL, " +
                "EventName VARCHAR NOT NULL, " +
                "StudentNumber VARCHAR NOT NULL, " +
                "EventType VARCHAR NOT NULL, " +
                "EventStartDatetime DATETIME NOT NULL, "+
                "EventEndDatetime DATETIME NOT NULL, "+
                "EventLocation VARCHAR, "+
                "EventAttendance INT, " +
                "PRIMARY KEY (EventID))";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //the bellow function is pretty much just copy and pasted from the UserSignupDAO
    public void insertEvent(UserTimetable event){
        String sql = "INSERT OR IGNORE INTO User_Timetable_Data (EventID, EventName, StudentNumber, EventType, EventStartDatetime, EventEndDatetime, EventLocation, EventAttendance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Define indexes of required (non-nullable) fields
            int[] requiredFields = {1, 2, 3, 4, 5, 6, 7, 8}; //EventID, EventName, StudentNumber, EventType, EventStartDatetime, EventEndDatetime, EventLocation, EventAttendance

            // Use the improved StatementPrep() with validation
            DatabaseConnection.StatementPrep(stmt, requiredFields,
                    event.getEventID(),
                    event.getEventName(),
                    event.getStudentNumber(),
                    event.getEventType(),
                    event.getEventStartDate(),
                    event.getEventEndDate(),
                    event.getEventLocation(),
                    event.getEventAttendance()    );

            stmt.executeUpdate();
            System.out.println("Event inserted successfully!");
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
        }
    }

    public void deleteEvent(int eventID){
        String sql = "DELETE FROM User_Timetable_Data WHERE EventID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1,eventID);

            stmt.executeUpdate();
            System.out.println("Event deleted successfully!");
        } catch (SQLException e) {
            System.err.println("Deletion failed: " + e.getMessage());
        }
    }

    public void updateEvent(int eventID, UserTimetable updatedEvent){
        try {
            String sql = "UPDATE User_Timetable_Data SET EventName = ?, EventType = ?, EventStartDatetime = ?, EventEndDatetime = ?," +
                    " EventLocation = ?, EventAttendance = ? WHERE EventID = ?";
            PreparedStatement updateEvent= connection.prepareStatement(sql);
            updateEvent.setString(1,updatedEvent.getEventName());
            updateEvent.setString(2,updatedEvent.getEventType());
            updateEvent.setString(3,updatedEvent.getEventStartDate());
            updateEvent.setString(4,updatedEvent.getEventEndDate());
            updateEvent.setString(5,updatedEvent.getEventLocation());
            updateEvent.setInt(6,updatedEvent.getEventAttendance());
            updateEvent.setInt(7,eventID);
            updateEvent.execute();
            System.out.println("Event updated successfully!");
        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
        }

    }


}