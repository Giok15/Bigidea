package apps.restserver.context;

import apps.generic.models.Account;
import apps.generic.utils.AccountUtil;
import apps.generic.utils.LoggingUtil;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;

public class MssqlAuthenticationImpl implements IAuthenticationContext {

    /**
     * Connect database - dbConfig file
     *
     * @return Connection - java.sql
     */
    private Connection dbConnect() {
        Connection connection = null;
        try {
            InputStream in = MssqlAuthenticationImpl.class.getResourceAsStream("/dbConfig.properties");
            Properties prop = new Properties();
            prop.load(in);
            Class.forName(prop.getProperty("db.driver"));
            connection = DriverManager.getConnection(prop.getProperty("db.url") + ";database=" + prop.getProperty("db.database") + ";User=" + prop.getProperty("db.username") + ";Password=" + prop.getProperty("db.password"));
        } catch (IOException | ClassNotFoundException | SQLException e) {
            LoggingUtil.log(MssqlAuthenticationImpl.class.getName(), Level.SEVERE, e);
        }
        return connection;
    }


    /**
     * insert an account
     *
     * @param account - generic account model
     * @return boolean - result of query
     */
    @Override
    public boolean insertAccount(Account account) {
        boolean result = false;
        try {
            String sql = "INSERT INTO dbo.[account] (name,email,password) VALUES (?, ?, ?)";
            Connection conn = dbConnect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, account.getName());
                    pstmt.setString(2, account.getEmail());
                    pstmt.setString(3, AccountUtil.hashPassword(account.getPassword()));
                    pstmt.executeUpdate();
                    result = true;
                } catch (NoSuchAlgorithmException e) {
                    LoggingUtil.log(MssqlAuthenticationImpl.class.getName(), Level.SEVERE, e);
                }
            }
        } catch (SQLException e) {
            LoggingUtil.log(MssqlAuthenticationImpl.class.getName(), Level.SEVERE, e);
        }
        return result;
    }


    /**
     * select a account by email and password
     *
     * @param email - user email
     * @param password - user password
     * @return Account - generic account model
     */
    @Override
    public Account selectAccount(String email, String password) {
        Account account = null;
        try {
            String sql = "SELECT * FROM dbo.[account] WHERE dbo.[account].email = ? AND dbo.[account].password = ?";

            Connection conn = dbConnect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, email);
                    pstmt.setString(2, AccountUtil.hashPassword(password));

                    try (ResultSet myResults = pstmt.executeQuery()) {
                        myResults.next();
                        account = new Account(myResults.getString("name"), myResults.getString("email"), myResults.getString("password"));
                    }
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            LoggingUtil.log(MssqlAuthenticationImpl.class.getName(), Level.SEVERE, e);
        }
        return account;
    }


    /**
     * Check if account exist by email
     *
     * @param email - user email
     * @return boolean - result of query
     */
    @Override
    public boolean selectAccountByEmail(String email) {
        boolean result = false;
        try {
            String sql = "SELECT count(email) as count FROM dbo.[Account] WHERE dbo.[Account].email = ?";
            Connection conn = dbConnect();
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, email);
                    try (ResultSet myResults = pstmt.executeQuery()) {
                        int count = 0;
                        while (myResults.next()) {
                            count = myResults.getInt("count");
                        }
                        if (count > 0) {
                            result = true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LoggingUtil.log(MssqlAuthenticationImpl.class.getName(), Level.SEVERE, e);
        }
        return result;
    }
}
