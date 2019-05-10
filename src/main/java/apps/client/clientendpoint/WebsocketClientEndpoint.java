package apps.client.clientendpoint;

import apps.client.messaging.IMessageHandler;
import apps.client.messaging.MessageHandler;
import apps.generic.constants.WebsocketConstants;
import apps.generic.utils.LoggingUtil;
import apps.generic.utils.RequestUtil;
import apps.generic.message.DesktopGroupMessage;

import javax.websocket.*;
import java.net.URI;
import java.util.logging.Level;


@ClientEndpoint
public class WebsocketClientEndpoint implements IWebsocketClientEndpoint {

    private Session session;
    private IMessageHandler messageHandler = new MessageHandler();


    @OnMessage
    public void onWebSocketText(String message)
    {
        messageHandler.handle((DesktopGroupMessage) RequestUtil.parseToObject(message, DesktopGroupMessage.class));
    }

    @OnClose
    public void onWebSocketClose()
    {
       session = null;
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        LoggingUtil.log(WebsocketClientEndpoint.class.getName(), Level.SEVERE, new Exception(cause));
    }

    public void sendMessageServer(DesktopGroupMessage message) {
        session.getAsyncRemote().sendText(RequestUtil.parseToString(message));
    }

    public void start() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI(WebsocketConstants.URL));

        } catch (Exception e) {
            LoggingUtil.log(WebsocketClientEndpoint.class.getName(), Level.SEVERE, e);
        }
    }
}
