package apps.generic.utils;

import apps.generic.models.Desktop;
import apps.generic.constants.ErrorConstants;
import com.google.common.io.Files;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class DesktopUtil {

    private DesktopUtil() {
        throw new IllegalStateException("constants class");
    }

    public static void updateGUIDesktopCount(List<File> desktopFiles, ListView lstFiles, Label lblFiles)
    {
        Platform.runLater(() -> {
            lstFiles.getItems().clear();
            lblFiles.setText(Integer.toString(desktopFiles.size()));
        });
    }

    public static void updateLstfiles(List<File> files, ListView lstFiles){
        Platform.runLater(() -> {
            for (File file : files) {

                if (!file.isHidden()) {
                    StringBuilder strFilename = new StringBuilder();
                    String name = file.getName();

                    if (file.getName().length() > 20) {
                        strFilename = handleFilename(file, strFilename, name);

                    } else {
                        strFilename.append(name);
                    }
                    lstFiles.getItems().add(strFilename.toString());
                }
            }
        });
    }

    private static StringBuilder handleFilename(File file, StringBuilder strFilename, String name)
    {
        if (!file.isDirectory()) {
            String extension = Files.getFileExtension(file.getAbsolutePath());
            int extensionLength = extension.length();
            String nameWithoutExtension = Files.getNameWithoutExtension(name);

            strFilename.append(nameWithoutExtension.substring(0, 20 - extensionLength));
            strFilename.append("....");
            strFilename.append(extension);
        } else {
            strFilename.append(name.substring(0, 20));
            strFilename.append("....");
        }
        return strFilename;
    }

    public static void uploadBackground(Label lblImage, Desktop desktop, ImageView imgDesktopBackground)
    {
        try
        {
            Stage stage = new Stage();
            stage.setTitle("Choose Background");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Background");
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                String fileName = "";
                if (file.getName().length() > 20)
                {
                    fileName = file.getName().substring(0,20) + "...";
                }
                else
                {
                    fileName = file.getName();
                }
                BufferedImage image = ImageIO.read(file);
                desktop.setBackground(DesktopUtil.backgroundToByte(image));
                imgDesktopBackground.setImage(SwingFXUtils.toFXImage(image, null));
                lblImage.setText(fileName);
            }
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.CONFLICT.getStatusCode(),Response.Status.CONFLICT.toString(), ErrorConstants.ERROR_WRONG);
        }
    }

    public static byte[] backgroundToByte(BufferedImage image) throws IOException
    {
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(image, "png", s);
        byte[] byteBackground = s.toByteArray();
        s.close();
        return byteBackground;
    }
}
