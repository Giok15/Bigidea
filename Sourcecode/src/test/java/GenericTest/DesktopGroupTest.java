package GenericTest;

import apps.generic.models.DesktopGroup;
import apps.generic.models.File;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DesktopGroupTest {

    @Test
    public void desktopgroupWithBackgroundAndFiles(){

        byte[] bytes = { 3, 10, 8, 25 };
        List<File> files = new ArrayList<>();
        files.add(new File(bytes, "test.exe"));

        DesktopGroup desktopGroup = new DesktopGroup(bytes, files);
        assertNull(desktopGroup.getAdmin());
        assertNull(desktopGroup.getAccount());
        assertEquals(desktopGroup.getBackground(), bytes);
        assertEquals(desktopGroup.getFiles(), files);
        assertEquals(desktopGroup.getUsers(), new ArrayList<>());
    }
}
