package Application;

import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.SQLException;

public class homePageController extends sceneLoaderController{
    protected Connection dBConnection;

    @FXML
    public void initialize() {
        try {
            setDbConnection();
        } catch (SQLException e) {
            System.err.println("Error initializing database connection: " + e.getMessage());
        }
    }

    public void setDbConnection() throws SQLException {
        dBConnection = DatabaseConnection.getInstance().getConnection();
    }}