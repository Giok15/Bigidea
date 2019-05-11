package apps.client.controllers;

import apps.generic.dto.HttpResponse;
import apps.generic.models.Account;
import apps.generic.enums.Status;
import apps.generic.constants.ErrorConstants;
import apps.generic.constants.FXMLContstants;
import apps.generic.constants.RESTConstants;
import apps.generic.utils.MessageUtil;
import apps.generic.utils.RequestUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javax.ws.rs.core.Response;
import java.io.IOException;

public class AccountController {

    //all fxml controls
    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtRName;

    @FXML
    private TextField txtREmail;

    @FXML
    private TextField txtRPassword;

    @FXML
    private TextField txtRPasswordConf;

    @FXML
    private Label lblName;

    @FXML
    private Label lblEmail;

    private Account account;
    private NavigationController navigationHandler;


    /**
     * send en checks login data
     */
    public void login() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (!email.equals("") && !password.equals("")) {
            HttpResponse response = RequestUtil.sendPostRequest(RESTConstants.URL + RESTConstants.AUTHENTICATION + "login", RequestUtil.parseToString(new Account(email, password)));
            if (response != null) {
                if (response.getResponseCode() == Response.Status.OK.getStatusCode()) {
                    navigationHandler.setStatus(Status.GOOD);
                    account = (Account) RequestUtil.parseToObject(response.getData(), Account.class);
                    redirectToAccountPage();
                } else {
                    handleResponseError(response);
                }
            } else {
                MessageUtil.showErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), Response.Status.INTERNAL_SERVER_ERROR.toString(), ErrorConstants.WRONG);
            }
        } else {
            MessageUtil.showErrorMessage(Response.Status.EXPECTATION_FAILED.getStatusCode(), Response.Status.EXPECTATION_FAILED.toString(), ErrorConstants.EMPTYFIELDS);
        }
    }


    /**
     * send en checks register data
     */
    public void register() {
        String name = txtRName.getText();
        String email = txtREmail.getText();
        String password = txtRPassword.getText();
        String passwordConf = txtRPasswordConf.getText();
        navigationHandler.setStatus(Status.ERROR);

        if (!name.equals("") && !email.equals("") && !password.equals("")) {
            if (password.equals(passwordConf)) {
                HttpResponse response = RequestUtil.sendPostRequest(RESTConstants.URL + RESTConstants.AUTHENTICATION + "register", RequestUtil.parseToString(new Account(name, email, password)));
                if (response != null) {
                    if (response.getResponseCode() == Response.Status.CREATED.getStatusCode()) {
                        resetRegister();
                    } else {
                        handleResponseError(response);
                    }
                } else {
                    MessageUtil.showErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(), Response.Status.BAD_REQUEST.toString(), ErrorConstants.ERROR_WRONG);
                }
            } else {
                MessageUtil.showErrorMessage(Response.Status.EXPECTATION_FAILED.getStatusCode(), Response.Status.EXPECTATION_FAILED.toString(), ErrorConstants.ERROR_PASCONF);
                txtRPasswordConf.setText("");
                txtRPassword.setText("");
            }
        } else {
            MessageUtil.showErrorMessage(Response.Status.EXPECTATION_FAILED.getStatusCode(), Response.Status.EXPECTATION_FAILED.toString(), ErrorConstants.ERROR_EMPTYFIELDS);
        }
    }


    /**
     * after login redirect to account page
     */
    private void redirectToAccountPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_ACCOUNT));
            AnchorPane pane = loader.load();
            AccountController accountController = loader.getController();
            accountController.accountFxmlLoad(navigationHandler, account);
            navigationHandler.changeStatusLoggedIn();
            navigationHandler.getMainHandler().getMainPane().setRight(pane);
        } catch (IOException e) {
            navigationHandler.setStatus(Status.ERROR);
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }


    /**
     * logout and get back to inlog/register page
     */
    public void logout() {
        try {
            account = null;
            FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource(FXMLContstants.FXML_LOGIN));
            AnchorPane paneLogin = loaderLogin.load();
            AccountController accountController = loaderLogin.getController();
            accountController.loginRegisterFxmlLoad(navigationHandler);
            navigationHandler.setStatus(Status.NOTHING);
            navigationHandler.changeStatusLoggedIn();
            navigationHandler.getMainHandler().getMainPane().setRight(paneLogin);
        } catch (Exception e) {
            navigationHandler.setStatus(Status.ERROR);
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.toString(), ErrorConstants.ERROR_UNKNOWN);
        }
    }


    /**
     * reset page if registered
     */
    private void resetRegister() {
        navigationHandler.setStatus(Status.GOOD);
        txtRName.setText("");
        txtREmail.setText("");
        txtRPassword.setText("");
        txtRPasswordConf.setText("");
    }


    /**
     * show response error from the server
     *
     * @param response
     */
    private void handleResponseError(HttpResponse response) {
        String message;
        if (!response.getData().equals(""))
            message = response.getData();
        else
            message = ErrorConstants.ERROR_WRONG;
        MessageUtil.showErrorMessage(response.getResponseCode(), response.getResponseMessage(), message);
    }


    /**
     * handles if accountGUI screen must loaded
     *
     * @param navigationHandler - controller for navigation
     * @param account - generic account model
     */
    public void accountFxmlLoad(NavigationController navigationHandler, Account account) {
        this.navigationHandler = navigationHandler;
        this.account = account;
        navigationHandler.setAccountController(this);

        lblName.setText(account.getName());
        lblEmail.setText(account.getEmail());
    }


    /**
     * handles if loginGUI screen must loaded
     *
     * @param navigationHandler - controller for navigation
     */
    public void loginRegisterFxmlLoad(NavigationController navigationHandler) {
        this.navigationHandler = navigationHandler;
        navigationHandler.setAccountController(this);
    }


    public Account getAccount() {
        return account;
    }
}
