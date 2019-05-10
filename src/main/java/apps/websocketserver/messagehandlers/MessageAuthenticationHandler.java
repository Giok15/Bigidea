package apps.websocketserver.messagehandlers;

import apps.generic.enums.EnumMessageOperations;
import apps.generic.message.DesktopGroupMessage;
import apps.generic.utils.RequestUtil;
import apps.websocketserver.IDesktopGroupServerEndpoint;
import apps.websocketserver.handlers.AccountHandler;
import apps.websocketserver.handlers.IAccountHandler;

import javax.websocket.Session;
import javax.ws.rs.core.Response;

public class MessageAuthenticationHandler implements IMessageAuthenticationHandler {

    private IAccountHandler accountHandler = new AccountHandler();

    @Override
    public boolean handleMessageAuthentication(IDesktopGroupServerEndpoint desktopGroupServerEndpoint, DesktopGroupMessage message, Session session)
    {
        boolean result;
        if (message.getOperation() == EnumMessageOperations.AUTHENTICATION)
        {
            desktopGroupServerEndpoint.sendMessage(new DesktopGroupMessage(EnumMessageOperations.AUTHENTICATION, RequestUtil.parseToString(Response.Status.OK)), session);
            result = accountHandler.authenticate(message.getAccount());
        }
        else
        {
            result = accountHandler.isAccountExist(message.getAccount());
        }
        return result;
    }
}
