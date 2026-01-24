module org.example.candidavagasjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires java.sql;
    requires javafx.base;
    requires spring.web;
    requires jasperreports;
    requires java.desktop;
    //requires org.example.candidavagasjavafx;
    //requires org.example.candidavagasjavafx;

    opens org.example.candidavagasjavafx.controller to javafx.fxml;
    exports org.example.candidavagasjavafx.aplication;
    opens org.example.candidavagasjavafx.aplication to javafx.fxml;
    opens org.example.candidavagasjavafx.entidades to javafx.base;
}