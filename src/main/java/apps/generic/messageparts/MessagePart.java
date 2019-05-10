package apps.generic.messageparts;

import apps.generic.models.Account;

public class MessagePart {
    private Account account;
    private String message = "";
    private int size;
    private int curSize;

    public MessagePart(Account account, int size)
    {
        this.account = account;
        this.size = size;
    }

    public void addMessagePart(String part)
    {
        message += part;
        curSize++;
    }

    public String getMessage()
    {
        return message;
    }

    public Account getAccount()
    {
        return account;
    }

    public boolean isFinished()
    {
        return size == curSize;
    }
}
