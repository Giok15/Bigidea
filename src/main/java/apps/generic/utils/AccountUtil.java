package apps.generic.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public final class AccountUtil {

    private AccountUtil() {
        throw new IllegalStateException("constants class");
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException
    {
        String pass = "";
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        byte[] byteDigest = messageDigest.digest(password.getBytes());
        BigInteger bigInteger = new BigInteger(1, byteDigest);

        pass = bigInteger.toString(16);
        StringBuilder builder = new StringBuilder();

        while (pass.length() < 32) {
            builder.append("0");
            pass = builder.toString();
        }
        return pass;
    }

    public static boolean isValidMail(String email) {

        String emailRegex = "^[a-zA-Z0-9.!#$%&'+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public static boolean isValidPassword(String password)
    {
        String passRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        Pattern pat = Pattern.compile(passRegex);
        return pat.matcher(password).matches();
    }
}
