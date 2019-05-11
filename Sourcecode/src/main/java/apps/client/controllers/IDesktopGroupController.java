package apps.client.controllers;

import apps.generic.dto.DesktopGroupDTO;

public interface IDesktopGroupController {
    void handleAddDesktopGroup();
    void handleStopDesktopGroup();
    void handleRequestDesktop(DesktopGroupDTO desktopGroupDTO);
    void handleLeaveDesktopGroup();
    void handleUpdateUsers(String userCount);
}
