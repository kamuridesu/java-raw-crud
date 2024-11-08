package httpserver;

import java.util.function.Function;

public final class Route {
    String path;
    String method;
    Function<Request, Response> handler;

    public Route(String path, String method, Function<Request, Response> handler) {
        this.path = path;
        this.method = method;
        this.handler = handler;
    }

    public String getPath() {
        return this.path;
    }

    public String getMethod() {
        return this.method;
    }

    public Function<Request, Response> getHandler() {
        return this.handler;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setHandler(Function<Request, Response> handler) {
        this.handler = handler;
    }

    public Response runHandler(Request request) {
        return this.handler.apply(request);
    }

    public boolean matches(Request request) {
        return this.path.equals(request.getPath())
                && this.method.toLowerCase().equals(request.getMethod().toLowerCase());
    }

}
