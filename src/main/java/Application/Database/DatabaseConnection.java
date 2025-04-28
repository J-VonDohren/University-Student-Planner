package Application.Database;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:student_planner.db";
    private static Connection instance = null;

    private static UserSignupDAO userSignupDAO;
    public static UserSignupDAO getUserSignupDAO() {
        if (userSignupDAO == null) {
            userSignupDAO = new UserSignupDAO();
        }
        return userSignupDAO;
    }

    private static UserTimetableDAO userTimetableDAO;
    public static UserTimetableDAO getUserTimetableDAO() {
        if (userTimetableDAO == null) {
            userTimetableDAO = new UserTimetableDAO();
        }
        return userTimetableDAO;
    }


    //    private static UserCollectedDAO userDAO; // uncomment when implemented

//  private constructor prevents external instantiation ensures that only one instance of
//  DatabaseConnection exists throughout the application. (Singleton Pattern)
    private DatabaseConnection() {}

    public static Connection getInstance() {
        if (instance == null) {
            try {
                instance = DriverManager.getConnection(URL);
//                System.out.println("Database connected successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static void closeConnection() {
        if (instance != null) {
            try {
                instance.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                instance = null;  // Reset instance to allow reconnecting if needed
            }
        }
    }

    /**
     * Utility function to set parameters in a PreparedStatement dynamically.
     * - Checks for required fields before executing.
     * - Handles null values by calling setNull().
     *
     * @param stmt  The PreparedStatement
     * @param requiredIndexes The indexes of required (non-nullable) fields.
     * @param values The array of values to set
     * @throws SQLException If a required field is null or if a database access error occurs
     */
    public static void StatementPrep(PreparedStatement stmt, int[] requiredIndexes, Object... values) throws SQLException {
        // Validate required fields
        for (int index : requiredIndexes) {
            if (values[index - 1] == null) {  // Convert 1-based index to 0-based
                throw new SQLException("Error: Required field at index " + index + " is null.");
            }
        }

        // Set parameters dynamically
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                stmt.setNull(i + 1, Types.VARCHAR);  // Handles null as SQL NULL
            } else {
                stmt.setObject(i + 1, values[i]);   // Handles non-null values
            }
        }
    }


}
