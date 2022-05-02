module com.example.week7 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.week7 to javafx.fxml;
    exports com.example.week7;
}