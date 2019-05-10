package apps.websocketserver.handlers;

import apps.generic.constants.RESTConstants;
import apps.generic.dto.HttpResponse;
import apps.generic.models.Account;
import apps.generic.utils.RequestUtil;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

public class AccountHandler implements IAccountHandler{
    private static ArrayList<Account> accounts = new ArrayList<>();


    public boolean authenticate(Account account)
    {
      HttpResponse response = RequestUtil.sendPostRequest(RESTConstants.URL + RESTConstants.AUTHENTICATION + "/login", RequestUtil.parseToString(account));

        boolean result = false;
        if (response != null && response.getResponseCode() == Response.Status.OK.getStatusCode()) {
                accounts.add(account);
                result = true;
        }
        return result;
    }

    public boolean isAccountExist(Account account)
    {
        boolean result = false;
        for (Account authenAccounts : accounts)
        {
            if (authenAccounts.getEmail().equals(account.getEmail()))
            {
               result = true;
            }
        }
        return result;
    }
}
