package Application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class loginPageController extends sceneLoaderController{
    public Button loginButton;
    protected Connection dBConnection;
    @FXML private Label loginLabel;
    @FXML private TextField loginTextField;
    @FXML private PasswordField loginPasswordField;

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
    }

    public void loginButtonOnClick(MouseEvent click){
        if(loginTextField.getText().isBlank() == false && loginPasswordField.getText().isBlank() == false){
            validateLogin();
        }
        else{
            loginLabel.setText("Please enter a username and password.");
        }
    }

    public void validateLogin(){
        try {
            String verifyLoginQuery = "SELECT count(1) FROM User_Signup_Data where StudentNumber = ? AND LoginPassword = ?";
            PreparedStatement statement = dBConnection.prepareStatement(verifyLoginQuery);
            statement.setString(1, loginTextField.getText());
            statement.setString(2, loginPasswordField.getText());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getInt(1) == 1){
                    switchToHomePage(); // when the user is correctly logged in it will load the users home page
                }else{
                    loginLabel.setText("Invalid login. Please try again!");
                }
            }
        }catch(Exception e){
            System.out.println(e + "exception");
        }
    }
}
