package apps.websocketserver.messagehandlers;

import apps.generic.message.DesktopGroupMessage;

public interface IMessagePartHandler {
    DesktopGroupMessage handleMessagePart(DesktopGroupMessage message);
}
