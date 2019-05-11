package apps.client.controllers;

import apps.generic.constants.*;
import apps.generic.models.Account;
import apps.generic.enums.Status;
import apps.generic.utils.MessageUtil;
import apps.generic.utils.WatcherUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.ResourceBundle;

public class NavigationController implements Initializable {

    //all fxml controls
    @FXML

    private ImageView imgAccount;

    @FXML
    private ImageView imgDesktop;

    @FXML
    private ImageView imgDesktopGroup;

    @FXML
    private ImageView imgStatusAccount;

    @FXML
    private ImageView imgStatusDesktop;

    @FXML
    private ImageView imgStatusGroup;

    private LayoutController mainHandler;
    private Status defaultStatus;
    private ImageView defaultImage;

    private AccountController accountController = new AccountController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainHandler = new LayoutController();
        defaultStatus = Status.NOTHING;
        defaultImage = imgStatusDesktop;
    }

    public void setMainHandler(LayoutController mainHandler)
    {
        this.mainHandler = mainHandler;
    }

    public void resetControls()
    {
        imgAccount.setImage(new Image(ImgConstants.IMG_ACCOUNT));
        imgDesktop.setImage(new Image(ImgConstants.IMG_DESKTOP));
        imgDesktopGroup.setImage(new Image(ImgConstants.IMG_GROUP));
        resetStatus();
    }

    public LayoutController getMainHandler()
    {
        return mainHandler;
    }

    public void setAccountController(AccountController accountController)
    {
        this.accountController = accountController;
    }

    public AccountController getAccountController()
    {
        return accountController;
    }

    public void resetStatus()
    {
        if (defaultStatus == Status.ERROR && defaultImage == imgStatusAccount && accountController.getAccount() != null || defaultStatus == Status.ERROR &&
                defaultImage == imgStatusDesktop || defaultStatus == Status.GOOD && defaultImage == imgStatusDesktop)
            setStatus(Status.NOTHING);
    }

    public void changeStatusLoggedIn()
    {
        if (accountController.getAccount() != null)
            imgStatusGroup.setImage(new Image(DefaultConstants.OPENFILE + getClass().getResource(ImgConstants.IMG_STATUSNOTHING).getPath()));
        else
            imgStatusGroup.setImage(new Image(DefaultConstants.OPENFILE + getClass().getResource(ImgConstants.IMG_STATUSDENIED).getPath()));
    }

    public void setStatus(Status status)
    {
        defaultStatus = status;
        String c = ImgConstants.IMG_STATUSNOTHING;
        switch (status)
        {
            case GOOD:
                c = ImgConstants.IMG_STATUSGOOD;
                break;

            case ERROR:
                c = ImgConstants.IMG_STATUSERROR;
                break;

            case DENIED:
                c = ImgConstants.IMG_STATUSDENIED;
                break;

            case NOTHING:
                c = ImgConstants.IMG_STATUSNOTHING;
                break;
        }
        defaultImage.setImage(new Image(DefaultConstants.OPENFILE + getClass().getResource(c).getPath()));
    }

    public void btnNavAccount(ActionEvent actionEvent) {
        try
        {
            resetControls();
            imgAccount.setImage(new Image(ImgConstants.IMG_ACCOUNT_PRESSED));
            defaultImage = imgStatusAccount;
            WatcherUtil.stopWatcherThread();

            if (accountController.getAccount()  == null)
            {
                FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_LOGIN));
                AnchorPane paneLogin = loaderLogin.load();
                accountController = loaderLogin.getController();
                accountController.loginRegisterFxmlLoad(this);

                mainHandler.getMainPane().setRight(paneLogin);
                setLayout();
            }
            else
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_ACCOUNT));
                AnchorPane pane = loader.load();
                Account account = accountController.getAccount();
                accountController = loader.getController();
                accountController.accountFxmlLoad(this, account);

                mainHandler.getMainPane().setRight(pane);
            }
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }
//
    public void btnNavDesktop(ActionEvent actionEvent) {
        loadDesktopScreen();
    }

    public void loadDesktopScreen() {
        try
        {
            resetControls();
            imgDesktop.setImage(new Image(ImgConstants.IMG_DESKTOP_PRESSED));
            defaultImage = imgStatusDesktop;

            FXMLLoader loaderDesktop = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_DESKTOP));
            BorderPane paneDesktop = loaderDesktop.load();
            DesktopController handlerDesktop = loaderDesktop.getController();
            handlerDesktop.mainDesktopFxmlLoad(this);
            mainHandler.getMainPane().setRight(paneDesktop);
            setLayout();
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.toString(), ErrorConstants.WRONG);
        }
    }

    public void btnNavGroup(ActionEvent actionEvent) {

        if (accountController.getAccount() != null)
        {
            try
            {
                resetControls();
                imgDesktopGroup.setImage(new Image(ImgConstants.IMG_GROUP_PRESSED));
                defaultImage = imgStatusGroup;

                FXMLLoader loaderDesktopGroup = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_DESKTOPGROUP));
                BorderPane paneDesktopGroup = loaderDesktopGroup.load();
                DesktopGroupController desktopHandler = loaderDesktopGroup.getController();
                desktopHandler.mainDesktopGroupFxmlLoad(this);
                mainHandler.getMainPane().setRight(paneDesktopGroup);
                setLayout();
            }
            catch (Exception e)
            {
                MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
            }
        }
        else
        {
            MessageUtil.showErrorMessage(Response.Status.FORBIDDEN.getStatusCode(),Response.Status.FORBIDDEN.toString(), ErrorConstants.ERROR_NOACCOUNT);
        }
    }

    private void setLayout()
    {
        try
        {
            AnchorPane paneBottom = FXMLLoader.load(getClass().getResource(FXMLContstants.FXML_BOTTOM));

            mainHandler.getMainPane().setBottom(paneBottom);

        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }
}
