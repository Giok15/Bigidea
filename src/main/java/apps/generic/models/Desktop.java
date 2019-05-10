package apps.generic.models;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Desktop {
    private String title;
    private String description;
    private byte[] background;
    private Path path;
    private List<File> files;


    public Desktop(Path path)
    {
        this.path = path;
    }

    public Desktop(String title, String description, byte[] background, Path path, List<File> files)
    {
        this.title = title;
        this.description = description;
        this.background = background;
        this.path = path;
        this.files = files;
    }

    public Desktop(byte[] background, List<File> files)
    {
        this.background = background;
        this.files = files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBackground(byte[] background) {
        this.background = background;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getTitle()
    {
        return title;
    }

    public List<File> getFiles()
    {
        return files;
    }

    public String getDescription()
    {
        return description;
    }

    public byte[] getBackground()
    {
        return background;
    }

    public Path getPath()
    {
        return path;
    }

}
