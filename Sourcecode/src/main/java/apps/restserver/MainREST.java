package apps.restserver;

import apps.restserver.controllers.Authentication;
import apps.generic.utils.LoggingUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.logging.Level;

public class MainREST
{
    public static void main(String[] args)
    {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8090);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                Authentication.class.getCanonicalName());
        try
        {
            jettyServer.start();
            jettyServer.join();
        }
        catch(Exception e)
        {
            LoggingUtil.log(MainREST.class.getName(), Level.SEVERE, e);
        }
        finally
        {
            jettyServer.destroy();
        }
    }
}
