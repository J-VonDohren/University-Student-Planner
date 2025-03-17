package Application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;

public class registerPageController extends sceneLoaderController{
    protected Connection dBConnection;

    @FXML private TextField registerFirstName;
    @FXML private TextField registerLastName;
    @FXML private TextField registerEmail;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField confirmRegisterPassword;
    @FXML private Label registerErrorMessagesText;
    @FXML private TextField registerStudentNumber;

    @FXML
    public void initialize() {
        try {
            setDbConnection();
        } catch (SQLException e) {
            System.out.println("Error initializing database connection: " + e.getMessage());
        }
    }

    public void setDbConnection() throws SQLException {
        dBConnection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean checkDuplicateStudentNumbers(String studentNumber){
        String checkUnique = "SELECT count(1) FROM User_Signup_Data where StudentNumber = '" + studentNumber + "';";
        try{
            Statement statement = dBConnection.createStatement();
            ResultSet queryResult = statement.executeQuery(checkUnique);
            queryResult.next();
            if(queryResult.getInt(1) == 1){
                return true; // returns true because the student number already exists
            }else{
                return false; //
            }
        }catch(Exception e){
            System.out.println(e + "exception");
        }
        return false; // will never execute but needed for no syntax error
    }

    public void registerUser() {
        String studentNumber = registerStudentNumber.getText();
        String firstName = registerFirstName.getText();
        String lastName = registerLastName.getText();
        String email = registerEmail.getText();
        String password = registerPassword.getText();
        String confirmedPassword = confirmRegisterPassword.getText();

        if (studentNumber.equals("") || firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || confirmedPassword.equals("")) {
            registerErrorMessagesText.setText("don't leave fields empty");
            return;
        } else if (confirmedPassword.equals(password) == false) {
            registerErrorMessagesText.setText("passwords don't match");
            return;
        } else {
            boolean isDuplicate = checkDuplicateStudentNumbers(studentNumber);
            if (isDuplicate == true) {
                registerErrorMessagesText.setText("student number is already registered");
                return;
            } else {
                try {
                    String addUserQuery = "INSERT INTO User_Signup_Data(StudentNumber, FirstName, LastName, Email, LoginPassword) VALUES(?, ?, ?, ?, ?);";
                    PreparedStatement statement = dBConnection.prepareStatement(addUserQuery);
                    statement.setString(1,studentNumber);
                    statement.setString(2,firstName);
                    statement.setString(3,lastName);
                    statement.setString(4,email);
                    statement.setString(5,password);

                    statement.execute();
                    changeScene("/Login-Page.fxml");
                } catch (Exception e) {
                    javafx.application.Platform.exit(); // exception exits platform
                }
            }
        }
    }
}
