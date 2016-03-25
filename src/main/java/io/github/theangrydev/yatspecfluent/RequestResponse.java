package io.github.theangrydev.yatspecfluent;

public class RequestResponse<R> {
    private final Object request;
    private final R response;

    public RequestResponse(Object request, R response) {
        this.request = request;
        this.response = response;
    }

    public Object getRequest() {
        return request;
    }

    public R getResponse() {
        return response;
    }
}
