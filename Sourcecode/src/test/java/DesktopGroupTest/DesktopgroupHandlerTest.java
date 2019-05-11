package DesktopGroupTest;

import apps.generic.models.Account;
import apps.generic.models.DesktopGroup;
import apps.generic.models.File;
import apps.websocketserver.handlers.DesktopGroupHandler;
import apps.websocketserver.handlers.IDesktopGroupHandler;
import org.junit.Test;
import org.mockito.Mockito;

import javax.websocket.Session;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DesktopgroupHandlerTest {

    private IDesktopGroupHandler desktopGroupHandler = new DesktopGroupHandler();
    private static Session sessionAdmin = Mockito.mock(Session.class);
    private static Session sessionUser = Mockito.mock(Session.class);

    @Test
    public void addDesktopgroup(){

        byte[] bytes = { 3, 10, 8, 25 };
        ArrayList<File> files = new ArrayList<>(); files.add(new File(bytes, "test"));
        DesktopGroup desktopGroup = new DesktopGroup(bytes, files);
        Account account = new Account("test","test@test.nl", "test");

        Response response = desktopGroupHandler.addDesktopGroup(desktopGroup, account, sessionAdmin);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void requestDesktopGroup(){

        Account account = new Account("test","test@test.nl", "test");

        DesktopGroup desktopGroupRequest = desktopGroupHandler.requestDesktopGroup(account.getEmail(), sessionUser);

        assertNotNull(desktopGroupRequest);
        assertEquals(desktopGroupRequest.getAccount().getEmail(), account.getEmail());
    }

    @Test
    public void requestDesktopGroupByUser(){

        DesktopGroup desktopGroup = desktopGroupHandler.getDesktopByUser(sessionUser);
        assertNotNull(desktopGroup);
        assertEquals(sessionAdmin, desktopGroup.getAdmin());
    }

    @Test
    public void desktopUsers(){

        List<Session> users = desktopGroupHandler.getUsers(sessionAdmin);
        assertNotNull(users);
        assertEquals(users.get(0), sessionUser);
    }

    @Test
    public void leaveUser(){

        assertTrue(desktopGroupHandler.leaveUser(sessionUser));
    }
}
