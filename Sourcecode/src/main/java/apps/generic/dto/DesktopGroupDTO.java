package apps.generic.dto;

import apps.generic.models.File;
import java.util.List;

public class DesktopGroupDTO {
private List<File> files;
private byte[] background;

    public DesktopGroupDTO(byte[] background, List<File> files)
    {
        this.files = files;
        this.background = background;
    }

    public byte[] getBackground() {
        return background;
    }

    public void setBackground(byte[] background) {
        this.background = background;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
