package GenericTest;

import apps.generic.models.Desktop;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DesktopTest {

    @Test
    public void desktopWithPath(){

        Path path = Mockito.mock(Path.class);

        Desktop desktop = new Desktop(path);
        assertEquals(desktop.getPath(), path);
        assertNull(desktop.getBackground());
        assertNull(desktop.getFiles());
        assertNull(desktop.getTitle());
        assertNull(desktop.getDescription());
    }

    @Test
    public void desktopWithBackgroundAndFiles(){

        byte[] bytes = { 3, 10, 8, 25 }; ;
        List<File> files = new ArrayList<>();
        files.add(Mockito.mock(File.class));

        Desktop desktop = new Desktop( bytes, files);
        assertEquals(desktop.getBackground(), bytes);
        assertEquals(desktop.getFiles(), files);
        assertNull(desktop.getTitle());
        assertNull(desktop.getDescription());
        assertNull(desktop.getPath());

    }

    @Test
    public void desktopWithAllProps(){

        byte[] bytes = { 3, 10, 8, 25 }; ;
        Path path = Mockito.mock(Path.class);
        List<File> files = new ArrayList<>();
        files.add(Mockito.mock(File.class));

        Desktop desktop = new Desktop("Test", "Hello World", bytes, path, files);
        assertEquals("Hello World",desktop.getDescription());
        assertEquals("Test", desktop.getTitle());
        assertEquals(desktop.getBackground(), bytes);
        assertEquals(desktop.getPath(), path);
        assertEquals(desktop.getFiles(), files);
    }
}
