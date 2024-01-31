module com.example.encryption_intarfase {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.encryption_intarfase to javafx.fxml;
    exports com.example.encryption_intarfase;
}