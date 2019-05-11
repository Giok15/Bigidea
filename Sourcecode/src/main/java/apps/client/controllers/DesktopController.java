package apps.client.controllers;

import apps.client.logic.DesktopLogic;
import apps.client.logic.IDesktopLogic;
import apps.generic.constants.*;
import apps.generic.models.Desktop;
import apps.generic.enums.Status;
import apps.generic.message.MessageResult;
import apps.generic.utils.DesktopUtil;
import apps.generic.utils.MessageUtil;
import apps.generic.utils.WatcherUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DesktopController implements Observer
{
    //all fxml controls
    @FXML
    private BorderPane desktopPane;

    @FXML
    private AnchorPane anchorDesktopBox;

    @FXML
    private ListView lstFiles;

    @FXML
    private Label lblFiles;

    @FXML
    private Label lblImage;

    @FXML
    private ImageView imgDesktopBackground;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextArea txtDescription;

    @FXML
    private AnchorPane anchorDesktops;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblDescription;

    @FXML
    private Button btnAddEdit;

    private NavigationController navigationController;
    private DesktopController mainDesktopController;
    private Desktop desktop;
    private IDesktopLogic desktopLogic = new DesktopLogic();
    private String strEditTitle = "";

    private ArrayList<DesktopController> desktopBoxes = new ArrayList<>();

    @Override
    public void update(Observable o, Object desktopFiles) {

        List<File> lstDesktopFiles = (List<File>) desktopFiles;
        desktop.setFiles(lstDesktopFiles);
        DesktopUtil.updateGUIDesktopCount(lstDesktopFiles, lstFiles, lblFiles);
        DesktopUtil.updateLstfiles(lstDesktopFiles, lstFiles);

    }

    public void btnNavAdd() {
        try
        {
            DesktopController desktopController = changeScreen(FXMLContstants.FXML_ADDEDITDESKTOP, true);
            desktopController.addDesktopFxmlLoad(navigationController, mainDesktopController);
            navigationController.setStatus(Status.NOTHING);
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
            navigationController.setStatus(Status.ERROR);
        }
    }

    public void btnSelectDesktop() {
        try
        {
            if (MessageUtil.showConfirmMessage(InformationConstants.INFORMATION_HEADER, InformationConstants.INFORMATION_SELECT))
                selectDesktop();
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.CONFLICT.getStatusCode(),Response.Status.CONFLICT.toString(), ErrorConstants.ERROR_WRONG);
            navigationController.setStatus(Status.ERROR);
        }
    }

    private void selectDesktop()
    {
        MessageResult message = desktopLogic.selectDesktop(desktop, true);
        if (message.getCode() != 200)
        {
            MessageUtil.showErrorMessage(message.getCode(),message.getMessage(), message.getData().toString());
            navigationController.setStatus(Status.ERROR);
        }
        else
        {
            navigationController.setStatus(Status.GOOD);
        }
    }

    public void btnEditDesktop()
    {
        try {
            if (MessageUtil.showConfirmMessage(InformationConstants.INFORMATION_HEADER, InformationConstants.INFORMATION_EDIT))
            {
                DesktopController desktopController = changeScreen(FXMLContstants.FXML_ADDEDITDESKTOP, true);
                desktopController.editDesktopFxmlLoad(navigationController, mainDesktopController, desktop);
            }
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.CONFLICT.getStatusCode(),Response.Status.CONFLICT.toString(), ErrorConstants.ERROR_WRONG);
            navigationController.setStatus(Status.ERROR);
        }
    }

    public void btnDeleteDesktop() {
        try {
            if (MessageUtil.showConfirmMessage(InformationConstants.INFORMATION_HEADER, InformationConstants.INFORMATION_CONFIRM))
            {
                DesktopController desktopController = changeScreen(FXMLContstants.FXML_ADDEDITDESKTOP, true);
                desktopController.addDesktopFxmlLoad(navigationController, mainDesktopController);
                desktopLogic.deleteDesktop(desktop.getTitle());
                mainDesktopController.getDesktops();
            }
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.CONFLICT.getStatusCode(),Response.Status.CONFLICT.toString(), ErrorConstants.ERROR_WRONG);
            navigationController.setStatus(Status.ERROR);
        }
    }

    private void selectDesktop(DesktopController desktopBox)
    {
        try
        {
            desktopBox.anchorDesktopBox.setStyle("-fx-background-color: #1f4e79; -fx-background-radius: 10");

            DesktopController desktopController = changeScreen(FXMLContstants.FXML_SELECTDESKTOP, true);
            desktopController.selectDesktopFxmlLoad(desktop, mainDesktopController, navigationController);
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
            navigationController.setStatus(Status.ERROR);
        }
    }

    private void getDesktops() {

        MessageResult message = desktopLogic.getDesktops();

        if (message.getCode() == 200) {
           Desktop[] desktops = (Desktop[]) message.getData();


            double locationX = 5.0;
            double locationY = 5.0;
            int count = 0;
            int row = 3;

            anchorDesktops.getChildren().clear();

            for (Desktop currentDesktop : desktops) {

                DesktopController desktopController = changeScreen(FXMLContstants.FXML_BOXDESKTOP, false);
                desktopController.boxDesktopFxmlLoad(currentDesktop, this, navigationController);

                desktopController.anchorDesktopBox.setLayoutX(locationX);
                desktopController.anchorDesktopBox.setLayoutY(locationY);

                anchorDesktops.getChildren().add(desktopController.anchorDesktopBox);

                locationX += 160;
                count++;

                if (count == row) {
                    locationX = 5.0;
                    locationY += 150;
                    row += 3;
                }

                desktopBoxes.add(desktopController);
            }
        } else {
            MessageUtil.showErrorMessage(message.getCode(),message.getMessage(), message.getData().toString());
            navigationController.setStatus(Status.ERROR);
        }
    }

    public void addEdit() {
        try {
            if (!desktop.getFiles().isEmpty()) {
                if (desktop.getBackground() != null && !desktop.getTitle().equals("") && !desktop.getDescription().equals("")) {

                    WatcherUtil.stopWatcherThread();

                    if (!strEditTitle.equals("")) {
                        desktopLogic.deleteDesktop(strEditTitle);
                    }

                    this.desktop.setPath(Paths.get(this.desktop.getPath().toFile().getAbsolutePath() + "/" + desktop.getTitle()));
                    MessageResult message = desktopLogic.addDesktop(desktop);

                    if (message.getCode() == 200) {
                        DesktopController desktopController = changeScreen(FXMLContstants.FXML_SELECTDESKTOP, true);
                        mainDesktopController.getDesktops();
                        desktopController.selectDesktopFxmlLoad(desktop, mainDesktopController, navigationController);
                        desktopController.selectDesktop();
                        navigationController.setStatus(Status.GOOD);
                    } else {
                        MessageUtil.showErrorMessage(message.getCode(), message.getMessage(), message.getData().toString());
                        navigationController.setStatus(Status.ERROR);
                    }
                } else {
                    MessageUtil.showErrorMessage(Response.Status.EXPECTATION_FAILED.getStatusCode(), Response.Status.EXPECTATION_FAILED.toString(), ErrorConstants.ERROR_EMPTYFIELDS);
                    navigationController.setStatus(Status.ERROR);
                }
            } else {
                MessageUtil.showErrorMessage(Response.Status.EXPECTATION_FAILED.getStatusCode(), Response.Status.EXPECTATION_FAILED.toString(), ErrorConstants.ERROR_NODESKTOPFILES);
                navigationController.setStatus(Status.ERROR);
            }
        } catch (Exception e) {
            MessageUtil.showErrorMessage(Response.Status.CONFLICT.getStatusCode(), Response.Status.CONFLICT.toString(), ErrorConstants.ERROR_WRONG);
            navigationController.setStatus(Status.ERROR);
        }
    }

    private DesktopController changeScreen(String fxml, boolean update)
    {
        DesktopController desktopController = new DesktopController();
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            AnchorPane pane = loader.load();
            desktopController = loader.getController();

            if (update)
            {
                mainDesktopController.desktopPane.setTop(pane);
            }

            return desktopController;
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
            navigationController.setStatus(Status.ERROR);
            return desktopController;
        }
    }

    private void resetColorDesktopBoxes()
    {
        for (DesktopController desktopController : desktopBoxes)
        {
            boolean setdefault = true;

            if (desktop != null && desktop.getTitle().equals(desktopController.desktop.getTitle()))
            {
                setdefault = false;
            }

            if (setdefault)
            {
                desktopController.anchorDesktopBox.setStyle("-fx-background-color: #001236; -fx-background-radius: 10");
            }
            else
            {
                desktopController.anchorDesktopBox.setStyle("-fx-background-color: #1f4e79; -fx-background-radius: 10");
            }
        }
    }

    public void titleChanged() {
        this.desktop.setTitle(this.txtTitle.getText());
    }

    public void descriptionChanged() {
        this.desktop.setDescription(this.txtDescription.getText());
    }

    public void btnNavSelectDesktop() {

        WatcherUtil.stopWatcherThread();

        selectDesktop(this);

        navigationController.setStatus(Status.NOTHING);
    }

    public void mainDesktopFxmlLoad(NavigationController navigationController)
    {
        try
        {
            this.navigationController = navigationController;
            mainDesktopController = this;

            FXMLLoader loaderDesktop = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_ADDEDITDESKTOP));
            AnchorPane paneDesktop = loaderDesktop.load();
            desktopPane.setTop(paneDesktop);
            DesktopController desktopController = loaderDesktop.getController();

            desktopController.addDesktopFxmlLoad(navigationController, this);
            getDesktops();
        }
        catch (IOException e)
        {
            navigationController.setStatus(Status.ERROR);
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }

    public void addDesktopFxmlLoad(NavigationController navigationController, DesktopController mainDesktopController)
    {
        try {
            this.navigationController = navigationController;
            this.mainDesktopController = mainDesktopController;

            desktop = new Desktop(Paths.get(DesktopPathsConstants.DOCUMENTURL));
            WatcherUtil.createWatcherThread(this, desktopLogic.getDesktopFiles());

            this.desktop.setBackground(DesktopUtil.backgroundToByte(SwingFXUtils.fromFXImage(imgDesktopBackground.getImage(), null)));

            mainDesktopController.desktop = null;
            mainDesktopController.resetColorDesktopBoxes();
        }
        catch (Exception e)
        {
            navigationController.setStatus(Status.ERROR);
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }

    public void editDesktopFxmlLoad(NavigationController navigationController, DesktopController mainDesktopController, Desktop desktop)
    {

        try {
            MessageResult message = desktopLogic.selectDesktop(desktop, true);
            if (message.getCode() == 200) {
                this.navigationController = navigationController;
                this.mainDesktopController = mainDesktopController;
                strEditTitle = desktop.getTitle();

                this.desktop = desktop;
                this.desktop.setPath(Paths.get(DesktopPathsConstants.DOCUMENTURL));
                WatcherUtil.createWatcherThread(this, desktopLogic.getDesktopFiles());
                DesktopUtil.updateGUIDesktopCount(desktop.getFiles(), lstFiles, lblFiles);
                DesktopUtil.updateLstfiles(desktop.getFiles(), lstFiles);

                txtTitle.setText(desktop.getTitle());
                txtDescription.setText(desktop.getDescription());
                imgDesktopBackground.setImage(new Image(new ByteArrayInputStream(desktop.getBackground())));
                btnAddEdit.setText("Wijzigen");
            } else {
                MessageUtil.showErrorMessage(message.getCode(),message.getMessage(), message.getData().toString());
            }
        }
        catch (Exception e)
        {
            navigationController.setStatus(Status.ERROR);
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }

    private void boxDesktopFxmlLoad(Desktop desktop, DesktopController mainDesktopController, NavigationController navigationController) {
        this.desktop = desktop;
        this.navigationController = navigationController;
        this.mainDesktopController = mainDesktopController;

        lblTitle.setText(desktop.getTitle());
        imgDesktopBackground.setImage(new Image(new ByteArrayInputStream(desktop.getBackground())));
    }

    private void selectDesktopFxmlLoad(Desktop desktop, DesktopController mainDesktopController, NavigationController navigationController) {
        this.desktop = desktop;
        this.mainDesktopController = mainDesktopController;
        this.navigationController = navigationController;

        lblTitle.setText(desktop.getTitle());
        lblDescription.setText(desktop.getDescription());
        imgDesktopBackground.setImage(new Image(new ByteArrayInputStream(desktop.getBackground())));
        lblFiles.setText(Integer.toString(this.desktop.getFiles().size()));

        DesktopUtil.updateLstfiles(desktop.getFiles(), lstFiles);

        mainDesktopController.desktop = desktop;
        mainDesktopController.resetColorDesktopBoxes();
    }

    public void uploadBackground() {
        DesktopUtil.uploadBackground(lblImage, desktop, imgDesktopBackground);
    }
}
