package apps.websocketserver.messagehandlers;

import apps.generic.message.DesktopGroupMessage;
import apps.websocketserver.IDesktopGroupServerEndpoint;

import javax.websocket.Session;

public interface IMessageDesktopGroupHandler {
    void handleMessageOperation(IDesktopGroupServerEndpoint desktopGroupServerEndpoint, DesktopGroupMessage message, Session session);
}
