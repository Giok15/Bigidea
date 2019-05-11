package apps.client.logic;

import apps.generic.constants.*;
import apps.generic.Background;
import apps.generic.models.Desktop;
import apps.generic.message.MessageResult;
import apps.generic.utils.MessageUtil;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DesktopLogic implements IDesktopLogic {

    public List<File> getDesktopFiles()
    {
        List<File> files = new ArrayList<>();
        for (File f: Objects.requireNonNull(new File(DesktopPathsConstants.DESKTOPURL).listFiles()))
        {
            if (!f.isHidden())
            {
              files.add(f);
            }
        }

        return files;
    }

    public MessageResult getDesktops()
    {
        MessageResult message;
        try
        {
            createMainDirIfNotExist();

            File folder = new File(DesktopPathsConstants.DOCUMENTURL);
            File[] listOfDesktops = folder.listFiles();

            Desktop[] desktops = new Desktop[listOfDesktops.length];

            message = new MessageResult(Response.Status.OK.getStatusCode(), desktops);
            for (int i = 0; i < listOfDesktops.length; i++) {
                File dirDesktop = listOfDesktops[i];

                File txtDescription = new File(dirDesktop.getPath() + DesktopPathsConstants.TXTDESCRIPTION);
                File background = new File(dirDesktop.getPath() + DesktopPathsConstants.BACKGROUND);
                File files = new File(dirDesktop.getPath() + DesktopPathsConstants.DIRFILES);

                if (txtDescription.exists() && files.exists() && background.exists())
                {
                    desktops[i] = readDirDesktop(dirDesktop, txtDescription, background);
                }
                else
                {
                    message = new MessageResult(Response.Status.NOT_FOUND.getStatusCode(), ErrorConstants.FILENOTFOUND);
                    break;
                }
            }

            return message;
        }
        catch (Exception e)
        {
            message = new MessageResult(Response.Status.CONFLICT.getStatusCode(), ErrorConstants.ERROR_UNKNOWN);
        }
        return  message;
    }

    public MessageResult deleteDesktop(String desktopTitle)
    {
        MessageResult message;

        try
        {
            File dirSelDesktop = new File(DesktopPathsConstants.DOCUMENTURL + desktopTitle);

            if (dirSelDesktop.exists())
            {
                FileUtils.forceDelete(dirSelDesktop);
            }

            message = new MessageResult(Response.Status.OK.getStatusCode());
        }
        catch (IOException e)
        {
            message = new MessageResult(Response.Status.NOT_FOUND.getStatusCode(), ErrorConstants.ERROR_FILES);
        }
        return message;
    }

    public MessageResult selectDesktop(Desktop desktop, boolean local)
    {
        MessageResult message;
        try {
            FileUtils.cleanDirectory(new File(DesktopPathsConstants.DESKTOPURL));
            FileUtils.copyDirectory(new File(desktop.getPath() + DesktopPathsConstants.DIRFILES), new File(DesktopPathsConstants.DESKTOPURL));
            changeBackground(desktop.getBackground());
            message = new MessageResult(Response.Status.OK.getStatusCode());
        }
        catch (Exception e)
        {
            message = new MessageResult(Response.Status.CONFLICT.getStatusCode(), ErrorConstants.ERROR_UNKNOWN);
        }
        return message;
    }

    public void changeBackground(byte[] background)
    {
        try {
            File file = new File(DesktopPathsConstants.DESKTOPURL + DesktopPathsConstants.BACKGROUND);
            FileUtils.writeByteArrayToFile(new File(file.getPath()), background);

            Background.Change(file.getPath());
            Path path = FileSystems.getDefault().getPath(file.getPath());
            Files.setAttribute(path, "dos:hidden", true);
        }
        catch (Exception e)
        {
            MessageUtil.showErrorMessage(Response.Status.CONFLICT.getStatusCode(), Response.Status.CONFLICT.toString(), ErrorConstants.WRONG);
        }
    }

    public MessageResult addDesktop(Desktop desktop) {

        MessageResult message;
        try {
            createMainDirIfNotExist();
            if (desktopNotExistOrConfirm(desktop)) {

                createDirectory(desktop);
                createFiles(desktop);
                setupConfig(desktop);

                message = new MessageResult(Response.Status.OK.getStatusCode());
            }
            else
            {
                message = new MessageResult(Response.Status.EXPECTATION_FAILED.getStatusCode(), ErrorConstants.ERROR_CANCELED);
            }
        }
        catch (Exception e)
        {
            message = new MessageResult(Response.Status.NOT_FOUND.getStatusCode(), ErrorConstants.ERROR_FILES);
        }
        return message;
    }


    //create main dir if not exist
    private void createMainDirIfNotExist() throws IOException
    {
        File dirMain = new File(DesktopPathsConstants.DOCUMENTURL);
        if (!dirMain.exists()) {
            FileUtils.forceMkdir(new File(DesktopPathsConstants.DOCUMENTURL));
        }
    }

    //Methods Add desktop//

    //check if desktopname already exist and give option to overwrite desktop
    private boolean desktopNotExistOrConfirm(Desktop desktop)
    {
        boolean result = true;
        if (desktop.getPath().toFile().exists())
        {
            int answer = JOptionPane.showConfirmDialog(null, InformationConstants.INFORMATION_DESKTOPEXIST, "INFORMATION", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.NO_OPTION)
            {
                result = false;
            }
        }
        return result;
    }

    //create new directory for added Desktop
    private void createDirectory(Desktop desktop) throws IOException
    {
        File file = desktop.getPath().toFile();
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }

        FileUtils.forceMkdir(file);
    }

    //create desktopconfig with background and description information
    private void setupConfig(Desktop desktop) throws IOException
    {
        File txtConfigFile = new File(desktop.getPath().toFile().getAbsolutePath() + "/description.txt");
        try (FileOutputStream fos = new FileOutputStream(desktop.getPath().toFile()+"/background.png")) {
            fos.write(desktop.getBackground());
        }
        PrintWriter writer = new PrintWriter(txtConfigFile, "UTF-8");
        writer.println(desktop.getDescription());
        writer.flush();
        writer.close();
    }

    //copy desktop files in the added desktopdirectory
    private void createFiles(Desktop desktop) throws IOException
    {
        for(File file : desktop.getFiles())
            FileUtils.copyFileToDirectory(file, new File(desktop.getPath().toFile().getAbsolutePath() + "/files"));
    }

    private Desktop readDirDesktop(File dirDesktop, File txtDescription, File background) throws IOException {
       try (BufferedReader br = new BufferedReader(new FileReader(txtDescription)))
        {
            StringBuilder buildDescription = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                buildDescription.append(line);
                buildDescription.append(System.lineSeparator());
                line = br.readLine();
            }

            return new Desktop(dirDesktop.getName(), buildDescription.toString(),  Files.readAllBytes(background.toPath()), dirDesktop.toPath(), new ArrayList<>(Arrays.asList(new File(dirDesktop.getPath() + DesktopPathsConstants.DIRFILES).listFiles())));
        }
    }
}
