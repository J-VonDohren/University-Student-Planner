package Application.Controllers;

import Application.AI_model;
import Application.Database.UserTimetable;
import Application.Database.UserTimetableDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.Console;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class mapPageController extends sceneLoaderController {
    @FXML public Canvas heatMap;
    @FXML public ListView<String> busyLocationList;
    @FXML public ListView<String> quietLocationList;
    @FXML public TextArea AIMapSummary;
    public enum feedState {LIVE, PREDICTED};
    public feedState current_state = feedState.LIVE;

    public static ArrayList<Event> calenderEvents = new ArrayList<Event>();

    public AI_model model = new AI_model();
    // stores all vital information regarding a building's code, x/y location and classes (in that order)
    /// PLACEHOLDER DATA JUST SO THERE'S SOMETHING TO SHOW
    // todo: WILL BE REPLACED ONCE "User_Signup_Data" IS POPULATED
    public static Building[] CampusBuildings = {
            new Building('P', 254, 100, new ArrayList<Event>()),
            new Building('S', 78, 164, new ArrayList<Event>()),
            new Building('Z', 128, 166, new ArrayList<Event>())
    };

    public static Circle CirclePreset =
            new Circle(25, 15, 51);

    public static class Building {
        Character letterID;
        Integer xPos;
        Integer yPos;
        ArrayList<Event> eventCount;

        public Building(Character letterID, Integer xPos, Integer yPos, ArrayList<Event> eventCount) {
            this.letterID = letterID;
            this.xPos = xPos;
            this.yPos = yPos;
            this.eventCount = eventCount;
        }

        public Character getBlockLetter() {
            return this.letterID;
        }
        public Integer getXPos() {
            return this.xPos;
        }
        public Integer getYPos() {
            return this.yPos;
        }

        public ArrayList<Event> getEventCount() {
            return this.eventCount;
        }
    }

    public static class Circle {
        // initial circle width and ring expansion size, respectively
        Integer circleWidth;
        Integer circleStepValue;
        // hue increase amount per ring (keep in mind that the maximum hue for RGB colors is 255)
        Integer hueStepValue;

        public Circle(Integer circleWidth, Integer circleStepValue, Integer hueStepValue) {
            this.circleWidth = circleWidth;
            this.circleStepValue = circleStepValue;
            this.hueStepValue = hueStepValue;
        }

        public int getWidth() {
            return this.circleWidth;
        }
        public int getStepValue() {
            return this.circleStepValue;
        }
        public int getHueValue() {
            return this.hueStepValue;
        }
    }

    public static class Event {
        Integer eventID;
        String eventName;
        String eventType;
        String eventStartDatetime;
        String eventEndDatetime;
        String eventLocation;
        Integer eventAttendance;

        public Event(Integer eventID, String eventName, String eventType, String eventStartDatetime,
                     String eventEndDatetime, String eventLocation, Integer eventAttendance) {
            this.eventID = eventID;
            this.eventName = eventName;
            this.eventType = eventType;
            this.eventStartDatetime = eventStartDatetime;
            this.eventEndDatetime = eventEndDatetime;
            this.eventLocation = eventLocation;
            this.eventAttendance = eventAttendance;
        }

        public Integer getEventID() { return this.eventID; }
        public String getEventName() {
            return this.eventName;
        }
        public String getEventType() {
            return this.eventType;
        }
        public String getEventStartDatetime() {
            return this.eventStartDatetime;
        }
        public String getEventEndDatetime() {
            return this.eventEndDatetime;
        }
        public String getEventLocation() {
            return this.eventLocation;
        }
        public Integer getEventAttendance() {
            return this.eventAttendance;
        }

        // for wittle bubba gemini that can't chew it's fooood right awwww boo hoo
        /// yes, I'm unreasonably mad at this lol, and no I don't want to talk about it.
        public String pureeEventData() { return String.join(", ", (
                getEventID().toString() +
                getEventName() +
                getEventType() +
                getEventStartDatetime() +
                getEventEndDatetime() +
                getEventLocation() +
                getEventAttendance().toString())); }
    }


    public void renderHeatmap(ArrayList<Event> calender_events) {
        // Reset the canvas
        heatMap.getGraphicsContext2D().clearRect(0.0, 0.0, 302.0, 200.0);
        // The more rooms there are in the building booked, the more layers of circle there are, with each warmer colors.
        for (int i = 0; i < calender_events.size(); i++) {
            // room stores the current room increment, essentially how many layers deep the for loop is
            // starts on the highest heat (at the core of the ring, and slowly draws it's way outwards)

            // lookup letterID inside event from calender_events
            Building building = findBuildingByLetter(calender_events.get(i).eventLocation.charAt(0));
            drawCircle(building, heatMap.getGraphicsContext2D(), i);
        }
        writeLabel("amongus", heatMap.getGraphicsContext2D());
    }


    public void drawCircle(Building building, GraphicsContext graphics, int room_count) {
        // set draw color to 'calculateHeat's output
        graphics.setFill(calculateHeat(room_count, CirclePreset));
        // 30 is the intial circle size, chosen due to it being roughly the size of a building
        float circle_width = CirclePreset.circleWidth + (CirclePreset.circleStepValue * room_count);

        // removing half of the circle width from the coords is to compensate for the offset of the circle being in the top right corner (thanks javafx)
        graphics.fillOval(building.xPos - (circle_width / 2), building.yPos - (circle_width / 2), circle_width, circle_width); // first pair edits the x/y coords while the second edits the x/y size
    }


    public void writeLabel(String text, GraphicsContext graphics) {
        TextArea new_label = new TextArea();
        new_label.setText(text);
    }


    public static Building findBuildingByLetter(Character letter) {
        Building result = null;
        for (Building building : CampusBuildings) {
            if (building.letterID == letter)
                result = building;
        }
        return result;
    }


    public static Color calculateHeat(int room_number, Circle circleTemplate) {
        // The values range from 0 to 255 to encompass the full RGB scale of color.

        int heat_hue_value = Math.clamp(room_number * circleTemplate.hueStepValue, 0, 255);
        // The heat hue value works as an inverse relationship with both red and blue hues.
        // The deeper the red, the less the blue and vice versa. Initally starts with max blue and zero red
        return (Color.rgb(Math.abs(heat_hue_value - 255), 0, heat_hue_value, 0.5));
    }


    // todo: write at least 2 tests on the data validity / size
    public static void loadEvents(UserTimetableDAO userTimetableDAO, String UserNumber){
        try {
            String profileInfoQuery = "SELECT * FROM User_Timetable_Data where StudentNumber = ?";
            PreparedStatement statement = userTimetableDAO.getDBConnection().prepareStatement(profileInfoQuery);
            statement.setString(1,UserNumber);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) { Event newEvent = new Event(
                resultSet.getInt("EventID"),
                resultSet.getString("EventName"),
                resultSet.getString("EventType"),
                resultSet.getString("EventStartDatetime"),
                resultSet.getString("EventEndDatetime"),
                resultSet.getString("EventLocation"),
                resultSet.getInt("EventAttendance"));
                calenderEvents.add(newEvent);

                // add a tally next to the event's corresponding building, makes it much easier to view event density later
                findBuildingByLetter(newEvent.eventLocation.charAt(0)).eventCount.add(newEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sortRooms(ArrayList<Event> calender_events) {
        // responsible for simply sorting and rendering rooms
        ArrayList<String> quiet_areas = new ArrayList<String>();
        ArrayList<String> busy_areas = new ArrayList<String>();

        for (Building building : CampusBuildings) {
            if (building.eventCount.size() > 1) {
                for (Event event : building.eventCount) {
                    busy_areas.add(event.eventName);
                }
            } else {
                for (Event event : building.eventCount) {
                    quiet_areas.add(event.eventName);
                }
            }
        }

        // create dynamic list that will update with changes, and use it to add the rooms onto the preferred list
        ObservableList<String> quiet_rooms = FXCollections.observableArrayList(quiet_areas);
        ObservableList<String> busy_rooms = FXCollections.observableArrayList(busy_areas);

        quietLocationList.getItems().setAll(quiet_rooms);
        busyLocationList.getItems().setAll(busy_rooms);
    }


    public void openLiveFeed() {
        current_state = feedState.LIVE;
        reloadHeatmap();
    }


    public void openPredictedFeed() {
        current_state = feedState.PREDICTED;
        reloadHeatmap();
    }


    public void generateAIData(ArrayList<Event> event_data) {
        // prepare big bubba's pureed applesauce,
        String input_data = "";
        for (Event event : calenderEvents)
            input_data += event.pureeEventData();

        System.out.print("\n\n" + input_data + "\n\n");
        // need to parse in context lol, give it a template of what each means

        String promptText = "Can you generate a summary of current activities including how clustered together the events are and their frequency given the following heatmap information for a university campus:" + input_data + " with a text limit of 200 characters including spaces and excluding any special characters." +
                "If there is no data given, inform the user that no data exists in the calender."+
                "The formatting for each event (stored inside the input_data as nested lists) is as follows: " +
                " Integer eventID;" +
                " String eventName," +
                " String eventType," +
                " String eventStartDatetime," +
                " String eventEndDatetime," +
                " String eventLocation," +
                " Integer eventAttendance.";
        // predicted feed

        model.promptAI(promptText);
        AIMapSummary.setText(model.promptAI(promptText));
    }


    public void reloadHeatmap() {
        loadEvents(userTimetableDAO, currentUserNumber);
        generateAIData(calenderEvents); // pass in all event data for gemini to feast on

        renderHeatmap(calenderEvents);
        sortRooms(calenderEvents);

        }
    }


    public void initialize() {
        super.initialize();
        try {
            model.initialiseAIModel();
            reloadHeatmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
