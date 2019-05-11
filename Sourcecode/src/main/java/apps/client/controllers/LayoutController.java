package apps.client.controllers;

import apps.generic.constants.ErrorConstants;
import apps.generic.constants.FXMLContstants;
import apps.generic.utils.MessageUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable
{
    @FXML
    private BorderPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle rb)  {

        try
        {
            FXMLLoader loaderBottom = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_BOTTOM));
            FXMLLoader loaderMenuBar = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_MENUBAR));
            AnchorPane paneBottom = (AnchorPane) loaderBottom.load();
            AnchorPane paneMenuBar = (AnchorPane) loaderMenuBar.load();

            NavigationController navigationHandler = loaderMenuBar.getController();
            navigationHandler.setMainHandler(this);
            navigationHandler.loadDesktopScreen();

            mainPane.setBottom(paneBottom);
            mainPane.setLeft(paneMenuBar);

        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }

    public BorderPane getMainPane()
    {
        return mainPane;
    }
}
