package apps.client.messaging;

import apps.client.controllers.IDesktopGroupController;
import apps.generic.message.DesktopGroupMessage;

public interface IMessageHandler {
    void setDesktopGroupController(IDesktopGroupController desktopGroupController);
    void handle(DesktopGroupMessage message);
}
