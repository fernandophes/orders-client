package br.edu.ufersa.cc.sd;

import java.io.IOException;

import javafx.fxml.FXML;

public class ReconnectController {

    @FXML
    private void switchToListAll() throws IOException {
        App.setRoot("listAll");
    }

}