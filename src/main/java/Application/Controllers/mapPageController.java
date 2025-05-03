package Application.Controllers;

import Application.Database.DatabaseConnection;
import Application.Database.UserTimetable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.swing.text.Element;
import java.awt.*;
import java.io.Console;
import java.sql.*;
import java.util.*;
import java.util.List;

public class mapPageController extends sceneLoaderController {
    @FXML Canvas heatMap;
    @FXML ListView<Integer> busyLocationList;
    @FXML ListView<Integer> quietLocationList;

    // stores all vital information regarding a building's code, x/y location and classes (in that order)
    /// PLACEHOLDER DATA JUST SO THERE'S SOMETHING TO SHOW
    /// todo: WILL BE REPLACED ONCE "User_Signup_Data" IS POPULATED
    public Building[] CampusBuildings = {
            new Building('P', 254, 100, new Integer[]{302}),
            new Building('S', 78, 164, new Integer[]{1, 201, 302}),
            new Building('Z', 128, 166, new Integer[]{201, 203, 203})
    };

    public Circle CirclePreset =
            new Circle(
                    25,
                    15,
                    51
            );

    public static class Building {
        Character blockLetter;
        Integer xPos;
        Integer yPos;
        Integer[] bookedRooms;

        public Building(Character BlockLetter, Integer xPos, Integer yPos, Integer[] bookedRooms) {
            this.blockLetter = BlockLetter;
            this.xPos = xPos;
            this.yPos = yPos;
            this.bookedRooms = bookedRooms;
        }

        public Character getBlockLetter() {
            return this.blockLetter;
        }

        public Integer getXPos() {
            return this.xPos;
        }

        public Integer getYPos() {
            return this.yPos;
        }

        public Integer[] getBookedRooms() {
            return this.bookedRooms;
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


    // TODO: HIGH priority, to be done after checkpoint & before friday when there's more breathing room
    // requires actual DB data to be created in SQL before it can be implemented
    /// transform, use the same code to retrieve calender data and use that, can also do at least 2 tests on the data validity or size?
    /*
    public void loadEvents(){
        try {
            String profileInfoQuery = "SELECT * FROM User_Timetable_Data where StudentNumber = ?";
            PreparedStatement statement = userSignupDAO.getDBConnection().prepareStatement(profileInfoQuery);
            statement.setString(1,currentUserNumber);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                CampusBuildings
                events.add(new UserTimetable(resultSet.getInt("EventID"),
                        resultSet.getString("EventName"),
                        currentUserNumber,
                        resultSet.getString("EventType"),
                        resultSet.getString("EventStartDatetime"),
                        resultSet.getString("EventEndDatetime"),
                        resultSet.getString("EventLocation"),
                        resultSet.getInt("EventAttendance")));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    */

    public void renderHeatmap(Building building) {
        // The more rooms there are, the more layers of circle there are, with each warmer colors.
        for (int room_count = building.bookedRooms.length; room_count > 0; room_count--) {
            // room stores the current room increment, essentially how many layers deep the for loop is
            // starts on the highest heat (at the core of the ring, and slowly draws it's way outwards)
            drawBuilding(building, heatMap.getGraphicsContext2D(), room_count);
        }
    }

    public Color calculateHeat(int room_number) {
        // The values range from 0 to 255 to encompass the full RGB scale of color.

        int heat_hue_value = Math.clamp(room_number * CirclePreset.hueStepValue, 0, 255);
        // The heat hue value works as an inverse relationship with both red and blue hues.
        // The deeper the red, the less the blue and vice versa. Initally starts with max blue and zero red
        return (Color.rgb(Math.abs(heat_hue_value - 255), 0, heat_hue_value, 0.5));
    }

    public void drawBuilding(Building building, GraphicsContext graphics, int room_count) {
        // set draw color to 'calculateHeat's output
        graphics.setFill(calculateHeat(room_count));
        // 30 is the intial circle size, chosen due to it being roughly the size of a building
        float circle_width = CirclePreset.circleWidth + (CirclePreset.circleStepValue * room_count);
        // removing half of the circle width from the coords is to compensate for the offset of the circle being in the top right corner (thanks javafx)
        graphics.fillOval(building.xPos - (circle_width / 2), building.yPos - (circle_width / 2), circle_width, circle_width); // first pair edits the x/y coords while the second edits the x/y size
    }

    public void sortRooms(Building building) {
        // responsible for simply sorting and rendering rooms
        /// TEMP SORTING METHOD
        // todo: IMPROVE THIS AFTER CHECKPOINT
        ListView<Integer> prefered_list_type;
        if (building.bookedRooms.length > 2) {
            prefered_list_type = busyLocationList;
        } else {
            prefered_list_type = quietLocationList;
        }
        // create dynamic list that will update with changes, and use it to add the rooms onto the preferred list
        ObservableList<Integer> rooms = FXCollections.observableArrayList(building.bookedRooms);
        prefered_list_type.getItems().addAll(rooms);
    }


    public void initialize() {
        try {
            // loadEvents(); todo: WIP, see further above, basically just providing fancier data for "sortRooms"
            for (Building building : CampusBuildings) {
                renderHeatmap(building);
                sortRooms(building);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
