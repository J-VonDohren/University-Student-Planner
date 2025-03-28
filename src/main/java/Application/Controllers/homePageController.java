package Application.Controllers;

import Application.AI_model;
import Application.Database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;


public class homePageController extends sceneLoaderController {
    @FXML private TextArea weeklyOverviewAI;

    public AI_model model = new AI_model();
    @FXML
    public void initialize() {
        try {
            model.initialiseAIModel();
            getWeeklyOverview();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getWeeklyOverview(){
        String weeklyInfo = "Monday: 2 hours studied; Tuesday: 3 tutorials attended, 1 practical attended"; // pulls the timetable data in json format to parse to the ai
        String promptText = "can you generate a weekly overview excluding any special characters, a heading and final query messages, including what went well and what can be improved given the following information:" + weeklyInfo + "with a text limit of 200 characters including spaces";

        model.promptAI(promptText);
        weeklyOverviewAI.setText(AI_model.reponseText);
    }
}