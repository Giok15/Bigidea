package apps.generic.utils;

import apps.client.desktopwatcher.DesktopWatcher;
import apps.generic.constants.DesktopPathsConstants;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Observer;

public final class WatcherUtil {

    private WatcherUtil() {
        throw new IllegalStateException("constants class");
    }

    private static DesktopWatcher desktopWatcher;

    public static void createWatcherThread(Observer controller, List<File> desktopFiles)
    {
        desktopWatcher = new DesktopWatcher(Paths.get(DesktopPathsConstants.DESKTOPURL));
        desktopWatcher.addObserver(controller);
        desktopWatcher.setSetupFiles(desktopFiles);
    }

    public static void stopWatcherThread() {
        desktopWatcher.interrupt();
    }
}
