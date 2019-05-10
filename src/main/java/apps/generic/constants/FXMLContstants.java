package apps.generic.constants;

public final class FXMLContstants {

    private FXMLContstants() {
        throw new IllegalStateException("constants class");
    }

    private static final String FXML_MAINDIR = "/fxml";
    private static final String FXML_LAYOUT = "/layout";
    private static final String FXML_SCREENS = "/screens";

    public static final String FXML_ACCOUNT = FXML_MAINDIR + "/AccountGUI.fxml";
    public static final String FXML_DESKTOP = FXML_MAINDIR + "/DesktopGUI.fxml";
    public static final String FXML_DESKTOPGROUP = FXML_MAINDIR + "/DesktopGroupGUI.fxml";
    public static final String FXML_LOGIN = FXML_MAINDIR + "/LoginRegisterGUI.fxml";
    public static final String FXML_MAIN = FXML_MAINDIR + "/MainGUI.fxml";

    public static final String FXML_BOTTOM = FXML_MAINDIR + FXML_LAYOUT + "/BottomGUI.fxml";
    public static final String FXML_MENUBAR = FXML_MAINDIR + FXML_LAYOUT +"/MenuBarGUI.fxml";
    public static final String FXML_ADDEDITDESKTOP = FXML_MAINDIR + FXML_SCREENS +"/addeditDesktopGUI.fxml";
    public static final String FXML_BOXDESKTOP = FXML_MAINDIR + FXML_SCREENS +"/boxDesktopGUI.fxml";
    public static final String FXML_SELECTDESKTOP = FXML_MAINDIR + FXML_SCREENS +"/selectDesktopGUI.fxml";
    public static final String FXML_ADDDESKTOPGROUP = FXML_MAINDIR + FXML_SCREENS + "/addDesktopGroupGUI.fxml";
    public static final String FXML_DISPLAYDESKTOPGROUP = FXML_MAINDIR + FXML_SCREENS + "/DisplayDesktopGroupGUI.fxml";
}
