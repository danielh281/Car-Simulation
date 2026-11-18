module com.dh.carsimulationassignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.media;

    opens com.dh.carsimulationassignment to javafx.fxml;
    exports com.dh.carsimulationassignment;
}
