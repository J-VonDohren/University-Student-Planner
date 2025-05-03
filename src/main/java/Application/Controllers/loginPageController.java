package Application.Controllers;

import Application.Database.DatabaseConnection;
import Application.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;


import java.sql.*;

public class loginPageController extends sceneLoaderController {
    public Button loginButton;
    @FXML private Label loginLabel;
    @FXML private TextField loginTextField;
    @FXML private PasswordField loginPasswordField;

    public void loginButtonOnClick(MouseEvent click){
        if(!loginTextField.getText().isBlank() && !loginPasswordField.getText().isBlank()){
            validateLogin();
        }
        else{
            loginLabel.setText("Please enter a username and password.");
        }
    }

    public void validateLogin(){
        try {
            String verifyLoginQuery = "SELECT count(1) FROM User_Signup_Data where StudentNumber = ? AND LoginPassword = ?";
            PreparedStatement statement = userSignupDAO.getDBConnection().prepareStatement(verifyLoginQuery);
            statement.setString(1, Validation.normaliseStudentNo(loginTextField.getText()));
            statement.setString(2, loginPasswordField.getText());
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                if(resultSet.getInt(1) == 1){
                    // when the user is correctly logged in it will load the users home page and set the currentUserNumber
                    currentUserNumber=Validation.normaliseStudentNo(loginTextField.getText());
                    //switchToHomePage();
                    switchToGoalsPage();
                }else{
                    loginLabel.setText("Invalid login. Please try again!");
                }
            }
        }catch(Exception e){
            System.out.println(e + "exception");
        }
    }
}
