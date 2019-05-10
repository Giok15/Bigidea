package apps.generic.utils;

import apps.generic.dto.HttpResponse;
import com.google.gson.Gson;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static javax.ws.rs.core.HttpHeaders.USER_AGENT;

public final class RequestUtil {

    private RequestUtil() {
        throw new IllegalStateException("constants class");
    }

    public static HttpResponse sendPostRequest(String requestUrl, String body) {
        try {
            URL obj = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Content-Length", Integer.toString(body.length()));

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(body);
            wr.flush();
            wr.close();

            HttpResponse httpResponse = new HttpResponse(con.getResponseCode(), con.getResponseMessage());

            BufferedReader reader;
            if (con.getResponseCode() == Response.Status.OK.getStatusCode() || con.getResponseCode() == Response.Status.CREATED.getStatusCode()) {

                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }
            else
            {
                reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder r = new StringBuilder();

            if ((inputLine = reader.readLine()) != null)
                r.append(inputLine);
            reader.close();

            if (r.length() > 0)
                httpResponse.setData(r.toString());

            return httpResponse;
        } catch (Exception e) {
            return null;
        }
    }

    public static String parseToString(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static Object parseToObject(String message, Class objectClass) {
        Gson gson = new Gson();
        return gson.fromJson(message, objectClass);
    }
}
