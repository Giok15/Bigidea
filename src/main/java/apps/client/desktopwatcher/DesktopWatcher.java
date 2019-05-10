package apps.client.desktopwatcher;

import apps.generic.constants.DefaultConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Observable;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

public class DesktopWatcher extends Observable implements Runnable {

    private Thread trDirWatcher;
    private Path path;
    private List<File> files;

    public DesktopWatcher(Path path){
        this.path = path;
        start();
        change();
    }

    public void setSetupFiles( List<File> files)
    {
        this.files = files;
        change();
    }

    private void start()
    {
        trDirWatcher = new Thread(this);
        trDirWatcher.setName("DirWatcher");
        trDirWatcher.start();
    }

    public void interrupt()
    {
        trDirWatcher.interrupt();
    }

    @Override
    public void run() {
        Path dir = this.path;
        WatchKey key;
        WatchService watcher;

        while (!trDirWatcher.isInterrupted()){
            try {
                watcher = FileSystems.getDefault().newWatchService();
                dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
                key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    path = Paths.get(dir + DefaultConstants.BACKSLASH + cast(event).context().toString());
                    if(event.kind() == ENTRY_CREATE) {
                        addFile(path.toFile());
                    } else if(event.kind() == ENTRY_DELETE) {
                        deleteFile(path.toFile());
                    }
                }
                key.reset();
            } catch (IOException | InterruptedException e)
            {
                trDirWatcher.interrupt();
            }
        }
    }

    private static WatchEvent<Path> cast(WatchEvent<?> event) {
        return (WatchEvent<Path>)event;
    }

    private void addFile(File file){
        files.add(file);
        change();
    }

    private void deleteFile(File file){

        files.remove(file);
        change();
    }

    private void change()
    {
        setChanged();
        notifyObservers(files);
    }
}