package apps.generic.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoggingUtil {

    private LoggingUtil() {
        throw new IllegalStateException("constants class");
    }

    public static void log(String classname, Level level, Exception e)
    {
        Logger.getLogger(classname).log(level, e.toString(), e);
    }
}
