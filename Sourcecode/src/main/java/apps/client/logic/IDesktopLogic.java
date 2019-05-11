package apps.client.logic;

import apps.generic.models.Desktop;
import apps.generic.message.MessageResult;

import java.io.File;
import java.util.List;

public interface IDesktopLogic {
    List<File> getDesktopFiles();
    MessageResult getDesktops();
    MessageResult deleteDesktop(String desktopTitle);
    MessageResult selectDesktop(Desktop desktop, boolean local);
    MessageResult addDesktop(Desktop desktop);
    void changeBackground(byte[] background);

}
