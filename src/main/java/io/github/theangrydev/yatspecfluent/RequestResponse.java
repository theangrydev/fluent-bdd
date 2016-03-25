package io.github.theangrydev.yatspecfluent;

public class RequestResponse<Response> {
    private final Object request;
    private final Response response;

    public RequestResponse(Object request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Object getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
