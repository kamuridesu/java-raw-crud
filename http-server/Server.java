import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
    public static void main(String[] args) throws Exception {
        var server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/", new Handler());
        server.setExecutor(null);
        System.out.println("Server started at address " + server.getAddress());
        server.start();
    }

    static class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            var request = new Request(t);
            System.out.println("Connection from " + t.getRemoteAddress());
            System.out.println("Request body: " + request.getBody());
            System.out.println("Headers: \n" + request.getHeaders() + "\n================");
            var response = "OK";
            t.sendResponseHeaders(200, response.length());
            var os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}


class Headers extends com.sun.net.httpserver.Headers {
    @Override
    public String toString() {
        var s = "";
        for (var key : this.keySet()) {
            s += key + ": " + this.get(key).get(0) + "\n";
        }
        return s;
    }
}


class Request {
    private HttpExchange raw;
    private String body;
    private Headers headers;

    Request(HttpExchange t) {
        this.raw = t;
        this.processRequestBody();
        this.processRequestHeaders();
    }

    private void processRequestBody() {
        var s = new Scanner(this.raw.getRequestBody()).useDelimiter("\\A");
        this.body = s.hasNext() ? s.next() : "";
        s.close();
    }

    private void processRequestHeaders() {
        var headers = new Headers();
        headers.putAll(this.raw.getRequestHeaders());
        this.headers = headers;
    }

    String getBody() {
        return this.body;
    }

    Headers getHeaders() {
        return this.headers;
    }

    HttpExchange getRawRequest() {
        return this.raw;
    }

}
