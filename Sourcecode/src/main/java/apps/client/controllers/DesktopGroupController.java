package apps.client.controllers;

import apps.client.clientendpoint.WebsocketClientEndpoint;
import apps.client.clientendpoint.IWebsocketClientEndpoint;
import apps.client.logic.DesktopLogic;
import apps.client.logic.IDesktopLogic;
import apps.client.messaging.*;
import apps.generic.constants.*;
import apps.generic.dto.DesktopGroupDTO;
import apps.generic.models.Desktop;
import apps.generic.enums.Status;
import apps.generic.utils.DesktopUtil;
import apps.generic.utils.MessageUtil;
import apps.generic.utils.WatcherUtil;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DesktopGroupController implements Observer, IDesktopGroupController {

    @FXML
    private BorderPane desktopPane;

    @FXML
    public Label lblLeden;

    @FXML
    public Label lblImage;

    @FXML
    private AnchorPane connectPane;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private ImageView imgDesktopBackground;

    @FXML
    private Label lblFiles;

    @FXML
    private ListView lstFiles;

    @FXML
    private Label lblCreatedName;

    @FXML
    private TextField txtEmail;

    private NavigationController navigationController;
    private DesktopGroupController mainDesktopGroupController;
    private IDesktopLogic desktopLogic = new DesktopLogic();
    private Desktop desktop;
    private IMessageHandler messagehandler = new MessageHandler();
    private IMessageSender messageSender = new MessageSender();
    private IWebsocketClientEndpoint desktopGroupClientEndpoint = new WebsocketClientEndpoint();

    @Override
    public void update(Observable o, Object desktopFiles) {
        List<File> lstDesktopFiles = (List<File>) desktopFiles;
        desktop.setFiles(lstDesktopFiles);
        DesktopUtil.updateGUIDesktopCount(lstDesktopFiles, lstFiles, lblFiles);
        DesktopUtil.updateLstfiles(lstDesktopFiles, lstFiles);
    }

    public void mainDesktopGroupFxmlLoad(NavigationController navigationHandler)
    {
        try {
            this.navigationController = navigationHandler;
            this.mainDesktopGroupController = this;

            FXMLLoader loaderDesktop = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_ADDDESKTOPGROUP));
            AnchorPane paneDesktopGroup = loaderDesktop.load();
            desktopPane.setTop(paneDesktopGroup);
            DesktopGroupController desktopGroupController = loaderDesktop.getController();
            messagehandler.setDesktopGroupController(desktopGroupController);
            messageSender = new MessageSender();
            desktopGroupController.addDesktopGroupFxmlLoad(navigationHandler, mainDesktopGroupController, messageSender);

            desktopGroupClientEndpoint.start();
            messageSender.setClient(desktopGroupClientEndpoint);
            messageSender.startAuthentication(navigationHandler.getAccountController().getAccount());
        }
        catch (IOException e)
        {
            navigationHandler.setStatus(Status.ERROR);
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }

    public void addDesktopGroupFxmlLoad(NavigationController navigationHandler, DesktopGroupController mainDesktopGroupController, IMessageSender messageSender)
    {
        try {
            this.navigationController = navigationHandler;
            this.mainDesktopGroupController = mainDesktopGroupController;
            this.messageSender = messageSender;

            desktop = new Desktop(Paths.get(DesktopPathsConstants.DOCUMENTURL));
            WatcherUtil.createWatcherThread(this, desktopLogic.getDesktopFiles());

            this.desktop.setBackground(DesktopUtil.backgroundToByte(SwingFXUtils.fromFXImage(imgDesktopBackground.getImage(), null)));
        }
        catch (Exception e)
        {
            navigationController.setStatus(Status.ERROR);
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }

    public void displayDesktopGroupFxmlLoad(NavigationController navigationHandler, DesktopGroupController mainDesktopGroupController, IMessageSender messageSender, Desktop desktop)
    {
        try {
            this.navigationController = navigationHandler;
            this.mainDesktopGroupController = mainDesktopGroupController;
            this.messageSender = messageSender;
            this.desktop = desktop;

            lblCreatedName.setText(mainDesktopGroupController.txtEmail.getText());
            mainDesktopGroupController.txtEmail.setText("");
            this.desktop.setFiles(desktopLogic.getDesktopFiles());
            desktopLogic.changeBackground(desktop.getBackground());

            imgDesktopBackground.setImage(new Image(new ByteArrayInputStream(desktop.getBackground())));
            WatcherUtil.createWatcherThread(this, desktopLogic.getDesktopFiles());
        }
        catch (Exception e)
        {
            navigationController.setStatus(Status.ERROR);
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }

    private void inputstreamDesktop(List<apps.generic.models.File> files)
    {
        try {
            FileUtils.cleanDirectory(new File(DesktopPathsConstants.DESKTOPURL));
            for (apps.generic.models.File file : files) {
                try (FileOutputStream stream = new FileOutputStream(DesktopPathsConstants.DESKTOPURL + "/" + file.getName())) {
                    stream.write(file.getBytes());
                }
            }
        }
        catch (Exception e)
        {
            navigationController.setStatus(Status.ERROR);
            MessageUtil.showErrorMessage(Response.Status.CONFLICT.getStatusCode(),Response.Status.CONFLICT.toString(), ErrorConstants.WRONG);
        }
    }

    private byte[] backgroundToByte() throws IOException
    {
        BufferedImage bImage = SwingFXUtils.fromFXImage(imgDesktopBackground.getImage(), null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", s);
        byte[] byteBackground = s.toByteArray();
        s.close();
        return byteBackground;
    }

    public void connectGroup() {
        messageSender.requestDesktop(navigationController.getAccountController().getAccount(), txtEmail.getText());
    }

    public void uploadBackground()
    {
        DesktopUtil.uploadBackground(lblImage, desktop, imgDesktopBackground);
    }

    public void startDesktopGroup() {
        try
        {
            byte[] byteBackgound = backgroundToByte();
            if (byteBackgound != null)
            {
                desktop = new Desktop(byteBackgound, desktopLogic.getDesktopFiles());
                messageSender.startDesktopGroup(desktop, navigationController.getAccountController().getAccount());
            }
        }
        catch (IOException e)
        {
            MessageUtil.showErrorMessage(Response.Status.CONFLICT.getStatusCode(),Response.Status.CONFLICT.toString(), ErrorConstants.WRONG);
            navigationController.setStatus(Status.ERROR);
        }
    }

    @Override
    public void handleAddDesktopGroup()
    {
        btnStart.setVisible(false);
        btnStop.setVisible(true);
        mainDesktopGroupController.connectPane.setVisible(false);
        navigationController.setStatus(Status.GOOD);
    }

    @Override
    public void handleStopDesktopGroup()
    {
        Platform.runLater(() -> {
        btnStart.setVisible(true);
        btnStop.setVisible(false);
        mainDesktopGroupController.connectPane.setVisible(true);
        navigationController.setStatus(Status.NOTHING);
        lblLeden.setText("0");
        });
    }

    public void handleUpdateUsers(String users)
    {
        Platform.runLater(() -> lblLeden.setText(users));
    }

    public void handleLeaveDesktopGroup()
    {
        Platform.runLater(() -> {
            try {
                MessageUtil.showInformationMessage(InformationConstants.INFORMATION, InformationConstants.INFORMATION_DESKTOPGROUP);
                loadAddGroup();
                navigationController.setStatus(Status.NOTHING);
                mainDesktopGroupController.connectPane.setVisible(true);
            }
            catch (Exception e) {
                MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
                navigationController.setStatus(Status.ERROR);
            }
        });
    }

    public void handleRequestDesktop(DesktopGroupDTO desktopGroup)
    {
        Platform.runLater(() -> {
            try {
                inputstreamDesktop(desktopGroup.getFiles());
                desktop.setBackground(desktopGroup.getBackground());
                loadDisplayGroup(desktop);
                mainDesktopGroupController.connectPane.setVisible(false);
            } catch (Exception e) {
                MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
                navigationController.setStatus(Status.ERROR);
            }
        });
    }

    public void stopDesktopGroup() {
        messageSender.stopDesktopGroup(navigationController.getAccountController().getAccount());
    }

    private void loadDisplayGroup(Desktop desktopdisplay) throws IOException
    {
        MessageUtil.showInformationMessage(InformationConstants.INFORMATION, InformationConstants.INFORMATION_SHORTCUTS);
        DesktopGroupController desktopGroupController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_DISPLAYDESKTOPGROUP));
        AnchorPane pane = loader.load();
        desktopGroupController = loader.getController();
        desktopGroupController.displayDesktopGroupFxmlLoad(navigationController, mainDesktopGroupController, messageSender, desktopdisplay);
        mainDesktopGroupController.desktopPane.setTop(pane);
    }

    private void loadAddGroup() throws IOException
    {
        DesktopGroupController desktopGroupController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_ADDDESKTOPGROUP));
        AnchorPane pane = loader.load();
        desktopGroupController = loader.getController();
        desktopGroupController.addDesktopGroupFxmlLoad(navigationController, mainDesktopGroupController, messageSender);
        messagehandler.setDesktopGroupController(desktopGroupController);
        mainDesktopGroupController.desktopPane.setTop(pane);
    }

    public void leave()
    {
        messageSender.leaveDesktop(navigationController.getAccountController().getAccount());
    }
}
