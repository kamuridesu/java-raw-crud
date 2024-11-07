package httpserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.sun.net.httpserver.HttpExchange;

public class Request {
    private HttpExchange raw;
    private String body;
    private Headers headers;
    private String method;
    private String path;
    private List<Map<String, String>> queryParams = new ArrayList<>();

    public Request(HttpExchange t) {
        this.raw = t;
        this.processRequestBody();
        this.processRequestHeaders();
        this.method = t.getRequestMethod();
        this.path = t.getRequestURI().getPath();
        this.queryParams = parseQueryParams(t.getRequestURI().getQuery());
    }

    private List<Map<String, String>> parseQueryParams(String query) {
        var params = new ArrayList<Map<String, String>>();
        if (query == null) {
            return params;
        }
        var pairs = query.split("&");
        for (var pair : pairs) {
            var keyValue = pair.split("=");
            var map = Map.of(keyValue[0], keyValue[1]);
            params.add(map);
        }
        return params;
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

    public String getBody() {
        return this.body;
    }

    public Headers getHeaders() {
        return this.headers;
    }

    public String getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public HttpExchange getRawRequest() {
        return this.raw;
    }

    public List<Map<String, String>> getQueryParams() {
        return this.queryParams;
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
