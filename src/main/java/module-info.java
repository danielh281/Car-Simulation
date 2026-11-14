module com.dh.carsimulationassignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.dh.carsimulationassignment to javafx.fxml;
    exports com.dh.carsimulationassignment;
}
