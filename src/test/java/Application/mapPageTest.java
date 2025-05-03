import Application.Controllers.mapPageController;
import org.junit.jupiter.api.Test;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;

import static org.junit.jupiter.api.Assertions.*;

class MapPageTest {

    @Test
    void circleWidthVisibleTest() {
        /// Circle Width must be a non-negative value or circles will not appear.
        mapPageController.Circle circlePresetWidth = new mapPageController.Circle(1, 1, 1);
        assertTrue(circlePresetWidth.getWidth() > 0);
    }

    @Test
    void circleStepVisibleTest() {
        /// Circle Step must be a non-negative value or heat bubbles will not grow.
        mapPageController.Circle circlePresetStepValue = new mapPageController.Circle(1, 1, 1);
        assertTrue(circlePresetStepValue.getStepValue() > 0);
    }

    @Test
    void circleHueVisibleTest() {
        /// Circle Hue must be a non-negative value or circle's color will not change.
        mapPageController.Circle circlePresetHueValue = new mapPageController.Circle(1, 1, 1);
        assertTrue(circlePresetHueValue.getHueValue() > 0);
    }

    @Test
    void campusBuildingsLetterExistsTest() {
        /// Campus Buildings must contain a building character identifier.
        mapPageController.Building campusBuildingsPresetLetter = new mapPageController.Building('A', 100, 200, new Integer[]{300});
        assertEquals('A', campusBuildingsPresetLetter.getBlockLetter());
    }

    @Test
    void campusBuildingsXPosExistsTest() {
        /// Campus Buildings must contain non-negative pixel-marked coordinates of block locations.
        mapPageController.Building campusBuildingsPresetXPos = new mapPageController.Building('A', 100, 200, new Integer[]{300});
        assertEquals(100, campusBuildingsPresetXPos.getXPos());
    }

    @Test
    void campusBuildingsYPosExistsTest() {
        /// Campus Buildings must contain non-negative pixel-marked coordinates of block locations.
        mapPageController.Building campusBuildingsPresetYPos = new mapPageController.Building('A', 100, 200, new Integer[]{300});
        assertEquals(200, campusBuildingsPresetYPos.getYPos());
    }

    @Test
    void campusBuildingsBookedRoomsExistsTest() {
        Integer[] bookedRooms = new Integer[]{300};
        mapPageController.Building campusBuildingsPresetBookedRooms = new mapPageController.Building('A', 100, 200, bookedRooms);
        assertEquals(bookedRooms, campusBuildingsPresetBookedRooms.getBookedRooms());
    }
}
