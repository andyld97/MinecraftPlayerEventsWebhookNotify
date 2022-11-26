package code.a.software;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Webhook {
    private String url = "";
    private String prefix = "";

    public Webhook(String prefix, String url) {
        this.prefix = prefix;
        this.url = url;
    }

    public void NotifyWebHook(String message) {
        try {

            String generatedMessage = prefix + ": " + message;
            String urlParam = URLEncoder.encode(generatedMessage, StandardCharsets.UTF_8);
            String nUrl = url.replace("{0}", urlParam);

            URL url = new URL(nUrl);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
        } catch (Exception ex) {

        }
    }
}
