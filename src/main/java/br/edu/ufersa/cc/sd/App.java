package br.edu.ufersa.cc.sd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import br.edu.ufersa.cc.sd.exceptions.ConnectionException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    public static void setScene(Scene scene) {
        App.scene = scene;
    }

    @Override
    public void start(final Stage stage) throws IOException {
        try {
            setScene(new Scene(loadFXML("listAll"), 640, 480));
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (final LoadException e) {
            setScene(new Scene(loadFXML("reconnect"), 640, 480));
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
    }

    public static void setRoot(final String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(final String fxml) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(final String[] args) {
        launch();
    }

}