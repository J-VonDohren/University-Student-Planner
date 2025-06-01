import Application.Controllers.mapPageController;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class MapPageControllerTest {
    /*
    @Test
    void renderHeatmap_Storage_Test() {
        // checking that the building class can successfully store and retrieve its own values
        mapPageController.Event event = new mapPageController.Event(1, "event", "type",
                "2025-04-04", "2025-04-05", "S401", 0);
        mapPageController.Building building = new mapPageController.Building('A', 100, 200, new ArrayList<mapPageController.Event>((Collection<? extends mapPageController.Event>) event));
        assertEquals('A', building.getBlockLetter());
        assertEquals(100, building.getXPos());
        assertEquals(200, building.getYPos());
    }

    @Test
    void renderHeatmap_XPosInRange_Test() {
        // ensuring xPos does not try to place buildings outside the borders of the ImageView container
        mapPageController.Event event = new mapPageController.Event(1, "event", "type",
                "2025-04-04", "2025-04-05", "S401", 0);
        mapPageController.Building building = new mapPageController.Building('A', 100, 200, new ArrayList<mapPageController.Event>((Collection<? extends mapPageController.Event>) event));
        assertTrue(building.getYPos() >= -300);
        assertTrue(building.getYPos() <= 300);
    }

    @Test
    void renderHeatmap_YPosInRange_Test() {
        // ensuring yPos does not try to place buildings outside the borders of the ImageView container
        mapPageController.Event event = new mapPageController.Event(1, "event", "type",
                "2025-04-05", "2025-04-05", "S401", 0);
        mapPageController.Building building = new mapPageController.Building('A', 100, 200, new ArrayList<mapPageController.Event>((Collection<? extends mapPageController.Event>) event));
        assertTrue(building.getYPos() >= -200);
        assertTrue(building.getYPos() <= 200);
    }
    */

    @Test
    void calculateHeat_ExpectedOutput_Test() {
        // ensures that the color values being outputted match intended results
        var color_1 = mapPageController.calculateHeat(1, mapPageController.CirclePreset);
        var color_2 = mapPageController.calculateHeat(2, mapPageController.CirclePreset);
        var color_3 = mapPageController.calculateHeat(3, mapPageController.CirclePreset);
        assertEquals("0xcc003380", color_1.toString()); // red_hue
        assertEquals("0x99006680", color_2.toString()); // purple hue
        assertEquals("0x66009980", color_3.toString()); // blue_hue
    }

    @Test
    void isWithinDates_Past_Test() {
        // checking that the building class can successfully store and retrieve its own values
        String start_time = "2025-04-04";
        String end_time = "2025-04-05";
        String today = "2025-04-06";
        // checking that the method can handle past dates
        assertFalse(mapPageController.isWithinDates(end_time, start_time, today));
        // opposite case
        assertFalse(mapPageController.isWithinDates(today, end_time, start_time));
        // checking that the method can handle future dates
        assertFalse(mapPageController.isWithinDates(end_time, start_time, today));
        // opposite case
        assertFalse(mapPageController.isWithinDates(today, end_time, start_time));
    }

    @Test
    void circleClass_Width_Test() {
        /// Circle Width must be a non-negative value or circles will not appear.
        mapPageController.Circle circlePresetWidth = new mapPageController.Circle(1, 1, 1);
        assertTrue(circlePresetWidth.getWidth() > 0);
    }

    @Test
    void circleClass_StepValue_Test() {
        /// Circle Step must be a non-negative value or heat bubbles will not grow.
        mapPageController.Circle circlePresetStepValue = new mapPageController.Circle(1, 1, 1);
        assertTrue(circlePresetStepValue.getStepValue() > 0);
    }

    @Test
    void circleClass_HueStepValue_Test() {
        /// Circle Hue must be a non-negative value or circle's color will not change.
        mapPageController.Circle circlePresetHueValue = new mapPageController.Circle(1, 1, 1);
        assertTrue(circlePresetHueValue.getHueValue() > 0);
    }
}