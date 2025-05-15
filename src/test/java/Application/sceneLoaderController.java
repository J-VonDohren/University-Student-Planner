import Application.Controllers.sceneLoaderController;

import Application.*;
import Application.Database.DatabaseConnection;
import Application.StageController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

class sceneLoaderControllerTest {
    @BeforeAll
    static void runJavaFX(){
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
        }
    }

    @Test
    void testLoginSceneLoadsSuccessfully() throws InterruptedException {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<Throwable> thrown = new AtomicReference<>();

            Platform.runLater(() -> {
                try {
                    Main.getLoginPage();
                } catch (Throwable e) {
                    thrown.set(e);
                } finally {
                    latch.countDown();
                }
            });

            if (!latch.await(5, TimeUnit.SECONDS)) {
                fail("Test timed out");
            }

            assertNull(thrown.get(), "Scene loading threw an unexpected exception: " +
                    (thrown.get() != null ? thrown.get().getMessage() : "null"));
        }catch(UnsupportedOperationException e){
        }
    }
    @Test
    void testRegisterSceneLoadsSuccessfully() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrown = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                Main.getRegisterPage();
            } catch (Throwable e) {
                thrown.set(e);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out");
        }

        assertNull(thrown.get(), "Scene loading threw an unexpected exception: " +
                (thrown.get() != null ? thrown.get().getMessage() : "null"));
    }
    @Test
    void testMapSceneLoadsSuccessfully() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrown = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                Main.getMapPage();
            } catch (Throwable e) {
                thrown.set(e);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out");
        }

        assertNull(thrown.get(), "Scene loading threw an unexpected exception: " +
                (thrown.get() != null ? thrown.get().getMessage() : "null"));
    }
    @Test
    void testHomeSceneLoadsSuccessfully() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrown = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                Main.getHomePage();
            } catch (Throwable e) {
                thrown.set(e);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out");
        }

        assertNull(thrown.get(), "Scene loading threw an unexpected exception: " +
                (thrown.get() != null ? thrown.get().getMessage() : "null"));
    }
    @Test
    void testGoalSceneLoadsSuccessfully() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrown = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                Main.getGoalsPage();
            } catch (Throwable e) {
                thrown.set(e);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out");
        }

        assertNull(thrown.get(), "Scene loading threw an unexpected exception: " +
                (thrown.get() != null ? thrown.get().getMessage() : "null"));
    }
    @Test
    void testProfileSceneLoadsSuccessfully() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrown = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                Main.getProfilePage();
            } catch (Throwable e) {
                thrown.set(e);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out");
        }

        assertNull(thrown.get(), "Scene loading threw an unexpected exception: " +
                (thrown.get() != null ? thrown.get().getMessage() : "null"));
    }
    @Test
    void testCalendarSceneLoadsSuccessfully() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrown = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                Main.getCalendarPage();
            } catch (Throwable e) {
                thrown.set(e);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out");
        }

        assertNull(thrown.get(), "Scene loading threw an unexpected exception: " +
                (thrown.get() != null ? thrown.get().getMessage() : "null"));
    }
    @Test
    void testIfScenesFailWithInvalidPaths() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrown = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(); // set no location
                loader.load();

            } catch (Throwable e) {
                thrown.set(e); // expecting this exception
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out");
        }

        assertTrue(thrown.get() instanceof IllegalStateException, "Expected IllegalStateException when no location is set");
    }

}
