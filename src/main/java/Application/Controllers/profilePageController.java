package Application.Controllers;

import Application.Database.DatabaseConnection;
import Application.Database.UserSignup;
import Application.Database.UserSignupDAO;
import Application.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;
import static java.util.Locale.filter;

public class profilePageController extends sceneLoaderController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField EmailField;
    @FXML private TextField phoneNumberField;
    @FXML private Button editButton;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private Text notice;
    @FXML private StackPane profilePlace;
    private UserSignup userSignup;

    //making this a function so it can be reused in other pages
    //when given a stackPane it will add a background and text to it
    public void setProfileImage(UserSignup user,Float size){
        //set profileImg placeholder
        //get a random seed from the userNumber so the color is different for each user
        Random random = new Random(Long.parseLong(user.getStudentNumber().replaceAll("[^0-9]","")));
        int[] colors={random.nextInt(1,255),random.nextInt(1,255),random.nextInt(1,255)};
        //if all three are too high make one lower so the background doesn't hide the text
        int brightColors=0;
        for (int color :colors){if (color > 220){brightColors+=1;}}
        if (brightColors == 3){colors[random.nextInt(1,4)]=random.nextInt(1,200);}
        //add elements to pane
        Circle profileBackGround=new Circle();
        profileBackGround.setRadius(size);
        profileBackGround.setStrokeType(StrokeType.INSIDE);
        profileBackGround.setFill(Color.rgb(colors[0],colors[1],colors[2]));

        Text profileText=new Text();
        profileText.setText(user.getFirstName().substring(0,1).toUpperCase()+user.getLastName().substring(0,1).toUpperCase());
        profileText.setFill(Color.WHITE);
        profileText.setStrokeType(StrokeType.OUTSIDE);
        profileText.setStrokeWidth(0f);
        Font font=new Font(size);
        profileText.fontProperty().setValue(font);

        profilePlace.getChildren().add(profileBackGround);
        profilePlace.getChildren().add(profileText);
    }

    public void initialize() {
        super.initialize();
        try {
            String profileInfoQuery = "SELECT * FROM User_Signup_Data where StudentNumber = ?";
            PreparedStatement statement = userSignupDAO.getDBConnection().prepareStatement(profileInfoQuery);
            statement.setString(1, currentUserNumber);
            ResultSet resultSet = statement.executeQuery();

            userSignup = new UserSignup(
                    currentUserNumber,
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName"),
                    resultSet.getString("Email"),
                    resultSet.getString("PhoneNumber"),
                    resultSet.getString("LoginPassword")
            );

            firstNameField.setText(userSignup.getFirstName());
            lastNameField.setText(userSignup.getLastName());
            EmailField.setText(userSignup.getEmail());
            phoneNumberField.setText(userSignup.getPhoneNumber());

            setProfileImage(userSignup, 55f);
        } catch (Exception e) {
            System.out.println(e + " exception");
        }
    }


    public void editButtonOnClick(MouseEvent event) {
        firstNameField.setEditable(true);
        lastNameField.setEditable(true);
        EmailField.setEditable(true);
        phoneNumberField.setEditable(true);

        editButton.setVisible(false);
        updateButton.setVisible(true);
        cancelButton.setVisible(true);
    }

    public void cancelButtonOnClick(MouseEvent event) {
        firstNameField.setText(userSignup.getFirstName());
        lastNameField.setText(userSignup.getLastName());
        EmailField.setText(userSignup.getEmail());
        phoneNumberField.setText(userSignup.getPhoneNumber());

        firstNameField.setEditable(false);
        lastNameField.setEditable(false);
        EmailField.setEditable(false);
        phoneNumberField.setEditable(false);

        updateButton.setVisible(false);
        cancelButton.setVisible(false);
        editButton.setVisible(true);
        notice.setVisible(false);
    }


    public Boolean validateChanges(){
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = EmailField.getText();
        String phoneNumber = phoneNumberField.getText();

        //check no fields are empty
        String blankError = Validation.anyBlank(firstName, lastName, email, phoneNumber);
        if (blankError != null) {
            notice.setText(blankError);
            notice.setVisible(true);
            notice.setFill(Color.RED);
            return false;
        }

        //check for correct email format
        String emailError = Validation.validateEmail(EmailField.getText());
        if (emailError != null) {
            notice.setText(emailError);
            notice.setVisible(true);
            notice.setFill(Color.RED);
            return false;
        }

        //check for correct phone number format
        String phoneNumberError = Validation.validatePhoneNumber(phoneNumberField.getText());
        if (phoneNumberError != null) {
            notice.setText(phoneNumberError);
            notice.setVisible(true);
            notice.setFill(Color.RED);
            return false;
        }

        return true;
    }

    public void updateButtonOnClick() {
        if (validateChanges()) {
            userSignup.setFirstName(firstNameField.getText());
            userSignup.setLastName(lastNameField.getText());
            userSignup.setEmail(EmailField.getText());
            userSignup.setPhoneNumber(phoneNumberField.getText());

            try {
                userSignupDAO.updateUser(userSignup);

                // disable fields again
                firstNameField.setEditable(false);
                lastNameField.setEditable(false);
                EmailField.setEditable(false);
                phoneNumberField.setEditable(false);

                updateButton.setVisible(false);
                cancelButton.setVisible(false);
                editButton.setVisible(true);

                notice.setText("Changes saved successfully.");
                notice.setVisible(true);
                notice.setFill(Color.GREEN);
            } catch (SQLException e) {
                notice.setText("An error occurred.");
                notice.setVisible(true);
                notice.setFill(Color.RED);
                System.out.println("Update failed: " + e);
            }
        }
    }
}