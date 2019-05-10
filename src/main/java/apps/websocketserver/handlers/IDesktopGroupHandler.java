package apps.websocketserver.handlers;

import apps.generic.models.Account;
import apps.generic.models.DesktopGroup;

import javax.websocket.Session;
import javax.ws.rs.core.Response;
import java.util.List;

public interface IDesktopGroupHandler {
    Response addDesktopGroup(DesktopGroup desktopGroup, Account account, Session session);
    Response delDesktopGroup(Session session);
    DesktopGroup requestDesktopGroup(String email, Session session);
    DesktopGroup getDesktopByUser(Session session);
    List<Session> getUsers(Session admin);
    boolean leaveUser(Session session);
}
