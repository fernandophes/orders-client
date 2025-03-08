module br.edu.ufersa.cc.sd {
    requires javafx.controls;
    requires javafx.fxml;

    opens br.edu.ufersa.cc.sd to javafx.fxml;
    exports br.edu.ufersa.cc.sd;
}
