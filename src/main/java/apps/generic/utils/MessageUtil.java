package apps.generic.utils;

import apps.generic.constants.ErrorConstants;
import apps.generic.constants.InformationConstants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public final class MessageUtil {

    private MessageUtil() {
        throw new IllegalStateException("constants class");
    }

    public static void showErrorMessage(int errorCode, String errorMessage, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ErrorConstants.ERROR);
        alert.setHeaderText(errorCode + ": " + errorMessage);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInformationMessage(String header, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(InformationConstants.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean showConfirmMessage(String header, String body)
    {
        Boolean result = false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(InformationConstants.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(body);

        ButtonType buttonTypeYes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("Nee", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> resultMessage = alert.showAndWait();
        if (resultMessage.isPresent() && resultMessage.get().getButtonData() == ButtonBar.ButtonData.YES) {
            result = true;
        }
        return result;
    }
}
