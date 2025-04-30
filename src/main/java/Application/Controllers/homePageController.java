package Application.Controllers;

import Application.AI_model;
import Application.Database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.MenuItem;
import java.util.HashMap;


import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;


public class homePageController extends sceneLoaderController {
    @FXML private TextArea weeklyOverviewAI;
    @FXML private Text motivationalAI;
    @FXML Text MondayOvr, TuesdayOvr, WedOvr, ThursOvr, FriOvr;
    public HashMap<String,String> weekDayHashMap = new HashMap<String, String>();


    public AI_model model = new AI_model();
    @FXML
    public void initialize() {
        super.initialize();
        try {

            weekDayHashMap.put("Monday", "");
            weekDayHashMap.put("Tuesday", "");
            weekDayHashMap.put("Wednesday", "");
            weekDayHashMap.put("Thursday", "");
            weekDayHashMap.put("Friday", "");

            model.initialiseAIModel();

            weeklyOverviewAI.setEditable(false);
            getWeeklyOverview();

            populateWidgets();

            getMotivationalQuote();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getWeeklyOverview(){
        String weeklyInfo = "Monday: 2 hours studied; Tuesday: 3 tutorials attended, 1 practical attended"; // pulls the timetable data in json format to parse to the ai
        String promptText = "can you generate a weekly overview excluding any special characters, a heading and final query messages, including what went well and what can be improved given the following information:" + weeklyInfo + "with a text limit of 200 characters including spaces";

        model.promptAI(promptText);
        String response = model.promptAI(promptText);
        weeklyOverviewAI.setText(response);
    }

    public void getMotivationalQuote(){
        String promptText = "Can you give me a random motivational quote related to studying. Just give me the quote and the author if known, nothing else";
        model.promptAI(promptText);
        String response = model.promptAI(promptText);
        motivationalAI.setText(response);
    }

    public void populateWidgets(){
        var weekdays = weekDayHashMap.keySet();
        for (String weekday : weekdays) {
            String promptText = "give me a dot point breakdown of what i need to do this week on and keep it strictly academic. Limit to 3 dot points, 3-4 words per dot points, use - to start a dot point. Do not use emoji, unnecessary special symbols, no header. Just straight concise dot points.";
            String response = model.promptAI(promptText);

            if (weekday.equals("Monday")) {
                MondayOvr.setText(response);
            } else if (weekday.equals("Tuesday")) {
                TuesdayOvr.setText(response);
            } else if (weekday.equals("Wednesday")) {
                WedOvr.setText(response);
            } else if (weekday.equals("Thursday")) {
                ThursOvr.setText(response);
            } else if (weekday.equals("Friday")) {
                FriOvr.setText(response);
            }
        }
    }
}