package apps.client.clientendpoint;

import apps.generic.message.DesktopGroupMessage;

public interface IWebsocketClientEndpoint {
    void start();
    void sendMessageServer(DesktopGroupMessage message);
}
