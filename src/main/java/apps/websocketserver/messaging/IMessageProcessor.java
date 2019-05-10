package apps.websocketserver.messaging;

import apps.generic.message.DesktopGroupMessage;
import apps.websocketserver.IDesktopGroupServerEndpoint;

import javax.websocket.Session;

public interface IMessageProcessor {
    void setDesktopGroupServerEndpoint(IDesktopGroupServerEndpoint desktopGroupServerEndpoint);
    void processMessage(DesktopGroupMessage message, Session session);
}
