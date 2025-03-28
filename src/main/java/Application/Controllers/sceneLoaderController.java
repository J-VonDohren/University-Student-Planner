package Application.Controllers;


import Application.Main;
import Application.Database.*;

import java.io.IOException;

public class sceneLoaderController extends Main {
    protected final UserSignupDAO userSignupDAO = DatabaseConnection.getUserSignupDAO();
//    protected UserCollectedDAO userDAO; // uncomment when implemented
//    protected TimetableDAO userDAO;

    public void switchToLoginPage() throws Exception {
        try{
            changeScene("/FXML/Login-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToRegisterPage() throws Exception {
        try{
            changeScene("/FXML/Register-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToHomePage() throws Exception {
        try{
            changeScene("/FXML/Home-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToMapPage() throws Exception {
        try{
            changeScene("/FXML/Map-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToGoalsPage() throws Exception {
        try{
            changeScene("/FXML/Goals-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToCalendarPage() throws Exception {
        try{
            changeScene("/FXML/Calendar-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void switchToProfilePage() throws Exception{
        try{
            changeScene("/FXML/Profile-Page.fxml");
            closeActiveStage();
        } catch (Exception e){
            System.out.println(e);
        }
    }

}
