package Application;

import Application.Database.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Stream;

import Application.Controllers.sceneLoaderController;

public class Main extends Application {
    protected sceneLoaderController sceneLoaderController = new sceneLoaderController();
    protected StageController stageController = new StageController();

    public static void main(String[] args) {
        launch(args);
    }

    public static FXMLLoader getHomePage() {
        return new FXMLLoader(Main.class.getResource("/FXML/Home-Page.fxml"));
    }

    public static FXMLLoader getLoginPage() {
        return new FXMLLoader(Main.class.getResource("/FXML/Login-Page.fxml"));
    }

    public static FXMLLoader getRegisterPage() {
        return new FXMLLoader(Main.class.getResource("/FXML/Register-Page.fxml"));
    }

    public static FXMLLoader getGoalsPage() {
        return new FXMLLoader(Main.class.getResource("/FXML/Goals-Page.fxml"));
    }

    public static FXMLLoader getMapPage() {
        return new FXMLLoader(Main.class.getResource("/FXML/Map-Page.fxml"));
    }

    public static FXMLLoader getCalendarPage() {
        return new FXMLLoader(Main.class.getResource("/FXML/Calendar-Page.fxml"));
    }

    public static FXMLLoader getProfilePage() {
        return new FXMLLoader(Main.class.getResource("/FXML/Profile-Page.fxml"));
    }

    @Override
    public void start(Stage applicationStage) throws Exception {
        sceneLoaderController.changeScene(getLoginPage());
    }

    @Override
    public void stop() {
        // Close DAOs when the app stops
        try {
            System.out.println("Application shutting down...");
            DatabaseConnection.closeConnection();  // Close DB connection on exit
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}