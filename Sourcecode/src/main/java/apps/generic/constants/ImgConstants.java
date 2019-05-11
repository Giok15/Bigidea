package apps.generic.constants;

public final class ImgConstants {

    private ImgConstants() {
        throw new IllegalStateException("constants class");
    }

    private static final String IMG_MAINDIR = "/images";
    public static final String IMG_ACCOUNT = IMG_MAINDIR + "/navigation/account.png";
    public static final String IMG_DESKTOP = IMG_MAINDIR + "/navigation/desktop.png";
    public static final String IMG_GROUP = IMG_MAINDIR + "/navigation/group.png";

    public static final String IMG_ACCOUNT_PRESSED = IMG_MAINDIR + "/navigation/account_pressed.png";
    public static final String IMG_DESKTOP_PRESSED = IMG_MAINDIR + "/navigation/desktop_pressed.png";
    public static final String IMG_GROUP_PRESSED = IMG_MAINDIR + "/navigation/group_pressed.png";

    public static final String IMG_STATUSGOOD = IMG_MAINDIR + "/status/good.png";
    public static final String IMG_STATUSERROR = IMG_MAINDIR + "/status/error.png";
    public static final String IMG_STATUSDENIED = IMG_MAINDIR + "/status/denied.png";
    public static final String IMG_STATUSNOTHING = IMG_MAINDIR + "/status/nothing.png";
}
