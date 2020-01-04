package acceptance;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MockClient {
    private static HttpURLConnection connection;

    public MockClient(URL url) throws IOException {
        this.connection = (HttpURLConnection)url.openConnection();
    }

    public void sendRequest(String requestMethod, String requestBody) throws IOException {
        connection.setRequestMethod(requestMethod);
        send(requestBody);
    }

    public void sendRequest(String requestMethod) throws IOException {
        connection.setRequestMethod(requestMethod);
    }

    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    public String getResponseMessage() throws IOException {
        InputStream inputStream = getResponseCode() < 300 ? connection.getInputStream() : connection.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        return reader.readLine();
    }

    private void send(String requestBody) throws IOException {
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(requestBody.getBytes());
        os.close();
    }

}
