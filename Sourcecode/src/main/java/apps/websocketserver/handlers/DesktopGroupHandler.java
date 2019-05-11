package apps.websocketserver.handlers;

import apps.generic.models.Account;
import apps.generic.models.DesktopGroup;
import apps.generic.utils.LoggingUtil;

import javax.websocket.Session;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DesktopGroupHandler implements IDesktopGroupHandler
{
    private static ArrayList<DesktopGroup> desktopGroups = new ArrayList<>();

    public Response addDesktopGroup(DesktopGroup desktopGroup, Account account, Session session)
    {
        Response.ResponseBuilder response = Response.status(Response.Status.BAD_REQUEST);
        try
        {
            desktopGroup.setAccount(account);
            desktopGroup.setAdmin(session);
            desktopGroups.add(desktopGroup);
            response.status(Response.Status.OK);
        }
        catch (Exception e)
        {
            response.status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return response.build();
    }

    public Response delDesktopGroup(Session session)
    {
        Response.ResponseBuilder response = Response.status(Response.Status.BAD_REQUEST);
        try
        {
            for (DesktopGroup desktop : desktopGroups)
            {
                if (desktop.getAdmin() == session)
                {
                    desktopGroups.remove(desktop);
                    response.status(Response.Status.OK);
                }
            }
        }
        catch (Exception e)
        {
            response.status(Response.Status.INTERNAL_SERVER_ERROR);
            LoggingUtil.log(DesktopGroupHandler.class.getName(), Level.SEVERE, e);
        }

        return response.build();
    }

    public DesktopGroup requestDesktopGroup(String email, Session session)
    {
        DesktopGroup desktopgroup = null;
        for (DesktopGroup desktopGroupCur : desktopGroups)
        {
            if (desktopGroupCur.getAccount().getEmail().equals(email))
            {
                desktopGroupCur.addUser(session);
                desktopgroup = desktopGroupCur;
                break;
            }
        }

        return desktopgroup;
    }

    public DesktopGroup getDesktopByUser(Session session)
    {
        DesktopGroup desktopGroup = null;
        try
        {
            for (DesktopGroup desktop : desktopGroups)
            {
                if (desktop.getUsers().contains(session) || desktop.getAdmin() == session)
                {
                    desktopGroup = desktop;
                    break;
                }
            }
        }
        catch (Exception e)
        {
            LoggingUtil.log(DesktopGroupHandler.class.getName(), Level.SEVERE, e);
        }

        return desktopGroup;
    }

    public List<Session> getUsers(Session admin)
    {
        List<Session> users = null;
        try
        {
            for (DesktopGroup desktop : desktopGroups)
            {
                if (desktop.getAdmin() == admin)
                {
                    users = desktop.getUsers();
                }
            }
        }
        catch (Exception e)
        {
            LoggingUtil.log(DesktopGroupHandler.class.getName(), Level.SEVERE, e);
        }

        return users;
    }

    public boolean leaveUser(Session session)
    {
        boolean result = false;
        try
        {
            for (DesktopGroup desktop : desktopGroups)
            {
                if (desktop.getUsers().contains(session))
                {
                    result = desktop.getUsers().remove(session);
                }
            }
        }
        catch (Exception e)
        {
            LoggingUtil.log(DesktopGroupHandler.class.getName(), Level.SEVERE, e);
        }
        return result;
    }
}
