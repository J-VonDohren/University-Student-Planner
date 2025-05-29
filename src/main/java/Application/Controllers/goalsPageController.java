package Application.Controllers;

import Application.AI_model;
import Application.Database.UserCollectedData;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class goalsPageController extends sceneLoaderController {
    //list of boolean values arranged like the following: [hoursStudiedKPI, GPA KPI, attendanceKPI, hoursStudiedGoal, GPAGoal, attendanceRateGoal]
    //true represents is valid and needs to be updated in the db
    boolean[] validatedChanges = new boolean[6]; // size set to 6 as there are only 6 text fields to validate

    //FXML page components
    @FXML Text hoursStudiedKPI;
    @FXML Text hoursStudiedGoal;
    @FXML Text GPAKPI;
    @FXML Text GPAGoal;
    @FXML Text attendanceKPI;
    @FXML Text attendanceRateGoal;
    @FXML Label editingErrorText;
    @FXML Button editGoalButton;
    @FXML Button updateUserDataButton;
    @FXML Button cancelChangesButton;
    @FXML Button saveChangesButton;

    @FXML Rectangle hoursStudiedKPIContainer;
    @FXML Rectangle GPAKPIContainer;
    @FXML Rectangle attendanceKPIContainer;
    @FXML Rectangle hoursStudiedGoalContainer;
    @FXML Rectangle GPAGoalContainer;
    @FXML Rectangle attendanceRateGoalContainer;

    @FXML TextField editHoursStudiedKPI;
    @FXML TextField editGPAKPI;
    @FXML TextField editAttendanceKPI;
    @FXML TextField editHoursStudiedGoal;
    @FXML TextField editGPAGoal;
    @FXML TextField editAttendanceRateGoal;

    @FXML LineChart<String, Double> hoursStudiedGraph;
    @FXML CategoryAxis hoursStudiedXAxis;
    @FXML NumberAxis hoursStudiedYAxis;

    @FXML LineChart<String,Double> GPAGraph;
    @FXML CategoryAxis GPAXAxis;
    @FXML NumberAxis GPAYAxis;

    @FXML LineChart<String, Double> attendanceGraph;
    @FXML CategoryAxis attendanceXAxis;
    @FXML NumberAxis attendanceYAxis;

    @FXML TextArea AIGeneratedGoalSuggestion;
    public AI_model model = new AI_model();

    public void initialize() {
        super.initialize();
        try{
        //get the info on the current user
        ResultSet resultSet = findUsersLatestRecords(currentUserNumber);
        //create a row instance to access the data
            UserCollectedData userCollectedData = new UserCollectedData(
                resultSet.getString("studentNumber"),
                resultSet.getString("dateModified"),
                resultSet.getInt("gpa"),
                resultSet.getInt("gpaGoal"),
                resultSet.getFloat("hoursStudied"),
                resultSet.getFloat("hoursStudiedGoal"),
                resultSet.getFloat("attendanceRate"),
                resultSet.getFloat("attendanceRateGoal"),
                resultSet.getString("unitsEnrolled")
                );
        //set the labels to the data
        initialiseTextFields(userCollectedData);
        displayDataChangesOverTime();

        Runnable AItask = new Runnable() {
            @Override
            public void run() {
                generateAIRecommendedGoals();
            }
        };
        Thread AIthread=new Thread(AItask);
        AIthread.start();

        resultSet.close();
        }catch(Exception e) {
        System.out.println(e);
        }
    }

    public void initialiseTextFields(UserCollectedData userCollectedData){
        // if statements check whether there is data collected for respective components to be shown
        if(userCollectedData.getHoursStudied() != 0f) {
        hoursStudiedKPI.setText("Studied: " + userCollectedData.getHoursStudied() + " hours");
        }
        if(userCollectedData.getHoursStudiedGoal() != 0f) {
            hoursStudiedGoal.setText("Study goal: " + userCollectedData.getHoursStudiedGoal());
        }
        if(userCollectedData.getGpa() != 0) {
            GPAKPI.setText("Acheived: " + userCollectedData.getGpa() + " GPA");
        }
        if(userCollectedData.getGpaGoal() != 0) {
            GPAGoal.setText("GPA goal: " + userCollectedData.getGpaGoal());
        }
        if(userCollectedData.getAttendanceRate() != 0f) {
            attendanceKPI.setText("Acheived: " + userCollectedData.getAttendanceRate() + "% attendance");
        }
        if(userCollectedData.getAttendanceRateGoal() != 0f) {
            attendanceRateGoal.setText("Attendance goal: " + userCollectedData.getAttendanceRateGoal() + "%");
        }
    }

    public void showGoals(){
        hoursStudiedGoal.setVisible(true);
        hoursStudiedGoalContainer.setVisible(true);
        GPAGoal.setVisible(true);
        GPAGoalContainer.setVisible(true);
        attendanceRateGoal.setVisible(true);
        attendanceRateGoalContainer.setVisible(true);
    }
    public void hideGoals(){
        hoursStudiedGoal.setVisible(false);
        hoursStudiedGoalContainer.setVisible(false);
        GPAGoal.setVisible(false);
        GPAGoalContainer.setVisible(false);
        attendanceRateGoal.setVisible(false);
        attendanceRateGoalContainer.setVisible(false);
    }
    public void showEditingGoals(){
        editHoursStudiedGoal.setVisible(true);
        editGPAGoal.setVisible(true);
        editAttendanceRateGoal.setVisible(true);
    }
    public void hideEditingGoals(){
        editHoursStudiedGoal.setVisible(false);
        editGPAGoal.setVisible(false);
        editAttendanceRateGoal.setVisible(false);
    }
    public void hideData(){
        hoursStudiedKPI.setVisible(false);
        hoursStudiedKPIContainer.setVisible(false);
        GPAKPI.setVisible(false);
        GPAKPIContainer.setVisible(false);
        attendanceKPI.setVisible(false);
        attendanceKPIContainer.setVisible(false);
    }
    public void showData(){
        hoursStudiedKPI.setVisible(true);
        hoursStudiedKPIContainer.setVisible(true);
        GPAKPI.setVisible(true);
        GPAKPIContainer.setVisible(true);
        attendanceKPI.setVisible(true);
        attendanceKPIContainer.setVisible(true);
    }
    public void showEditingData(){
        editHoursStudiedKPI.setVisible(true);
        editGPAKPI.setVisible(true);
        editAttendanceKPI.setVisible(true);
    }
    public void hideEditingData(){
        editHoursStudiedKPI.setVisible(false);
        editGPAKPI.setVisible(false);
        editAttendanceKPI.setVisible(false);
    }

    public void updateData(){
        hideGoals();
        hideData();
        showEditingData();
        editGoalButton.setVisible(false);
        updateUserDataButton.setVisible(false);
        cancelChangesButton.setVisible(true);
        saveChangesButton.setVisible(true);
    }

    public void editGoals(){
        hideGoals();
        hideData();
        showEditingGoals();
        editGoalButton.setVisible(false);
        updateUserDataButton.setVisible(false);
        cancelChangesButton.setVisible(true);
        saveChangesButton.setVisible(true);

    }
    public void resetTextFields(){
        editGPAKPI.setText("");
        editAttendanceKPI.setText("");
        editHoursStudiedKPI.setText("");
        editGPAGoal.setText("");
        editAttendanceRateGoal.setText("");
        editHoursStudiedGoal.setText("");
    }
    public void cancelChanges(){
        hideEditingData();
        hideEditingGoals();
        showData();
        showGoals();
        editingErrorText.setText(""); // removes error messages
        editGoalButton.setVisible(true);
        updateUserDataButton.setVisible(true);
        cancelChangesButton.setVisible(false);
        saveChangesButton.setVisible(false);
        resetTextFields();
    }

    public void checkIfTextFieldsNull(){
        Arrays.fill(validatedChanges, true);
        //indexing is based on how the array is sorted: [hoursStudiedKPI, GPAKPI, attendanceKPI, hoursStudiedGoal, GPAGoal, attendanceRateGoal]
        if(editHoursStudiedKPI.getText().isEmpty()){
            validatedChanges[0] = false;
        }
        if(editGPAKPI.getText().isEmpty()){
            validatedChanges[1] = false;
        }
        if(editAttendanceKPI.getText().isEmpty()){
            validatedChanges[2] = false;
        }
        if(editHoursStudiedGoal.getText().isEmpty()){
            validatedChanges[3] = false;
        }
        if(editGPAGoal.getText().isEmpty()){
            validatedChanges[4] = false;
        }
        if(editAttendanceRateGoal.getText().isEmpty()){
            validatedChanges[5] = false;
        }
    }
    public static boolean isInteger(String stringToCheck) {
        try {
            Integer.parseInt(stringToCheck);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isFloat(String stringToCheck) {
        try {
            Float.parseFloat(stringToCheck);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void checkRequiredNumerics() {
        //indexing is based on how the array is sorted: [hoursStudiedKPI, GPAKPI, attendanceKPI, hoursStudiedGoal, GPAGoal, attendanceRateGoal]
        // check if they aren't integers because if they have passed the previous checks they should be set to true already
        if (isFloat(editHoursStudiedKPI.getText()) == false) {
            validatedChanges[0] = false;
        }
        if (isInteger(editGPAKPI.getText()) == false) {
            validatedChanges[1] = false;
        }
        if (isFloat(editAttendanceKPI.getText()) == false) {
            validatedChanges[2] = false;
        }
        if (isFloat(editHoursStudiedGoal.getText()) == false) {
            validatedChanges[3] = false;
        }
        if (isInteger(editGPAGoal.getText()) == false) {
            validatedChanges[4] = false;
        }
        if (isFloat(editAttendanceRateGoal.getText()) == false) {
            validatedChanges[5] = false;
        }
    }
    public boolean verifyUsersChanges(){
        checkIfTextFieldsNull();
        int truthCount = 0;
        for(int i = 0; i < validatedChanges.length; i++){
            if(validatedChanges[i] == false){
                continue;
            }
            else{
                truthCount++;
                continue;
            }
        }
        if(truthCount == 0){
            return false;
        }
        truthCount = 0;
        checkRequiredNumerics();
        for(int i = 0; i < validatedChanges.length; i++){
            if(validatedChanges[i] == false){
                continue;
            }
            else{
                truthCount++;
                continue;
            }
        }
        if(truthCount == 3){
            return true;
        }
        return false;
    }

    public ResultSet findUsersLatestRecords(String currentUsersNumber){
        try{
            String searchQuery = "SELECT * FROM User_Collected_Data WHERE studentNumber = ? ORDER BY datetime(dateModified) DESC LIMIT 1;";
            PreparedStatement statement = userCollectedDataDAO.getDBConnection().prepareStatement(searchQuery);
            statement.setString(1, currentUsersNumber);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendChangesToDB(String currentUsersNumber) throws SQLException {
        try{
        //indexing is based on how the array is sorted: [hoursStudiedKPI, GPAKPI, attendanceKPI, hoursStudiedGoal, GPAGoal, attendanceRateGoal]
        ResultSet usersCurrentData = findUsersLatestRecords(currentUsersNumber);
        String newEntriesDateModified = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).toString();
        String newEntriesHoursStudiedKPI = usersCurrentData.getString("HoursStudied") != null ? usersCurrentData.getString("HoursStudied") : "0";
        String newEntriesHoursStudiedGoal = usersCurrentData.getString("HoursStudiedGoal") != null ? usersCurrentData.getString("HoursStudiedGoal") : "0";
        String newEntriesGPAKPI = usersCurrentData.getString("GPA") != null ? usersCurrentData.getString("GPA") : "0";
        String newEntriesGPAGoal = usersCurrentData.getString("GPAGoal") != null ? usersCurrentData.getString("GPAGoal") : "0";
        String newEntriesAttendanceRateKPI = usersCurrentData.getString("AttendanceRate") != null ? usersCurrentData.getString("AttendanceRate") : "0";
        String newEntriesAttendanceGoal = usersCurrentData.getString("AttendanceRateGoal")!= null ? usersCurrentData.getString("AttendanceRateGoal") : "0";
        String newEntriesEnrolledUnits = usersCurrentData.getString("UnitsEnrolled") != null ? usersCurrentData.getString("UnitsEnrolled") : " ";

        for(int i = 0; i < validatedChanges.length; i++){
            if(i == 0 && validatedChanges[i] == true){
                newEntriesHoursStudiedKPI = editHoursStudiedKPI.getText();
            }
            if(i == 1 && validatedChanges[i] == true){
                newEntriesGPAKPI = editGPAKPI.getText();
            }
            if(i == 2 && validatedChanges[i] == true){
                newEntriesAttendanceRateKPI = editAttendanceKPI.getText();
            }
            if(i == 3 && validatedChanges[i] == true){
                newEntriesHoursStudiedGoal = editHoursStudiedGoal.getText();
            }
            if(i == 4 && validatedChanges[i] == true){
                newEntriesGPAGoal = editGPAGoal.getText();
            }
            if(i == 5 && validatedChanges[i] == true){
                newEntriesAttendanceGoal = editAttendanceRateGoal.getText();
            }
        }
            UserCollectedData newUserData = new UserCollectedData(currentUsersNumber, newEntriesDateModified, Integer.parseInt(newEntriesGPAKPI), Integer.parseInt(newEntriesGPAGoal), Float.parseFloat(newEntriesHoursStudiedKPI), Float.parseFloat(newEntriesHoursStudiedGoal), Float.parseFloat(newEntriesAttendanceRateKPI), Float.parseFloat(newEntriesAttendanceGoal), newEntriesEnrolledUnits);
            userCollectedDataDAO.insertData(newUserData);
            initialiseTextFields(newUserData);
            usersCurrentData.close();
        }catch (SQLException e){
            System.out.println("SQL Exception.");
        }
    }

    public void saveChanges() throws Exception {
        boolean updatedUser = verifyUsersChanges();
        if (updatedUser == true){
            sendChangesToDB(currentUserNumber);
            displayDataChangesOverTime(); // populates the line charts with the new data
            hideEditingData();
            hideEditingGoals();
            showData();
            showGoals();
            editGoalButton.setVisible(true);
            updateUserDataButton.setVisible(true);
            cancelChangesButton.setVisible(false);
            saveChangesButton.setVisible(false);
            resetTextFields();
            editingErrorText.setText(""); // don't want to display any error when the user has made it successful if an error was made previously
            switchToGoalsPage();
        }
        else {
            //doesn't change visibility of any components
            editingErrorText.setText("Changes are not valid.");
            resetTextFields();
        }
    }

    public void displayDataChangesOverTime(){
        try{
            hoursStudiedGraph.getData().removeAll(); // removes all previous series added to the chart
            GPAGraph.getData().removeAll();
            attendanceGraph.getData().removeAll();

            String searchQuery = "SELECT strftime('%d/%m', dateModified) AS formattedDate, " +
                            "AVG(GPA) AS GPA, " +
                            "AVG(HoursStudied) AS HoursStudied, " +
                            "AVG(AttendanceRate) AS AttendanceRate, " +
                            "AVG(GPAGoal) AS GPAGoal, " +
                            "AVG(HoursStudiedGoal) AS HoursStudiedGoal, " +
                            "AVG(AttendanceRateGoal) AS AttendanceRateGoal " +
                            "FROM User_Collected_Data " +
                            "WHERE studentNumber = ? " +
                            "GROUP BY strftime('%d/%m', dateModified) " +
                            "ORDER BY strftime('%d/%m', dateModified);";

            PreparedStatement statement = userCollectedDataDAO.getDBConnection().prepareStatement(searchQuery);
            statement.setString(1, currentUserNumber);
            ResultSet resultSet = statement.executeQuery();

            XYChart.Series<String, Double> hoursStudiedSeries = new XYChart.Series();
            XYChart.Series<String, Double> GPASeries = new XYChart.Series();
            XYChart.Series<String, Double> attendanceSeries = new XYChart.Series();

            while(resultSet.next()){
                String formattedDate = resultSet.getString("formattedDate");

                // Convert dateModified to desired format
                hoursStudiedSeries.getData().add(new XYChart.Data(formattedDate, Double.parseDouble(resultSet.getString("HoursStudied"))));
                GPASeries.getData().add(new XYChart.Data(formattedDate, Double.parseDouble(resultSet.getString("GPA"))));
                attendanceSeries.getData().add(new XYChart.Data(formattedDate, Double.parseDouble(resultSet.getString("AttendanceRate"))));
            }

            hoursStudiedGraph.getData().addAll(hoursStudiedSeries);
            GPAGraph.getData().addAll(GPASeries);
            attendanceGraph.getData().addAll(attendanceSeries);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void generateAIRecommendedGoals(){
        String currentGoals = ""; // pulls the data in to parse to the ai
        String currentData = "";
        String promptText = "can you suggest what can be improved given the following information:" + currentGoals + currentData + "with a text limit of 200 characters including spaces";

        model.promptAI(promptText);
        AIGeneratedGoalSuggestion.setText(model.promptAI(promptText));
    }

}
