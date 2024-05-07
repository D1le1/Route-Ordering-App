module com.example.operatordesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires android.json;
    requires kotlinx.coroutines.core;


    opens com.example.operatordesktop to javafx.fxml;
    exports com.example.operatordesktop;
    exports com.example.operatordesktop.util;
    opens com.example.operatordesktop.util to javafx.fxml;
}