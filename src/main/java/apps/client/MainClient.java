package apps.client;

import apps.generic.constants.DefaultConstants;
import apps.generic.constants.FXMLContstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainClient extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(FXMLContstants.FXML_MAIN));
        stage.resizableProperty().setValue(false);
        stage.setHeight(485);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(DefaultConstants.CSS_STYLESHEET);

        stage.setTitle(DefaultConstants.TITLE);
        stage.setScene(scene);

        stage.getIcons().add(new Image(DefaultConstants.OPENFILE + getClass().getResource(DefaultConstants.ICON).getPath()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
