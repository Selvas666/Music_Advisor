package advisor.external.facade;

import advisor.external.model.Authorization;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.vavr.collection.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.NoSuchElementException;

public class AuthorizationController {

    private static final String state = RandomStringUtils.random(10, true, true);
    private volatile static String code_verifier = "";

    public static Authorization authorize(String baseUrl) {
        String clientId = "71d427a1aae24bef87b4a0dd33ec6f22";
        String redirectUri = "http://localhost:8080";
        String clientSecret = "2716c1ffe8e241538482aa15359e0698";
        HttpServer httpServer;
        try {
            httpServer = createServer();
        } catch (IOException e) {
            return Authorization.builder()
                    .communicate(List.of("---FAILURE DURING SERVER INITIALIZATION---"))
                    .result(false)
                    .build();
        }

        httpServer.start();

        String compliedUrl = baseUrl +
                "/authorize?" +
                clientId +
                "&redirect_uri=" +
                redirectUri +
                "&response_type=code";
//                "&state=" +
//                state;

        System.out.printf("%s%n%s%n", "Use this link to request the access code:", compliedUrl);

        System.out.println("Waiting for a code...");

        long start = System.currentTimeMillis();
        long timeout = 60000L;

        while (code_verifier.isBlank() && System.currentTimeMillis() - start < timeout) {
        }
        httpServer.stop(1);

        if (!code_verifier.isBlank()) {
            System.out.println("Making http request for access_token...");

            String authData = prepareAuth(clientId, clientSecret);
            String body = "client_id="
                    + clientId
                    + "&code_verifier="
                    + code_verifier;

            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", authData)
                    .uri(URI.create(baseUrl + "/api/token"))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .timeout(Duration.ofMillis(10000))
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response;

            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) throw new IOException();
                System.out.println(response.body());
                return Authorization.builder()
                        .communicate(List.of("User authorized."))
                        .result(true)
                        .token(JsonParser.parseString(response.body()).getAsJsonObject().get("access_token").getAsString())
                        .build();

            } catch (InterruptedException | IOException ex) {
                ex.printStackTrace();
                return Authorization.builder()
                        .communicate(List.of("Failed to retrieve token. Try again."))
                        .result(false)
                        .build();
            }

        }
        return Authorization.builder()
                .communicate(List.of("Authorization code not found. Try again."))
                .result(false)
                .build();

    }

    private static HttpServer createServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/", createHandler());
        return httpServer;
    }

    private static HttpHandler createHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                java.util.List<NameValuePair> paramsList = URLEncodedUtils.parse(exchange.getRequestURI(), StandardCharsets.UTF_8);
                System.out.println(exchange.getRequestURI());
                NameValuePair stateParam;
                NameValuePair codeParam;
                try {
//                    stateParam = paramsList.stream().filter(param -> param.getName().equals("state")).findFirst().orElseThrow();
                    codeParam = paramsList.stream().filter(param -> param.getName().equals("code")).findFirst().orElseThrow();
                } catch (NoSuchElementException ex) {
//                    ex.printStackTrace();
                    String response = "Authorization code not found. Try again.";
                    exchange.sendResponseHeaders(500, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
//                if (!stateParam.getValue().equals(state)) {
//                    return;
//                }

                if (!codeParam.getValue().isBlank()) {
                    String response = "Got the code. Return back to your program.";
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(response);
                    System.out.println(codeParam.getValue());
                    code_verifier = codeParam.getValue();
                }
            }
        };
    }

    private static String prepareAuth(String client_id, String client_secret) {
        Base64.Encoder encoder = Base64.getEncoder();
        String toEncode = client_id + ":" + client_secret;
        return "Basic " + encoder.encodeToString(toEncode.getBytes());
    }
}
