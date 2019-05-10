package apps.websocketserver;

import apps.generic.message.DesktopGroupMessage;

import javax.websocket.Session;
import java.util.List;

public interface IDesktopGroupServerEndpoint {
    void sendMessage(DesktopGroupMessage message, Session session);
    void broadcast(DesktopGroupMessage message, List<Session> sessions);
}
