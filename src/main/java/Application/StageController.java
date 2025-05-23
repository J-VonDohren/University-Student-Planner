package Application;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;

public class StageController {
    public Stage applicationStage = new Stage();

    public Stage getApplicationStage(){
        return applicationStage;
    }
    public void formatStage(){
        try {
            String imagePath = "/Img/QUT-Logo.jpg";
            URL imageUrl = StageController.class.getResource(imagePath);

            if (imageUrl == null) {
                throw new IllegalArgumentException("Image file not found: " + imagePath);
            }

            applicationStage.getIcons().add(new Image(imageUrl.toExternalForm()));
            applicationStage.setMinWidth(450);
            applicationStage.setMinHeight(300);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading stage icon.");
        }
    }
    public static void closeActiveStage(){
        Platform.runLater(() -> {
            for (Window window : Stage.getWindows()) {
                if (window instanceof Stage) {
                    ((Stage) window).close();
                }
            }
        });
    }
}
