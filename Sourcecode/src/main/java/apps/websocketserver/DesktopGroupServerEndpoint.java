package apps.websocketserver;

import apps.generic.message.DesktopGroupMessage;
import apps.generic.utils.LoggingUtil;
import apps.generic.utils.RequestUtil;
import apps.websocketserver.messaging.IMessageProcessor;
import apps.websocketserver.messaging.MessageProcessor;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;


@ServerEndpoint(value = "/desktopgroup/")
public class DesktopGroupServerEndpoint implements IDesktopGroupServerEndpoint {

    private static HashSet<Session> sessions = new HashSet<>();
    private IMessageProcessor messageProcessor = new MessageProcessor();

    public DesktopGroupServerEndpoint()
    {
        messageProcessor.setDesktopGroupServerEndpoint(this);
    }


    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        messageProcessor.processMessage((DesktopGroupMessage) RequestUtil.parseToObject(message, DesktopGroupMessage.class), session);
    }

    @OnClose
    public void onClose(Session session) {
        try{
            for(Session s: sessions){
                s.close();
            }
            sessions.clear();
        }
        catch(Exception e){
            LoggingUtil.log(DesktopGroupServerEndpoint.class.getName(), Level.SEVERE, e);
        }
    }

    @OnError
    public void onError(Throwable cause) {
        LoggingUtil.log(DesktopGroupServerEndpoint.class.getName(), Level.SEVERE, new Exception(cause));
    }

    public void sendMessage(DesktopGroupMessage message, Session session) {
        session.getAsyncRemote().sendText(RequestUtil.parseToString(message));
    }

    public void broadcast(DesktopGroupMessage message, List<Session> sessions) {
        for (Session session : sessions) {
            session.getAsyncRemote().sendText(RequestUtil.parseToString(message));
        }
    }
}
