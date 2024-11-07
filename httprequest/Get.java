import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;

class Get {
    public static void main(String[] args) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                        .uri(URI.create("https://httpbin.org/get"))
                        .GET()
                        .build();
        var response = client.send(request, BodyHandlers.ofString());
        System.out.println(response);
        System.out.println(response.body());
    }    
}
