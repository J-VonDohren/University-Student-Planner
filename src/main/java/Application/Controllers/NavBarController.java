package Application.Controllers;

import javafx.scene.control.Button;
import javafx.fxml.FXML;
import java.util.List;

public class NavBarController extends sceneLoaderController {
    @FXML public Button homeButton;
    @FXML public Button goalsButton;
    @FXML public Button mapButton;
    @FXML public Button calendarButton;
    @FXML public Button profileButton;

    protected List<Button> navButtons;

    public void initialize(){
        super.initialize();
        navButtons = List.of(homeButton, goalsButton, mapButton, calendarButton, profileButton);
        setHighLightColour();
    }

    public void setHighLightColour() {
        try {
            // Reset all buttons' style
            for (Button btn : navButtons) {
                btn.setStyle("-fx-background-color: #87CEEB; -fx-text-fill: black;");
            }

            switch (sceneLoaderController.curPage) {
                case "home":
                    homeButton.setStyle("-fx-background-color: #003C72; -fx-text-fill: white;");
                    break;
                case "goals":
                    goalsButton.setStyle("-fx-background-color: #003C72; -fx-text-fill: white;");
                    break;
                case "map":
                    mapButton.setStyle("-fx-background-color: #003C72; -fx-text-fill: white;");
                    break;
                case "calendar":
                    calendarButton.setStyle("-fx-background-color: #003C72; -fx-text-fill: white;");
                    break;
                case "profile":
                    profileButton.setStyle("-fx-background-color: #003C72; -fx-text-fill: white;");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error setting highlight color: " + e.getMessage());
        }
    }
}
