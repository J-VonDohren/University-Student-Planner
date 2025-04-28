module Application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires java.desktop;
    requires java.compiler;

    // Open package to JavaFX runtime
    opens Application.Controllers to javafx.graphics, javafx.fxml;

    exports Application;
    exports Application.Controllers;
    exports Application.Database;

}