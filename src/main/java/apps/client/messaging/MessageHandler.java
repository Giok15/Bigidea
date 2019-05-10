package apps.client.messaging;

import apps.client.controllers.DesktopGroupController;
import apps.client.controllers.IDesktopGroupController;
import apps.generic.dto.DesktopGroupDTO;
import apps.generic.enums.EnumMessageOperations;
import apps.generic.utils.RequestUtil;
import apps.generic.message.DesktopGroupMessage;
import apps.generic.messageparts.MessageParts;

public class MessageHandler implements IMessageHandler{

    private MessageParts messagePartHandler = new MessageParts();

    private static IDesktopGroupController desktopGroupController = new DesktopGroupController();
    DesktopGroupMessage message;

    public void handle(DesktopGroupMessage message)
    {
        this.message = message;
        if (!handleMessagePart())
        {
            handleMessageOperation();
        }
    }

    public void setDesktopGroupController(IDesktopGroupController controller) {
        desktopGroupController = controller;
    }

        private void handleMessageOperation() {
            if (message.getOperation() == EnumMessageOperations.ADDDESKTOPGROUP) {
                desktopGroupController.handleAddDesktopGroup();
            }
            if (message.getOperation() == EnumMessageOperations.DELDESKTOPGROUP) {
                desktopGroupController.handleStopDesktopGroup();
            }
            if (message.getOperation() == EnumMessageOperations.REQUESTDESKOP) {
                desktopGroupController.handleRequestDesktop((DesktopGroupDTO) RequestUtil.parseToObject(message.getContent(), DesktopGroupDTO.class));
            }
            if (message.getOperation() == EnumMessageOperations.LEAVEDESKTOP) {
                desktopGroupController.handleLeaveDesktopGroup();
            }
            if (message.getOperation() == EnumMessageOperations.UPDATEUSERS) {
                desktopGroupController.handleUpdateUsers(message.getContent());
            }
        }

    private boolean handleMessagePart() {
        boolean result = false;
        if (messagePartHandler.handle(message)) {
            result = isFinished();
        }
        return result;
    }

    private boolean isFinished()
    {
        boolean result = true;
        if (messagePartHandler.getMessagePartByAccount(message.getAccount()).isFinished()) {
            message = (DesktopGroupMessage) RequestUtil.parseToObject(messagePartHandler.getMessagePartByAccount(message.getAccount()).getMessage(), DesktopGroupMessage.class);
            messagePartHandler.clearMessagePartByAccount(message.getAccount());
            result = false;
        }
        return result;
    }
}
