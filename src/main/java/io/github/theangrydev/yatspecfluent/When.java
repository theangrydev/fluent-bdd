package io.github.theangrydev.yatspecfluent;

public interface When<Request, Response> {
    Request request();
    Response response(Request request);
}