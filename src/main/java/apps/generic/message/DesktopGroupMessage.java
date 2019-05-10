package apps.generic.message;

import apps.generic.enums.EnumMessageOperations;
import apps.generic.enums.EnumMessagePart;
import apps.generic.models.Account;

import javax.websocket.Session;

public class DesktopGroupMessage {

    private String content;

    private Session sender;
    private Account account;

    private EnumMessageOperations operation;
    private EnumMessagePart messagePart;

    public DesktopGroupMessage(EnumMessagePart messagePart, String content, Account account)
    {
        this.content = content;
        this.messagePart = messagePart;
        this.account = account;
    }

    public DesktopGroupMessage(EnumMessageOperations operation, String content, Account account)
    {
        this.content = content;
        this.operation = operation;
        this.account = account;
    }

    public DesktopGroupMessage(EnumMessageOperations operation, Account account)
    {
        this.account = account;
        this.operation = operation;
    }

    public DesktopGroupMessage(EnumMessageOperations operation, String content)
    {
        this.content = content;
        this.operation = operation;
    }

    public void setOperation(EnumMessageOperations operation){
        this.operation = operation;
    }

    public EnumMessageOperations getOperation(){
        return operation;
    }

    public EnumMessagePart getMessagePart(){
        return messagePart;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Session getSender() {
        return sender;
    }

    public void setSender(Session sender) {
        this.sender = sender;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
