package apps.generic.models;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

public class DesktopGroup {
private Session admin;
private ArrayList<Session> users = new ArrayList<>();
private List<File> files;
private byte[] background;

    public DesktopGroup(byte[] background, List<File> files)
    {
        this.files = files;
        this.background = background;
    }

    public void setAdmin(Session admin) {this.admin = admin;}

    public Session getAdmin() {return admin;}

    public void addUser(Session user) {users.add(user);}

    public List<Session> getUsers() {return users;}


    private Account account;
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account adminAccount) {
        this.account = adminAccount;
    }

    public byte[] getBackground() {
        return background;
    }

    public void setBackground(byte[] background) {
        this.background = background;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
