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
    protected static FXMLLoader homePage = new FXMLLoader(Main.class.getResource("/FXML/Home-Page.fxml"));
    protected static FXMLLoader loginPage = new FXMLLoader(Main.class.getResource("/FXML/Login-Page.fxml"));
    protected static FXMLLoader registerPage = new FXMLLoader(Main.class.getResource("/FXML/Register-Page.fxml"));
    protected static FXMLLoader goalsPage= new FXMLLoader(Main.class.getResource("/FXML/Goals-Page.fxml"));
    protected static FXMLLoader mapPage = new FXMLLoader(Main.class.getResource("/FXML/Map-Page.fxml"));
    protected static FXMLLoader calendarPage = new FXMLLoader(Main.class.getResource("/FXML/Calendar-Page.fxml"));
    protected static FXMLLoader profilePage = new FXMLLoader(Main.class.getResource("/FXML/Profile-Page.fxml"));

    protected sceneLoaderController sceneLoaderController = new sceneLoaderController();
    protected StageController stageController = new StageController();

    public static void main(String[] args) {
        launch(args);
    }

    public static FXMLLoader getLoginPage(){
        return loginPage;
    }
    public static FXMLLoader getHomePage(){
        return homePage;
    }
    public static FXMLLoader getRegisterPage(){
        return registerPage;
    }
    public static FXMLLoader getGoalsPage(){
        return goalsPage;
    }
    public static FXMLLoader getMapPage(){
        return mapPage;
    }
    public static FXMLLoader getCalendarPage(){
        return calendarPage;
    }
    public static FXMLLoader getProfilePage(){
        return profilePage;
    }
    @Override
    public void start(Stage applicationStage) throws Exception {
        sceneLoaderController.changeScene(loginPage); // loads the login page as the first scene
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