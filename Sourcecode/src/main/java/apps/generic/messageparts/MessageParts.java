package apps.generic.messageparts;

import apps.generic.enums.EnumMessagePart;
import apps.generic.message.DesktopGroupMessage;
import apps.generic.models.Account;

import java.util.ArrayList;

public class MessageParts {
    private static ArrayList<MessagePart> parts = new ArrayList<>();

    public void addMessagePart(MessagePart messagePart)
    {
        parts.add(messagePart);
    }

    public void addMessage(Account account, String message)
    {
        for (MessagePart messagepart : parts)
        {
            if (messagepart.getAccount().getEmail().equals(account.getEmail()))
            {
                messagepart.addMessagePart(message);
                break;
            }
        }
    }

    public MessagePart getMessagePartByAccount(Account account)
        {
        MessagePart messagePartReturn = null;
        for (MessagePart messagepart : parts)
        {
            if (messagepart.getAccount().getEmail().equals(account.getEmail()))
            {
                messagePartReturn = messagepart;
            }
        }
        return messagePartReturn;
    }

    public void clearMessagePartByAccount(Account account)
    {
        for (MessagePart messagepart : parts)
        {
            if (messagepart.getAccount().getEmail().equals(account.getEmail()))
            {
                parts.remove(messagepart);
                break;
            }
        }
    }

    public boolean handle(DesktopGroupMessage message)
    {
        boolean result = true;

        if (message.getMessagePart() == EnumMessagePart.SETUPMESSAGEPARTS)
        {
            MessagePart messagePart = new MessagePart(message.getAccount(), Integer.parseInt(message.getContent()));
            addMessagePart(messagePart);
        }
        else if (message.getMessagePart() == EnumMessagePart.SENDMESSAGEPARTS) {
            addMessage(message.getAccount(), message.getContent());
        } else {
            result = false;
        }
        return result;
    }
}
