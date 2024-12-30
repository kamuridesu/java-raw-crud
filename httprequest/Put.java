import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

class Put {
    public static void main(String[] args) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create("https://httpbin.org/put"))
                .header("Content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"name\": \"test\"}"))
                .build();
        var response = client.send(request, BodyHandlers.ofString());
        System.out.println(response);
        System.out.println(response.body());
    }
}
