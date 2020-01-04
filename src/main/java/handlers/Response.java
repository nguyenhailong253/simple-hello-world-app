package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response {

    private Gson jsonConverter = new GsonBuilder().create();
    private int statusCode;
    private Map<String, Object> responseBody;

    public Response(int statusCode, Object content) {
        this.statusCode = statusCode;
        this.responseBody = new HashMap<>();
        responseBody.put("content", content);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, Object> getResponseBody() {
        return responseBody;
    }

    public void send(HttpExchange exchange) throws IOException {
        String jsonResponseBody = jsonConverter.toJson(responseBody);
        exchange.sendResponseHeaders(statusCode, jsonResponseBody.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(jsonResponseBody.getBytes());
        outputStream.close();
    }
}

