package Application.Controllers;

import Application.Database.UserSignup;
import Application.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class registerPageController extends sceneLoaderController {

    @FXML private TextField registerFirstName;
    @FXML private TextField registerLastName;
    @FXML private TextField registerEmail;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField confirmRegisterPassword;
    @FXML private Label registerErrorMessagesText;
    @FXML private TextField registerStudentNumber;
    @FXML private Button creatAccBtn;

    @FXML
    public void initialize() {
        // Pressing Enter on username moves to password
        creatAccBtn.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                registerUser();
            }
        });
    }

    private void revealErrorMsg(){
        registerErrorMessagesText.setVisible(true);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> registerErrorMessagesText.setVisible(false));
        delay.play();
    }

    public void registerUser() {
        String studentNumber = Validation.normaliseStudentNo(registerStudentNumber.getText());
        String firstName = registerFirstName.getText();
        String lastName = registerLastName.getText();
        String email = registerEmail.getText();
        String password = registerPassword.getText();
        String confirmedPassword = confirmRegisterPassword.getText();

        String blankError = Validation.anyBlank(studentNumber,firstName,lastName,email,password,confirmedPassword);
        if (blankError != null) {
            registerErrorMessagesText.setText(blankError);
            revealErrorMsg();
            return;
        }

        String studentNoError = Validation.validateStudentNumber(studentNumber);
        if (studentNoError != null) {
            registerErrorMessagesText.setText(studentNoError);
            revealErrorMsg();
            return;
        }

        String emailError = Validation.validateEmail(email);
        if (emailError != null) {
            registerErrorMessagesText.setText(emailError);
            revealErrorMsg();
            return;
        }

        String passwordError = Validation.validatePassword(password, confirmedPassword);
        if (passwordError != null) {
            registerErrorMessagesText.setText(passwordError);
            revealErrorMsg();
            return;
        }

        try {
            UserSignup userToSignUp = new UserSignup(studentNumber,firstName,lastName,email, "", password);
            userSignupDAO.insertUser(userToSignUp);
            switchToLoginPage();
        } catch (Exception e) {
            javafx.application.Platform.exit(); // exception exits platform
        }
    }

}
