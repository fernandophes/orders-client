module br.edu.ufersa.cc.sd {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires static lombok;

    opens br.edu.ufersa.cc.sd to javafx.fxml;
    exports br.edu.ufersa.cc.sd;
}
