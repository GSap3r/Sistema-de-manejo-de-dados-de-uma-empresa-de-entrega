module org.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;
    requires java.sql;
    requires java.logging;
    requires org.json;
    requires kernel;
    requires layout;
    requires io;

    opens org.example.javafx.db.entidades to javafx.fxml;
    opens org.example.javafx to javafx.fxml;
    exports org.example.javafx;
    exports org.example.javafx.db.entidades;

}