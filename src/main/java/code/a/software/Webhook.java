package code.a.software;

import java.io.InputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;

public class Webhook {
    private String url = "";
    private String prefix = "";

    public Webhook(String prefix, String url) {
        this.prefix = prefix;
        this.url = url;
    }

    private String getComputerName()
    {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();

            return hostname;
        } catch (UnknownHostException e) {
            System.out.println("Unable to determine the hostname of the computer");
        }

        return "Unknown";
    }

    public void NotifyWebHook(String message) {
        try {
            String generatedMessage = prefix + ": " + message;
            String payload = "{\"type\": \"0\", \"application\": \"Minecraft\", \"message\": \"" + message + "\", \"scope\": \""+ prefix +"\", \"device\": \"" + getComputerName() + "\", \"userAgent\": \"Spigot/PluginV1.0.0\"}";

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            /*String urlParam = URLEncoder.encode(generatedMessage, StandardCharsets.UTF_8);
            String nUrl = url.replace("{0}", urlParam);

            URL url = new URL(nUrl);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();*/
        } catch (Exception ex) {

        }
    }
}
