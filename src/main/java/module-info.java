module org.phoenixaki.enemykiller.enemykiller {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.phoenixaki.enemykiller.enemykiller to javafx.fxml;
    exports org.phoenixaki.enemykiller.enemykiller;
}