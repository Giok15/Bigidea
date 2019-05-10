package apps.client.messaging;

import apps.client.clientendpoint.IWebsocketClientEndpoint;
import apps.generic.models.Account;
import apps.generic.models.Desktop;

public interface IMessageSender {
    void startAuthentication(Account account);
    void startDesktopGroup(Desktop desktop, Account account);
    void requestDesktop(Account account, String email);
    void stopDesktopGroup(Account account);
    void leaveDesktop(Account account);
    void setClient(IWebsocketClientEndpoint websocketClientEndpoint);
}
