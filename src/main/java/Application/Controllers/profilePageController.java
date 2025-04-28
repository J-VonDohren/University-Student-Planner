package Application.Controllers;

import Application.Database.DatabaseConnection;
import Application.Database.UserSignup;
import Application.Database.UserSignupDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    @FXML private Label firstName;
    @FXML private Label lastName;
    @FXML private Label Email;
    @FXML private Label phoneNumber;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField EmailField;
    @FXML private TextField phoneNumberField;
    @FXML private Button editButton;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private Text notice;
    @FXML private Circle profileBackGround;
    @FXML private Text profileText;
    private UserSignup userSignup;

    //making this a function so it can be reused in other pages
    public void setProfileImage(UserSignup user){
        //set profileImg placeholder
        //get a random seed from the userNumber so the color is different for each user
        Random random = new Random(Long.parseLong(user.getStudentNumber().replaceAll("[^0-9]","")));
        int[] colors={random.nextInt(1,255),random.nextInt(1,255),random.nextInt(1,255)};
        //if all three are too high make one lower so the background doesn't hide the text
        int brightColors=0;
        for (int color :colors){if (color > 220){brightColors+=1;}}
        if (brightColors == 3){colors[random.nextInt(1,4)]=random.nextInt(1,200);}
        profileBackGround.setFill(Color.rgb(colors[0],colors[1],colors[2]));
        profileText.setText(user.getFirstName().substring(0,1).toUpperCase()
                +user.getLastName().substring(0,1).toUpperCase());
    }

    public void initialize(){
        try {
            //get the info on the current user
            String profileInfoQuery = "SELECT * FROM User_Signup_Data where StudentNumber = ?";
            PreparedStatement statement = userSignupDAO.getDBConnection().prepareStatement(profileInfoQuery);
            statement.setString(1,currentUserNumber);
            ResultSet resultSet = statement.executeQuery();

            //make a UserSignup instance to use for this page
            userSignup = new UserSignup(currentUserNumber
                    ,resultSet.getString("FirstName")
                    ,resultSet.getString("LastName")
                    ,resultSet.getString("Email")
                    ,resultSet.getString("PhoneNumber")
                    ,resultSet.getString("LoginPassword"));
            //set labels to user info
            firstName.setText("First name: "+userSignup.getFirstName());
            lastName.setText("Last Name: "+userSignup.getLastName());
            Email.setText("Email: "+userSignup.getEmail());
            phoneNumber.setText("Phone number: "+userSignup.getPhoneNumber());

            setProfileImage(userSignup);
        }catch(Exception e){
            System.out.println(e + "exception");
        }
    }

    public void editButtonOnClick(MouseEvent click) {
        //hide labels and button
        firstName.setVisible(false);
        lastName.setVisible(false);
        Email.setVisible(false);
        phoneNumber.setVisible(false);
        editButton.setVisible(false);
        //show elements for editing form
        firstNameField.setVisible(true);
        lastNameField.setVisible(true);
        EmailField.setVisible(true);
        phoneNumberField.setVisible(true);
        cancelButton.setVisible(true);
        updateButton.setVisible(true);
        //set fields to saved values when opening (done everytime so changes made before hitting cancel are forgotten)
        firstNameField.setText(userSignup.getFirstName());
        lastNameField.setText(userSignup.getLastName());
        EmailField.setText(userSignup.getEmail());
        phoneNumberField.setText(userSignup.getPhoneNumber());
    }

    private  void hideForm(){
        //show labels and button
        firstName.setVisible(true);
        lastName.setVisible(true);
        Email.setVisible(true);
        phoneNumber.setVisible(true);
        editButton.setVisible(true);
        //hide elements for editing form
        firstNameField.setVisible(false);
        lastNameField.setVisible(false);
        EmailField.setVisible(false);
        phoneNumberField.setVisible(false);
        cancelButton.setVisible(false);
        updateButton.setVisible(false);
        notice.setVisible(false);
    }

    public void cancelButtonOnClick(MouseEvent click) {
        hideForm();
    }

    public Boolean validateChanges(){
        String[] fields = {firstNameField.getText(),lastNameField.getText(),EmailField.getText(),phoneNumberField.getText()};
        //check no fields are empty
        for (String value : fields){
            if (value.isEmpty()){
                notice.setText("don't leave fields empty.");
                notice.setVisible(true);
                notice.setFill(Color.RED);
                return false;
            }
        }
        //check fields have been changed
        String[] currentValues = {userSignup.getFirstName(),userSignup.getLastName()
                ,userSignup.getEmail(),userSignup.getPhoneNumber()};
        if (Arrays.equals(fields,currentValues)) {
            notice.setText("no fields have been changed.");
            notice.setVisible(true);
            notice.setFill(Color.RED);
            return false;
        }
        //check for correct email format
        if (!EmailField.getText().matches(".+@.+")) {
            notice.setText("invalid email.");
            notice.setVisible(true);
            notice.setFill(Color.RED);
            return false;
        }
        return true;
    }

    public void updateButtonOnClick() {
        if (validateChanges()){
            userSignup.setFirstName(firstNameField.getText());
            userSignup.setLastName(lastNameField.getText());
            userSignup.setEmail(EmailField.getText());
            userSignup.setPhoneNumber(phoneNumberField.getText());

            try {
                userSignupDAO.updateUser(userSignup);

                hideForm();
                firstName.setText("First name: "+userSignup.getFirstName());
                lastName.setText("Last Name: "+userSignup.getLastName());
                Email.setText("Email: "+userSignup.getEmail());
                phoneNumber.setText("Phone number: "+userSignup.getPhoneNumber());
                setProfileImage(userSignup);

                notice.setText("changes saved successfully.");
                notice.setVisible(true);
                notice.setFill(Color.GREEN);
            }catch (SQLException e) {
                notice.setText("an error has occurred.");
                notice.setVisible(true);
                notice.setFill(Color.RED);
                System.out.println("Update failed: "+e);
            }
        }
    }
    

}