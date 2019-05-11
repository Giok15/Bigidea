package apps.websocketserver.messagehandlers;

import apps.generic.message.DesktopGroupMessage;
import apps.websocketserver.IDesktopGroupServerEndpoint;

import javax.websocket.Session;

public interface IMessageAuthenticationHandler {
    boolean handleMessageAuthentication(IDesktopGroupServerEndpoint desktopGroupServerEndpoint, DesktopGroupMessage message, Session session);
}
