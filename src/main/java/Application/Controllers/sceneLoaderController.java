package Application.Controllers;


import Application.*;
import Application.Database.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXML;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Stream;

import Application.Main;
import Application.StageController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class sceneLoaderController{
    protected final UserSignupDAO userSignupDAO = DatabaseConnection.getUserSignupDAO();
    protected final UserTimetableDAO userTimetableDAO = DatabaseConnection.getUserTimetableDAO();
    protected final UserCollectedDataDAO userCollectedDataDAO = DatabaseConnection.getUserCollectedDataDAO();
    private Parent root;
    public static String currentUserNumber; //used to track the user currently logged in
    protected StageController stageController = new StageController();
    protected ArrayList<Thread> ineruptableThreads=new ArrayList<Thread>();
    @FXML protected MenuItem viewProfileItem;
    @FXML protected MenuItem updateDetailsItem;
    @FXML protected MenuItem updateGoalsItem;
    @FXML protected MenuItem logoutItem;
    @FXML protected MenuItem closeAppItem;

    @FXML
    public void initialize() {
        try {
            for (Thread thread:ineruptableThreads){
                thread.interrupt();
            }
            ineruptableThreads.clear();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void changeScene(FXMLLoader loader) throws IOException {
        try {
            if (loader.getLocation() == null) {
                throw new IllegalStateException("no location set for current FXML loader");
            }
            root = loader.load();
            double baseW = root.prefWidth(-1) + 125;
            double baseH = root.prefHeight(-1);

            Group group = new Group(root);
            StackPane scalableContainer = new StackPane(group);
            Scene scene = new Scene(scalableContainer, baseW, baseH);

            stageController.formatStage();
            stageController.applicationStage.setScene(scene);
            stageController.applicationStage.setResizable(true);
            stageController.applicationStage.show();

            scalableContainer.widthProperty().addListener((obs, oldVal, newVal) -> updateScale(group, scalableContainer, baseW, baseH));
            scalableContainer.heightProperty().addListener((obs, oldVal, newVal) -> updateScale(group, scalableContainer, baseW, baseH));

            Platform.runLater(
                    () -> updateScale(group, scalableContainer, baseW, baseH)
            );

        } catch (IllegalStateException e) {
            System.out.println(e);
        }
    }
    private void updateScale(Group group, StackPane container, double baseW, double baseH) {
        double scaleX = container.getWidth() / baseW;
        double scaleY = container.getHeight() / baseH;
        //double scale = Math.min(scaleX, scaleY);

        group.setScaleX(scaleX);
        group.setScaleY(scaleY);

        group.setLayoutX((container.getWidth() - baseW * scaleX) / 2);
        group.setLayoutY((container.getHeight() - baseH * scaleY) / 2);
    }



    public void switchToLoginPage() throws Exception {
        try{
            changeScene(Main.getLoginPage());
            stageController.applicationStage.getScene().getStylesheets().add("/Styling/login.css");
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
}
