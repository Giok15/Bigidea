package apps.generic.models;

public class File {

    private byte[] bytes;
    private String name;

    public File(byte[] bytes, String name)
    {
        this.bytes = bytes;
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getName() {
        return name;
    }
}
