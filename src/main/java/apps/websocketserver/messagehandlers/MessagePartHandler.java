package apps.websocketserver.messagehandlers;

import apps.generic.message.DesktopGroupMessage;
import apps.generic.messageparts.MessageParts;
import apps.generic.utils.RequestUtil;

public class MessagePartHandler implements IMessagePartHandler {

     private MessageParts messageParts = new MessageParts();

    @Override
    public DesktopGroupMessage handleMessagePart(DesktopGroupMessage message) {
        DesktopGroupMessage result = message;
        if (messageParts.handle(message)) {
            if (messageParts.getMessagePartByAccount(message.getAccount()).isFinished()) {
                result = (DesktopGroupMessage) RequestUtil.parseToObject(messageParts.getMessagePartByAccount(message.getAccount()).getMessage(), DesktopGroupMessage.class);
                messageParts.clearMessagePartByAccount(message.getAccount());
            }
            else {
                result = null;
            }
        }
        return result;
    }
}
