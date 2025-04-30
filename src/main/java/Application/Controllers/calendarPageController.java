package Application.Controllers;

import Application.Database.UserTimetable;
import Application.Database.UserTimetableDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.RadioButton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.util.converter.NumberStringConverter;

import javax.swing.*;
import javax.tools.Tool;

public class calendarPageController extends sceneLoaderController {
    ArrayList<UserTimetable> events=new ArrayList<UserTimetable>();
    private Integer month;
    private Integer year;
    @FXML private GridPane calendar;
    @FXML private StackPane monthDecrementButton;
    @FXML private StackPane monthIncrementButton;
    @FXML private Text monthText;
    @FXML private StackPane form;
    @FXML private Rectangle background;
    @FXML private TextField nameField;
    @FXML private ComboBox typeSelect;
    @FXML private DatePicker startDateSelect;
    @FXML private Spinner startHourSpinner;
    @FXML private Spinner startMinuteSpinner;
    @FXML private DatePicker endDateSelect;
    @FXML private Spinner endHourSpinner;
    @FXML private Spinner endMinuteSpinner;
    @FXML private TextField eventLocationField;
    @FXML private Text formNotice;
    @FXML private Text attendanceLabel;
    @FXML private RadioButton attendanceButtonYes;
    @FXML private RadioButton attendanceButtonNo;
    @FXML private ToggleGroup attendance;
    @FXML private Button addEventButton;
    @FXML private Button editEventButton;
    @FXML private Button deleteEventButton;


    public void displayMonth(){
        //update month text
        monthText.setText(year+" "+java.time.Month.of(month));

        //clear the days from the calendar
        ArrayList<javafx.scene.Node> children=new ArrayList<javafx.scene.Node>(calendar.getChildren());
        for (javafx.scene.Node i: children){
            if (i.getTypeSelector().equals("StackPane")){
                calendar.getChildren().remove(i);
            }
        }
        //reset rowConstraints
        calendar.getRowConstraints().clear();
        calendar.getRowConstraints().add(new RowConstraints());
        calendar.getRowConstraints().get(0).setMinHeight(20);
        calendar.getRowConstraints().add(new RowConstraints());
        calendar.getRowConstraints().get(1).setMinHeight(20);

        //add days to calendar
        Integer week = 0;
        Integer dayOfMonth=1;
        Integer dayOfWeek=LocalDate.of(year,month,1).getDayOfWeek().getValue();
        for (int i=1; i <= java.time.Month.of(month).length(LocalDate.now().getYear()%4==0) ;i++){
            StackPane day = new StackPane();
            calendar.add(day, dayOfWeek-1, week);
            Text date = new Text(dayOfMonth.toString());
            //if this is the current day change it's color to highlight it
            if (year.equals(LocalDate.now().getYear()) && month.equals(LocalDate.now().getMonth().getValue())
                    && dayOfMonth.equals(LocalDate.now().getDayOfMonth()) ){
                day.setStyle("-fx-background-color:LIGHTSKYBLUE");
            }
            day.getChildren().add(date);

            //this count is used to determine how low text will be placed so it does not overlap
            ArrayList<StackPane> eventBlocks = new ArrayList<StackPane>();
            for (UserTimetable event : events){
                Integer startDay=Integer.parseInt(event.getEventStartDate().substring(8,10));
                Integer startMonth=Integer.parseInt(event.getEventStartDate().substring(5,7));
                Integer startYear=Integer.parseInt(event.getEventStartDate().substring(0,4));
                Integer endDay=Integer.parseInt(event.getEventEndDate().substring(8,10));
                Integer endMonth=Integer.parseInt(event.getEventEndDate().substring(5,7));
                Integer endYear=Integer.parseInt(event.getEventEndDate().substring(0,4));

                if (startMonth.equals(month) && startDay.equals(dayOfMonth) && startYear.equals(year)
                    || endMonth.equals(month) && endDay.equals(dayOfMonth) && endYear.equals(year)){

                    StackPane eventBlock=new StackPane();
                    eventBlock.setStyle("-fx-cursor:hand;");
                    //set up function to load edit form
                    eventBlock.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            form.setVisible(true);
                            background.setVisible(true);
                            attendanceLabel.setVisible(true);
                            attendanceButtonYes.setVisible(true);
                            attendanceButtonNo.setVisible(true);
                            editEventButton.setVisible(true);
                            deleteEventButton.setVisible(true);
                            //set fields to current values
                            nameField.setText(event.getEventName());
                            typeSelect.setValue(event.getEventType());
                            startDateSelect.setValue(LocalDate.parse(event.getEventStartDate().substring(0,10)));
                            startHourSpinner.decrement(24);
                            startHourSpinner.increment(Integer.parseInt(event.getEventStartDate().substring(11,13)));
                            startMinuteSpinner.decrement(60);
                            startMinuteSpinner.increment(Integer.parseInt(event.getEventStartDate().substring(14,16)));
                            endDateSelect.setValue(LocalDate.parse(event.getEventEndDate().substring(0,10)));
                            endHourSpinner.decrement(24);
                            endHourSpinner.increment(Integer.parseInt(event.getEventEndDate().substring(11,13)));
                            endMinuteSpinner.decrement(60);
                            endMinuteSpinner.increment(Integer.parseInt(event.getEventEndDate().substring(14,16)));
                            eventLocationField.setText(event.getEventLocation());
                            if (event.getEventAttendance()==1){attendance.selectToggle(attendanceButtonYes);
                            }else{attendance.selectToggle(attendanceButtonNo);}
                            //set event handler for delete button
                            deleteEventButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    userTimetableDAO.deleteEvent(event.getEventID());
                                    closeForm();    getEvents();    displayMonth();
                                }
                            });
                            //set event handler for edit button
                            editEventButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    Integer shortAttendance;
                                    if (attendance.getSelectedToggle()==attendanceButtonYes){shortAttendance=1;}else{shortAttendance=0;}
                                    UserTimetable updatedEvent=new UserTimetable(event.getEventID(), nameField.getText(),currentUserNumber
                                            ,typeSelect.getValue().toString(),getStartDatetime(),getEndDatetime(),eventLocationField.getText(),shortAttendance);
                                    userTimetableDAO.updateEvent(event.getEventID(),updatedEvent);
                                    closeForm();    getEvents();    displayMonth();
                                }
                            });
                        }
                    });

                    Rectangle background=new Rectangle();
                    switch (event.getEventType()){
                        case "Study period":
                            background.setFill(Color.BLUE);
                            break;
                        case "Work schedule":
                            background.setFill(Color.GREEN);
                            break;
                        case "Food break":
                            background.setFill(Color.ORANGE);
                            break;
                        case "Assignment":
                            background.setFill(Color.RED);
                            break;
                    }
                    background.setHeight(20);
                    background.setWidth(76);
                    eventBlock.getChildren().add(background);

                    //check length of text and if it's to long cut it of so it does not overfill space
                    Text eventText = new Text();
                    if (event.getEventName().length() < 13){
                        eventText.setText(event.getEventName());
                    }else{eventText.setText(event.getEventName().substring(0,10)+"...");}

                    eventText.setFill(Color.WHITE);
                    eventText.setWrappingWidth(76);
                    eventBlock.getChildren().add(eventText);

                    String location="";
                    if (event.getEventLocation()!=null && !event.getEventLocation().equals("")){
                        location="\nLocation: "+event.getEventLocation();
                    }
                    String attendance;
                    if (event.getEventAttendance()==1){
                        attendance="\nAttendance: yes";
                    }
                    else{
                        attendance="\nAttendance: no";
                    }

                    Tooltip details=new Tooltip("Name: "+event.getEventName()
                            +"\nType: "+event.getEventType()
                            +"\nStart Datetime: "+event.getEventStartDate()
                            +"\nEnd Datetime: "+event.getEventEndDate()
                            +attendance+location);
                    details.setMinWidth(100);
                    details.setHideDelay(Duration.ZERO);
                    details.setShowDelay(Duration.ZERO);
                    Tooltip.install(eventBlock,details);
                    eventBlock.setMaxHeight(20);

                    day.getChildren().add(eventBlock);
                    eventBlocks.add(eventBlock);

                    //only change the rowHeight if it's not already big enough
                    if ((eventBlocks.size()+1)*20 > calendar.getRowConstraints().get(week).getMinHeight()) {
                        calendar.getRowConstraints().get(week).setMinHeight(
                                calendar.getRowConstraints().get(week).getMinHeight() + 20);
                    }
                }
            }
            if (eventBlocks.size() >0){
                date.setTranslateY((-calendar.getRowConstraints().get(week).getMinHeight()/2) +9);
                Integer textCount=1;
                for (StackPane eventBlock: eventBlocks){
                    eventBlock.setTranslateY(((-calendar.getRowConstraints().get(week).getMinHeight()/2) +9)
                            +(textCount * 20));
                    textCount+=1;
                }
            }


            dayOfWeek+=1;
            if (dayOfWeek> 7){dayOfWeek=1;
                week+=1;
                calendar.getRowConstraints().add(new RowConstraints());
                calendar.getRowConstraints().get(week).setMinHeight(20);
            }
            dayOfMonth+=1;
        }

    }

    public void getEvents(){
        events=new ArrayList<UserTimetable>();
        try {
            String profileInfoQuery = "SELECT * FROM User_Timetable_Data where StudentNumber = ?";
            PreparedStatement statement = userSignupDAO.getDBConnection().prepareStatement(profileInfoQuery);
            statement.setString(1,currentUserNumber);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                events.add(new UserTimetable(resultSet.getInt("EventID")
                        ,resultSet.getString("EventName")
                        ,currentUserNumber
                        ,resultSet.getString("EventType")
                        ,resultSet.getString("EventStartDatetime")
                        ,resultSet.getString("EventEndDatetime")
                        ,resultSet.getString("EventLocation")
                        ,resultSet.getInt("EventAttendance")));
            }


        }catch(Exception e){
            System.out.println(e + "exception");
        }
    }

    public void initialize(){
        super.initialize();
        getEvents();
        year=LocalDate.now().getYear();
        month=LocalDate.now().getMonth().getValue();
        displayMonth();
    }

    public void decrementMonth(){
        if (month == 1){
            month=12;
            year-=1;
        }
        else{month-=1;}
        displayMonth();
    }

    public void incrementMonth(){
        if (month == 12){
            month=1;
            year+=1;
        }
        else{month+=1;}
        displayMonth();
    }

    public void showAddEventForm(){
        //reset form
        nameField.setText("");
        typeSelect.setValue("");
        startDateSelect.setValue(null);
        startHourSpinner.decrement(24);
        startMinuteSpinner.decrement(60);
        endDateSelect.setValue(null);
        endHourSpinner.decrement(24);
        endMinuteSpinner.decrement(60);
        eventLocationField.setText("");

        form.setVisible(true);
        background.setVisible(true);
        addEventButton.setVisible(true);
    }

    public void closeForm(){
        form.setVisible(false);
        background.setVisible(false);
        formNotice.setVisible(false);
        attendanceLabel.setVisible(false);
        attendanceButtonYes.setVisible(false);
        attendanceButtonNo.setVisible(false);
        addEventButton.setVisible(false);
        editEventButton.setVisible(false);
        deleteEventButton.setVisible(false);
    }

    //this function does all input checks except checking for duplicates so it can be used for adding and editing events
    public String checkEventInputs(){
        //check all necessary fields are filled out, doesn't check hour or minute since spinners can't be set to black
        if (nameField.getText().equals("") || typeSelect.getValue().equals("") ||
                startDateSelect.getValue()==null || endDateSelect.getValue()==null ){
            return "Necessary fields missing";
        }
        //check the format of location is correct (if a location is provided)
        if (!eventLocationField.getText().equals("")) {
            if (!eventLocationField.getText().substring(0, 1).matches("\\D*") ||
                    !eventLocationField.getText().substring(1).matches("\\d*")) {
                return "Location format incorrect";
            }
        }
        return null;
    }

    public String getStartDatetime(){
        //get start and end dateTime
        String eventStartDateTime = startDateSelect.getValue().toString()+" ";
        if (Integer.parseInt(startHourSpinner.getValue().toString()) < 10){eventStartDateTime+=startHourSpinner.getValue().toString() + "0:";}
        else{eventStartDateTime+=startHourSpinner.getValue().toString() + ":";}
        if (Integer.parseInt(startMinuteSpinner.getValue().toString()) < 10){eventStartDateTime+=startMinuteSpinner.getValue().toString() + "0:00";}
        else{eventStartDateTime+=startMinuteSpinner.getValue().toString() + ":00";}
        return eventStartDateTime;
    }

    public String getEndDatetime(){
        String eventEndDateTime = endDateSelect.getValue().toString()+" ";
        if (Integer.parseInt(endHourSpinner.getValue().toString()) < 10){eventEndDateTime+=endHourSpinner.getValue().toString() + "0:";}
        else{eventEndDateTime+=endHourSpinner.getValue().toString() + ":";}
        if (Integer.parseInt(endMinuteSpinner.getValue().toString()) < 10){eventEndDateTime+=endMinuteSpinner.getValue().toString() + "0:00";}
        else{eventEndDateTime+=endMinuteSpinner.getValue().toString() + ":00";}
        return eventEndDateTime;
    }

    public void addEvent(){
        String check = checkEventInputs();
        if (check == null){
            String eventStartDateTime=getStartDatetime();
            String eventEndDateTime=getEndDatetime();
            try {
                //get new ID
                String IDsql= "SELECT MAX(EventID) FROM User_Timetable_Data";
                PreparedStatement SQLstmt = userTimetableDAO.getDBConnection().prepareStatement(IDsql);
                Integer newID= SQLstmt.executeQuery().getInt(1)+1;
                //make an event object
                UserTimetable newEvent = new UserTimetable(newID,nameField.getText(),currentUserNumber
                        ,typeSelect.getValue().toString(),eventStartDateTime, eventEndDateTime
                        , eventLocationField.getText(),0);
                userTimetableDAO.insertEvent(newEvent);
                closeForm();
                getEvents();
                displayMonth();
            }catch(Exception e){
                System.out.println(e + "exception");
            }
        }
        else{
            //input validation has an issue display it
            formNotice.setText(check);
            formNotice.setVisible(true);
        }
    }
}