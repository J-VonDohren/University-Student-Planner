package Application.Controllers;


import Application.Main;
import Application.Database.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Stream;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;


public class sceneLoaderController extends Main {
    protected final UserSignupDAO userSignupDAO = DatabaseConnection.getUserSignupDAO();
    protected final UserTimetableDAO userTimetableDAO = DatabaseConnection.getUserTimetableDAO();
//    protected UserCollectedDAO userDAO; // uncomment when implemented

    @FXML protected MenuItem viewProfileItem;
    @FXML protected MenuItem updateDetailsItem;
    @FXML protected MenuItem updateGoalsItem;
    @FXML protected MenuItem logoutItem;
    @FXML protected MenuItem closeAppItem;

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

    protected boolean checkDuplicateStudentNumbers(String studentNumber) {
        String checkUnique = "SELECT count(1) FROM User_Signup_Data where StudentNumber = '" + studentNumber + "';";
        try {
            Statement statement = userSignupDAO.getDBConnection().createStatement();
            ResultSet queryResult = statement.executeQuery(checkUnique);
            queryResult.next();

            return queryResult.getInt(1) == 1; // returns true if the student number already exists
        } catch (Exception e){
            System.out.println(e + "exception");
        }
        return false; // will never execute but needed for no syntax error
    }

    /** Returns an error message for invalid input or null when all arguments are valid */
    protected String inputValidation(
            String studentNumber,
            String firstName,
            String lastName,
            String email,
            String password,
            String confirmedPassword
    ){
        try {
            // Check for empty fields
            if (Stream.of(studentNumber, firstName, lastName, email, password, confirmedPassword).anyMatch(String::isEmpty)) {
                return "don't leave fields empty.";
            }

            if (!password.equals(confirmedPassword)) {
                return "passwords don't match.";
            }

            if (password.length() < 8) {
                return "password must be at least 8 characters long.";
            }

            // check if email has an '@' with characters on either side
            if (!email.matches(".+@.+")) {
                return "invalid email.";
            }

            if (checkDuplicateStudentNumbers(studentNumber) == true){
                return "student number already registered.";
            }

            // check if student number solely comprised of 'n' followed by 8 numeric characters
            if (!studentNumber.matches("^n[0-9]{8}$")) {
                return "invalid student number.";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Passed all checks (so far)
        return null;
    }

    /** Prepends an 'n' to the student number if not present */
    protected static String normaliseStudentNo(String studentNo) {
        if (studentNo.startsWith("n")) {
            return studentNo;
        }
        return "n" + studentNo;
    }

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

    public void logout(){
        try{
            currentUserNumber = "";
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
        System.out.println("Updating goals");
    }

    public void updateDetails() {
        System.out.println("Updating details");
    }

    public void viewProfile() {
        try{
            switchToProfilePage();
        } catch (Exception e){
            System.out.println(e);
        }
    }

}
