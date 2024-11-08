package httpserver;

public final class Response {
    private String body = "";
    private int statusCode = 200;
    private String contentType = "text/plain";

    public Response() {
    }

    public Response(String body) {
        this.body = body;
    }

    public Response(String body, int statusCode) {
        this.body = body;
        this.statusCode = statusCode;
    }

    public Response(String body, int statusCode, String contentType) {
        this.body = body;
        this.statusCode = statusCode;
        this.contentType = contentType;
    }

    public String getBody() {
        return this.body;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
