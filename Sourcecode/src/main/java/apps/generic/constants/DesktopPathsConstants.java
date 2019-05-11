package apps.generic.constants;

public final class DesktopPathsConstants {

    private DesktopPathsConstants() {
        throw new IllegalStateException("constants class");
    }

    public static final String DESKTOPURL = System.getProperty("user.home") + "/Desktop";
    public static final String DOCUMENTURL = System.getProperty("user.home") + "/Documents/BigIdea/";
    public static final String TXTDESCRIPTION = "/description.txt";
    public static final String DIRFILES = "/files";
    public static final String BACKGROUND = "/background.png";
}
