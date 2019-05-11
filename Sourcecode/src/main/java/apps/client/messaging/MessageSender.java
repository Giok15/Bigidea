package apps.client.messaging;

import apps.client.clientendpoint.IWebsocketClientEndpoint;
import apps.generic.constants.ErrorConstants;
import apps.generic.enums.EnumMessageOperations;
import apps.generic.enums.EnumMessagePart;
import apps.generic.models.Account;
import apps.generic.models.Desktop;
import apps.generic.models.DesktopGroup;
import apps.generic.utils.MessageUtil;
import apps.generic.utils.RequestUtil;
import apps.generic.message.DesktopGroupMessage;
import com.google.common.base.Splitter;
import org.apache.commons.io.FileUtils;

import javax.ws.rs.core.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MessageSender implements IMessageSender{

    private IWebsocketClientEndpoint websocketClientEndpoint;

    public void setClient(IWebsocketClientEndpoint websocketClientEndpoint)
    {
        this.websocketClientEndpoint = websocketClientEndpoint;
    }

    public void startAuthentication(Account account) {
        websocketClientEndpoint.sendMessageServer(new DesktopGroupMessage(EnumMessageOperations.AUTHENTICATION, account));
    }

    public void startDesktopGroup(Desktop desktop, Account account) {

        try
        {
            ArrayList<apps.generic.models.File> files = new ArrayList<>();
            for (File f : desktop.getFiles()) {
                files.add(new apps.generic.models.File(FileUtils.readFileToByteArray(f), f.getName()));
            }

            DesktopGroup desktopGroup = new DesktopGroup(desktop.getBackground(), files);
            String message = RequestUtil.parseToString(new DesktopGroupMessage(EnumMessageOperations.ADDDESKTOPGROUP, RequestUtil.parseToString(desktopGroup), account));
            sendMessagePart(message, account);
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.toString(), ErrorConstants.FILENOTFOUND);
        }
    }

    public void requestDesktop(Account account, String email) {
        websocketClientEndpoint.sendMessageServer(new DesktopGroupMessage(EnumMessageOperations.REQUESTDESKOP, email, account));
    }

    public void stopDesktopGroup(Account account) {
        websocketClientEndpoint.sendMessageServer(new DesktopGroupMessage(EnumMessageOperations.DELDESKTOPGROUP, account));
    }

    public void leaveDesktop(Account account)
    {
        websocketClientEndpoint.sendMessageServer(new DesktopGroupMessage(EnumMessageOperations.LEAVEDESKTOP, account));
    }

    public void sendMessagePart(String message, Account account)
    {
        List<String> parts = Splitter.fixedLength(65000).splitToList(message);

        websocketClientEndpoint.sendMessageServer(new DesktopGroupMessage(EnumMessagePart.SETUPMESSAGEPARTS, Integer.toString(parts.size()), account));

        for (String part : parts) {
            websocketClientEndpoint.sendMessageServer(new DesktopGroupMessage(EnumMessagePart.SENDMESSAGEPARTS, part, account));
        }
    }
}
