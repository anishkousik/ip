package typecast;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import typecast.ui.MainWindow;

import java.io.IOException;

/**
 * A GUI for TypeCast using FXML.
 */
public class Main extends Application {

    private TypeCast typeCast = new TypeCast("./data/tasks.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("TypeCast");
            stage.setMinHeight(600);
            stage.setMinWidth(400);
            fxmlLoader.<MainWindow>getController().setTypeCast(typeCast);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}