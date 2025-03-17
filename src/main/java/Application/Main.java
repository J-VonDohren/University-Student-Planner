package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage LogInStage) throws Exception {
        Parent LoginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Login-Page.fxml")));
        LogInStage.initStyle(StageStyle.UNDECORATED);
        LogInStage.setScene(new Scene(LoginRoot));
        LogInStage.getIcons().add(new Image("Img/QUT-Logo.jpg"));
        LogInStage.show();
    }
}
//Test Jira