package Application.Controllers;


import Application.*;
import Application.Database.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXML;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Stream;

import Application.Main;
import Application.StageController;
import javafx.stage.Stage;

public class sceneLoaderController{
    protected final UserSignupDAO userSignupDAO = DatabaseConnection.getUserSignupDAO();
    protected final UserTimetableDAO userTimetableDAO = DatabaseConnection.getUserTimetableDAO();
    protected final UserCollectedDataDAO userCollectedDataDAO = DatabaseConnection.getUserCollectedDataDAO();
    private Parent root;
    public static String currentUserNumber; //used to track the user currently logged in
    protected StageController stageController = new StageController();
    @FXML protected MenuItem viewProfileItem;
    @FXML protected MenuItem updateDetailsItem;
    @FXML protected MenuItem updateGoalsItem;
    @FXML protected MenuItem logoutItem;
    @FXML protected MenuItem closeAppItem;

    public sceneLoaderController(Stage stage) {stage = stageController.applicationStage;
    }
    public sceneLoaderController(){

    }

    @FXML
    public void initialize() {
        try {
            viewProfileItem.setOnAction(e -> viewProfile());
            updateDetailsItem.setOnAction(e -> updateDetails());
            updateGoalsItem.setOnAction(e -> updateGoals());
            logoutItem.setOnAction(e -> logout());
            closeAppItem.setOnAction(e -> closeApp());

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void changeScene(FXMLLoader loader) throws IOException {
        try{
            if (loader.getLocation() == null) {
                throw new IllegalStateException("no location set for current FXML loader");
            }
            root = loader.load();

            stageController.formatStage();

            stageController.applicationStage.setScene(new Scene(root));
            stageController.applicationStage.show();
        } catch (IllegalStateException e) {
            System.out.println(e);
        }
    }
    public void switchToLoginPage() throws Exception {
        try{
            changeScene(Main.getLoginPage());
            stageController.closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToRegisterPage() throws Exception {
        try{
            changeScene(Main.getRegisterPage());
            stageController.closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToHomePage() throws Exception {
        try{
            changeScene(Main.getHomePage());
            stageController.closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToMapPage() throws Exception {
        try{
            changeScene(Main.getMapPage());
            stageController.closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToGoalsPage() throws Exception {
        try{
            changeScene(Main.getGoalsPage());
            stageController.closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToCalendarPage() throws Exception {
        try{
            changeScene(Main.getCalendarPage());
            stageController.closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void switchToProfilePage() throws Exception{
        try{
            changeScene(Main.getProfilePage());
            stageController.closeActiveStage();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void logout(){
        try{
            this.currentUserNumber = "";
            switchToLoginPage();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void closeApp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to close the app?");
        alert.setContentText("Any unsaved changes will be lost.");

        // Show dialog and wait for user response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void updateGoals() {
        try{
            switchToGoalsPage();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void updateDetails() {
        try{
            switchToCalendarPage();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void viewProfile() {
        try{
            switchToProfilePage();
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
