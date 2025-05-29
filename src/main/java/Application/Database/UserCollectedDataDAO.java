package Application.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserCollectedDataDAO extends BaseDAO{
    public UserCollectedDataDAO(){
        createTable();
    }
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS User_Collected_Data (" +
                "StudentNumber CHAR, " +
                "dateModified VARCHAR," +
                "GPA INT DEFAULT 0," +
                "GPAGoal INT DEFAULT 0," +
                "HoursStudied FLOAT DEFAULT 0," +
                "HoursStudiedGoal FLOAT DEFAULT 0," +
                "AttendanceRate FLOAT DEFAULT 0," +
                "AttendanceRateGoal FLOAT DEFAULT 0," +
                "UnitsEnrolled VARCHAR," +
                "PRIMARY KEY(StudentNumber, dateModified))";
        try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertData(UserCollectedData usersData){
        String sql = "INSERT INTO User_Collected_Data (studentNumber, dateModified, gpa, gpaGoal, hoursStudied, hoursStudiedGoal, attendanceRate, attendanceRateGoal, unitsEnrolled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Define indexes of required (non-nullable) fields
            int[] requiredFields = {1, 2}; // StudentNumber, dateModified

            // Use the improved StatementPrep() with validation
            DatabaseConnection.StatementPrep(stmt, requiredFields,
                    usersData.getStudentNumber(),
                    usersData.getDateModified(),
                    usersData.getGpa(),
                    usersData.getGpaGoal(),
                    usersData.getHoursStudied(),
                    usersData.getHoursStudiedGoal(),
                    usersData.getAttendanceRate(),
                    usersData.getAttendanceRateGoal(),
                    usersData.getUnitsEnrolled()
            );

            stmt.executeUpdate();
            System.out.println("data added successfully!");
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
        }
    }

}
