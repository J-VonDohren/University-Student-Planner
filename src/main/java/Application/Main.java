package Application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;

public class Main extends Application {

    private Parent root;
    private Stage applicationStage = new Stage(); //global stage reference
    protected FXMLLoader homePage;
    protected FXMLLoader loginPage;
    protected FXMLLoader registerPage;
    protected FXMLLoader goalsPage;
    protected FXMLLoader mapPage;
    protected FXMLLoader calendarPage;

    public void changeScene(String fxmlFilePath) throws IOException{
        try{
            FXMLLoader fxmlFile = new FXMLLoader(getClass().getResource(fxmlFilePath));
            root = fxmlFile.load();

            formatStage();

            applicationStage.setScene(new Scene(root));
            applicationStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage applicationStage) throws Exception {
        this.applicationStage = applicationStage; // syncs the 2 stages

        loadAllFXMLFiles(); // stores all fxml files in a FXMLLoader protected variable for use in the sceneLoaderController

        changeScene("/Login-Page.fxml"); // loads the login page as the first scene

        applicationStage.show(); // shows the stage
    }
    public void formatStage(){
        applicationStage.getIcons().add(new Image("Img/QUT-Logo.jpg"));
    }
    public void closeActiveStage(){
        Platform.runLater(() -> {
            for (Window window : Stage.getWindows()) {
                if (window instanceof Stage) { // doesn't close every window open
                    ((Stage) window).close();
                }
            }
        });
    }

    public void loadAllFXMLFiles(){
        try{
        loginPage = new FXMLLoader(getClass().getResource("/Login-Page.fxml"));
        registerPage = new FXMLLoader(getClass().getResource("/Register-Page.fxml"));
        homePage = new FXMLLoader(getClass().getResource("/Home-Page.fxml"));
        goalsPage = new FXMLLoader(getClass().getResource("/Goals-Page.fxml"));
        mapPage = new FXMLLoader(getClass().getResource("/Map-Page.fxml"));
        calendarPage = new FXMLLoader(getClass().getResource("/Calendar-Page.fxml"));

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}