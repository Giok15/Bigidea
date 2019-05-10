package GenericTest;

import apps.generic.models.File;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FileTest {

    @Test
    public void fileWithBytesAndName(){

        byte[] bytes = { 3, 10, 8, 25 };
        String name = "Test.exe";

        File file = new File(bytes, name);

        assertEquals(file.getBytes(), file.getBytes());
        assertEquals(file.getName(), name);
    }
}
