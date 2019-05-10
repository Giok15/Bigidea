package apps.websocketserver.messaging;

import apps.generic.message.DesktopGroupMessage;
import apps.websocketserver.IDesktopGroupServerEndpoint;
import apps.websocketserver.messagehandlers.*;

import javax.websocket.Session;

public class MessageProcessor implements IMessageProcessor {

    private IDesktopGroupServerEndpoint desktopGroupServerEndpoint;
    private IMessageAuthenticationHandler messageAuthenticationHandler = new MessageAuthenticationHandler();
    private IMessageDesktopGroupHandler messageOperationHandler = new MessageDesktopGroupHandler();
    private IMessagePartHandler messagePartHandler = new MessagePartHandler();

    @Override
    public void setDesktopGroupServerEndpoint(IDesktopGroupServerEndpoint desktopGroupServerEndpoint) {
        this.desktopGroupServerEndpoint = desktopGroupServerEndpoint;
    }

    @Override
    public void processMessage(DesktopGroupMessage message, Session session)
    {
        if (messageAuthenticationHandler.handleMessageAuthentication(desktopGroupServerEndpoint, message, session)) {
            message = messagePartHandler.handleMessagePart(message);
            if (message != null)
                messageOperationHandler.handleMessageOperation(desktopGroupServerEndpoint, message, session);
        }
    }
}
