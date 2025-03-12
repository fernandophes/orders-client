module br.edu.ufersa.cc.sd {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    requires transitive org.apache.logging.log4j;
    requires transitive javafx.graphics;

    requires static lombok;

    opens br.edu.ufersa.cc.sd to javafx.fxml;

    exports br.edu.ufersa.cc.sd.models;
    exports br.edu.ufersa.cc.sd.services;
    exports br.edu.ufersa.cc.sd;
}
