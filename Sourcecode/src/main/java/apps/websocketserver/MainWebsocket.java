package apps.websocketserver;

import apps.generic.utils.LoggingUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;
import java.util.logging.Level;

public class MainWebsocket {

    private static final int PORT = 8095;

    public static void main(String[] args) {
        startWebSocketServer();
    }

    private static void startWebSocketServer()
    {
        Server webSocketServer = new Server();
        ServerConnector connector = new ServerConnector(webSocketServer);
        connector.setPort(PORT);
        webSocketServer.addConnector(connector);
        ServletContextHandler webSocketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        webSocketContext.setContextPath("/");
        webSocketServer.setHandler(webSocketContext);

        try
        {
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(webSocketContext);
            wscontainer.addEndpoint(DesktopGroupServerEndpoint.class);
            webSocketServer.start();
            webSocketServer.join();
        } catch (Exception e)
        {
            LoggingUtil.log(MainWebsocket.class.getName(), Level.SEVERE, e);
        }
    }
}
