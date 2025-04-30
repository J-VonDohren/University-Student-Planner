package Application.Controllers;

import Application.Database.DatabaseConnection;
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
import java.sql.Array;
import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class mapPageController extends sceneLoaderController {
    @FXML private Canvas heatMap;
    @FXML private ListView busyLocationList;
    @FXML private ListView quietLocationList;

    // initial circle width and ring expansion size, respectively
    int init_circle_width = 25;
    int cirle_step_value = 15;

    // hue increase amount per ring (keep in mind that the maximum hue for RGB colors is 255)
    int hue_step_value = 51;


    // stores all vital information regarding a building's code, x/y location and classes (in that order)
    private final Building[] CampusBuildings = {
            new Building('P', 254, 100, new Integer[]{302}),
            new Building('S', 78, 164, new Integer[]{1, 201, 302}),
            new Building('Z', 128, 166, new Integer[]{201, 203, 203})
    };

    static class Building {
        Character BlockLetter;
        Integer xPos;
        Integer yPos;
        Integer[] bookedRooms;

        Building(Character BlockLetter, Integer xPos, Integer yPos, Integer[] bookedRooms) {
            this.BlockLetter = BlockLetter;
            this.xPos = xPos;
            this.yPos = yPos;
            this.bookedRooms = bookedRooms;
        }
    }

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

        int heat_hue_value = Math.clamp(room_number * hue_step_value, 0, 255);
        // The heat hue value works as an inverse relationship with both red and blue hues.
        // The deeper the red, the less the blue and vice versa. Initally starts with max blue and zero red
        return (Color.rgb(Math.abs(heat_hue_value - 255), 0, heat_hue_value, 0.5));
    }

    public void drawBuilding(Building building, GraphicsContext graphics, int room_count) {
        // set draw color to 'calculateHeat's output
        graphics.setFill(calculateHeat(room_count));
        // 30 is the intial circle size, chosen due to it being roughly the size of a building
        float circle_width = init_circle_width + (cirle_step_value * room_count);
        // removing half of the circle width from the coords is to compensate for the offset of the circle being in the top right corner (thanks javafx)
        graphics.fillOval(building.xPos - (circle_width / 2), building.yPos - (circle_width / 2), circle_width, circle_width); // first pair edits the x/y coords while the second edits the x/y size
    }

    public void sortRooms(Building building) {
        // responsible for simply sorting and rendering rooms
        /// TEMP SORTING METHOD, IMPROVE THIS
        ListView prefered_list_type;
        if (building.bookedRooms.length > 2) {
            prefered_list_type = busyLocationList;
        } else {
            prefered_list_type = quietLocationList;
        }
        // create dynamic list that will update with changes, and use it to add the rooms onto the preferred list
        ObservableList<Integer> rooms = FXCollections.observableArrayList(building.bookedRooms);
        prefered_list_type.getItems().add(rooms);
    }

    public void initialize() {
        super.initialize();
        try {
            for (Building building : CampusBuildings) {
                renderHeatmap(building);
                sortRooms(building);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
