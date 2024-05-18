module org.phoenixaki.enemykiller.enemykiller {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.seanstan95.enemykiller to javafx.fxml;
    exports org.seanstan95.enemykiller;
}