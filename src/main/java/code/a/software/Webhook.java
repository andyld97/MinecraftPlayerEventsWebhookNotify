package code.a.software;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Webhook {
    private String url = "";
    private String prefix = "";

    public Webhook(String prefix, String url) {
        this.prefix = prefix;
        this.url = url;
    }

    public String getPrefix()
    {
        return prefix;
    }

    private String getComputerName()
    {
        try {
            InetAddress addr = InetAddress.getLocalHost();

            return addr.getHostName();
        } catch (UnknownHostException e) {
            System.out.println("Unable to determine the hostname of the computer");
        }

        return "Unknown";
    }

    public void NotifyWebHook(String message) {
        try {
            String payload = "{\"type\": \"0\", \"application\": \"Minecraft\", \"message\": \"" + message + "\", \"scope\": \""+ prefix +"\", \"device\": \"" + getComputerName() + "\", \"userAgent\": \"Spigot/PluginV1.0.0\"}";

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            // ignore
        }
    }
}
