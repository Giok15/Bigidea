package apps.websocketserver.messagehandlers;

import apps.generic.dto.DesktopGroupDTO;
import apps.generic.enums.EnumMessageOperations;
import apps.generic.enums.EnumMessagePart;
import apps.generic.message.DesktopGroupMessage;
import apps.generic.models.DesktopGroup;
import apps.generic.utils.RequestUtil;
import apps.websocketserver.IDesktopGroupServerEndpoint;
import apps.websocketserver.handlers.DesktopGroupHandler;
import apps.websocketserver.handlers.IDesktopGroupHandler;
import com.google.common.base.Splitter;

import javax.websocket.Session;
import javax.ws.rs.core.Response;
import java.util.List;

public class MessageDesktopGroupHandler implements IMessageDesktopGroupHandler {

    private IDesktopGroupHandler desktopGroupLogic = new DesktopGroupHandler();

    @Override
    public void handleMessageOperation(IDesktopGroupServerEndpoint desktopGroupServerEndpoint, DesktopGroupMessage message, Session session) {

        switch (message.getOperation()) {
            case ADDDESKTOPGROUP:
                Response response = desktopGroupLogic.addDesktopGroup((DesktopGroup) RequestUtil.parseToObject(message.getContent(), DesktopGroup.class), message.getAccount(), session);
                message.setContent(RequestUtil.parseToString(response));
                desktopGroupServerEndpoint.sendMessage(message, session);
                break;

            case DELDESKTOPGROUP:
                List<Session> sessions = desktopGroupLogic.getUsers(session);
                if (sessions != null) {
                    message.setOperation(EnumMessageOperations.LEAVEDESKTOP);
                    desktopGroupServerEndpoint.broadcast(message, sessions);
                }
                message.setOperation(EnumMessageOperations.DELDESKTOPGROUP);
                desktopGroupServerEndpoint.sendMessage(message, session);
                desktopGroupLogic.delDesktopGroup(session);
                break;

            case REQUESTDESKOP:
                DesktopGroup desktopGroup = desktopGroupLogic.requestDesktopGroup(message.getContent(), session);
                message.setContent(RequestUtil.parseToString(new DesktopGroupDTO(desktopGroup.getBackground(), desktopGroup.getFiles())));

                sendMessagePart(message, desktopGroupServerEndpoint, session);
                updateUsers(desktopGroup, message, desktopGroupServerEndpoint);
                break;

            case LEAVEDESKTOP:
                DesktopGroup desktopGroupLeave = desktopGroupLogic.getDesktopByUser(session);
                desktopGroupLogic.leaveUser(session);
                message.setOperation(EnumMessageOperations.LEAVEDESKTOP);
                desktopGroupServerEndpoint.sendMessage(message, session);
                updateUsers(desktopGroupLeave, message, desktopGroupServerEndpoint);
                break;
        }
    }

    private void updateUsers(DesktopGroup desktopGroup, DesktopGroupMessage message, IDesktopGroupServerEndpoint desktopGroupServerEndpoint)
    {
        message.setOperation(EnumMessageOperations.UPDATEUSERS);
        message.setContent(Integer.toString(desktopGroupLogic.getUsers(desktopGroup.getAdmin()).size()));
        desktopGroupServerEndpoint.sendMessage(message, desktopGroup.getAdmin());
    }

    public void sendMessagePart(DesktopGroupMessage message, IDesktopGroupServerEndpoint desktopGroupServerEndpoint, Session session)
    {
        List<String> parts = Splitter.fixedLength(65000).splitToList(RequestUtil.parseToString(message));

        desktopGroupServerEndpoint.sendMessage(new DesktopGroupMessage(EnumMessagePart.SETUPMESSAGEPARTS, Integer.toString(parts.size()), message.getAccount()), session);

        for (String part : parts) {
            desktopGroupServerEndpoint.sendMessage(new DesktopGroupMessage(EnumMessagePart.SENDMESSAGEPARTS, part, message.getAccount()), session);
        }
    }
}
