package handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {
    private HttpExchange exchange;

    public Request(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public String getMethod() {
        return exchange.getRequestMethod();
    }

    public String getURI() {
        return exchange.getRequestURI().getPath();
    }

    public String getBody() throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String requestBody = new String();
        String dataFromReader = bufferedReader.readLine();

        while (dataFromReader != null) {
            requestBody += dataFromReader;
            dataFromReader = bufferedReader.readLine();
        }
        return requestBody;
    }
}
