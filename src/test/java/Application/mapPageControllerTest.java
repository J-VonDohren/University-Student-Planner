import Application.Controllers.mapPageController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapPageControllerTest {
    /*
    @Test
    void renderHeatmap_Storage_Test() {
        // checking that the building class can successfully store and retrieve its own values
        mapPageController.Building building = new mapPageController.Building('A', 100, 200);
        assertEquals('A', building.getBlockLetter());
        assertEquals(100, building.getXPos());
        assertEquals(200, building.getYPos());
    }

    @Test
    void renderHeatmap_XPosInRange_Test() {
        // ensuring xPos does not try to place buildings outside the borders of the ImageView container
        mapPageController.Building building = new mapPageController.Building('A', 100, 200, new String[]{"GP_300"});
        assertTrue(building.getYPos() >= -300);
        assertTrue(building.getYPos() <= 300);
    }

    @Test
    void renderHeatmap_YPosInRange_Test() {
        // ensuring yPos does not try to place buildings outside the borders of the ImageView container
        mapPageController.Building building = new mapPageController.Building('A', 100, 200, new String[]{"GP_300"});
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

    /*
    @Test
    void sortRoomsTest() {
        // todo - create this test case once the sorting method is improved
    }
    */

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